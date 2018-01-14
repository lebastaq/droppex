package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"net/url"
	"os"

	jwtmiddleware "github.com/auth0/go-jwt-middleware"
	jwt "github.com/dgrijalva/jwt-go"
	pbl "github.com/freddygv/droppex/app-server/leader"
	pb "github.com/freddygv/droppex/app-server/servrpc"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

const (
	ctrlRPCPort    string = ":50100"
	ctrlSocketPort string = ":29200"
	version        string = "/api/1"
	maxUploadSize  int64  = 500 * 1024 * 1024 // 500 MB
)

var ctrlAddress = "104.198.48.195" // initial value
var signingKey = []byte("fY6k6km3Pw1txgPa7Qc73AYqUNZj6Wg8")

func main() {
	go startRPCServer()

	router := mux.NewRouter()

	router.HandleFunc(version+"/status", statusHandler).Methods("GET")
	router.HandleFunc(version+"/auth", authHandler).Methods("GET")

	router.Handle(version+"/search", jwtMiddleware.Handler(searchHandler)).Methods("GET")
	router.Handle(version+"/search/{pattern}", jwtMiddleware.Handler(searchHandler)).Methods("GET")
	router.Handle(version+"/files/{filename}", jwtMiddleware.Handler(downloadHandler)).Methods("GET")
	router.Handle(version+"/files_post", jwtMiddleware.Handler(uploadHandler)).Methods("POST")
	router.Handle(version+"/delete/{filename}", jwtMiddleware.Handler(deleteHandler)).Methods("POST")

	log.Println("Starting HTTP server on port:", 8000)
	log.Fatal(http.ListenAndServeTLS(":8000", "domain.crt", "domain.key", handlers.LoggingHandler(os.Stdout, router)))
}

// server is used to implement helloworld.GreeterServer.
type server struct{}

type jwtToken struct {
	Token string `json:"token"`
}

type file struct {
	Filename    string `json:"filename,omitempty"`
	SizeInBytes int64  `json:"size,omitempty"`
}

var jwtMiddleware = jwtmiddleware.New(jwtmiddleware.Options{
	ValidationKeyGetter: func(token *jwt.Token) (interface{}, error) {
		return signingKey, nil
	},
	SigningMethod: jwt.SigningMethodHS256,
})

func (s *server) AnnounceLeader(ctx context.Context, in *pbl.LeaderIP) (*pbl.EmptyReply, error) {
	ctrlAddress = in.Ip
	return &pbl.EmptyReply{}, nil
}

func startRPCServer() {
	lis, err := net.Listen("tcp", ctrlRPCPort)
	if err != nil {
		panic(err)
	}
	log.Println("Started gRPC server on port", ctrlRPCPort)

	s := grpc.NewServer()
	pbl.RegisterLeaderServiceServer(s, &server{})

	// Register reflection service on gRPC server.
	reflection.Register(s)
	if err := s.Serve(lis); err != nil {
		panic(err)
	}
}

// searchHandler lists all files matching a pattern
var searchHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	log.Println("in search")
	params := mux.Vars(r)

	// If there's no pattern, we're listing all files
	pattern, _ := params["pattern"]

	// Connect to the server
	conn, err := grpc.Dial(ctrlAddress+ctrlRPCPort, grpc.WithInsecure())
	if err != nil {
		renderError(w, "STORAGE_CONTROLLER_CONNECTION_ERROR", http.StatusInternalServerError)
		return
	}
	defer conn.Close()

	c := pb.NewPortalControllerClient(conn)

	stream, err := c.FileSearch(context.Background(), &pb.SearchRequest{Pattern: pattern})
	if err != nil {
		log.Println(err)
		renderError(w, "STORAGE_CONTROLLER_STREAM_ERROR", http.StatusInternalServerError)
		return
	}

	var matches []file

	// Read matches from the stream until EOF err
	for {
		match, err := stream.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Println(err)
			renderError(w, "FILE_DELETION_ERROR", http.StatusInternalServerError)
			return
		}

		matches = append(matches, file{Filename: match.Filename, SizeInBytes: match.FileSize})
	}

	json.NewEncoder(w).Encode(matches)
})

// downloadHandler downloads a file with a given filename
var downloadHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	token := r.Header.Get("Authorization")
	filename, err := url.PathUnescape(params["filename"])
	if err != nil {
		renderError(w, "INVALID_FILENAME", http.StatusBadRequest)
		return
	}

	conn, err := net.Dial("tcp", ctrlAddress+ctrlSocketPort)
	if err != nil {
		renderError(w, "STORAGE_CONTROLLER_CONNECTION_FAILED", http.StatusInternalServerError)
	}
	defer conn.Close()

	log.Printf("Request to download %s from %s", filename, token)

	// Notify that it's a file download
	io.WriteString(conn, "download\n")
	io.WriteString(conn, filename+"\n")

	io.Copy(w, conn)

})

// uploadHandler uploads a file to the servers
var uploadHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	// Verify that filesize is below max allowed
	r.Body = http.MaxBytesReader(w, r.Body, maxUploadSize)
	if err := r.ParseMultipartForm(maxUploadSize); err != nil {
		renderError(w, "FILESIZE_LIMIT_EXCEEDED", http.StatusBadRequest)
		return
	}

	token := r.Header.Get("Authorization")

	file, header, err := r.FormFile("file")
	if err != nil {
		renderError(w, "INVALID_FILE", http.StatusBadRequest)
		return
	}
	defer file.Close()

	fileSize := r.PostFormValue("filesize")
	fileHash := r.PostFormValue("hash")
	filename := header.Filename

	log.Printf("Upload requested for %s (%s Bytes) by token: %s with hash: %s\n", filename, fileSize, token, fileHash)

	log.Println(ctrlAddress + ctrlSocketPort)
	conn, err := net.Dial("tcp", ctrlAddress+ctrlSocketPort)
	if err != nil {
		renderError(w, "STORAGE_CONTROLLER_CONNECTION_FAILED", http.StatusInternalServerError)
	}
	defer conn.Close()

	// Notify that it's a file upload
	io.WriteString(conn, "upload\n")
	io.WriteString(conn, filename+"\n")
	io.WriteString(conn, fileHash+"\n")
	io.WriteString(conn, token+"\n")

	// Forward the file to StorageController
	io.Copy(conn, file)

	w.Write([]byte("File upload success."))
})

// deleteHandler deletes the file associated with the input
var deleteHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)

	conn, err := grpc.Dial(ctrlAddress+ctrlRPCPort, grpc.WithInsecure())
	if err != nil {
		renderError(w, "STORAGE_CONTROLLER_CONNECTION_ERROR", http.StatusInternalServerError)
		return
	}
	defer conn.Close()

	c := pb.NewPortalControllerClient(conn)

	_, err = c.DeleteFile(context.Background(), &pb.DeletionRequest{Filename: params["filename"]})
	if err != nil {
		log.Println(err)
		renderError(w, "FILE_DELETION_ERROR", http.StatusInternalServerError)
		return
	}

	w.Write([]byte(fmt.Sprintf("Deleted %s", params["filename"])))
})

// Generate token for hardcoded admin user
// Obviously not secure, can move this auth0 later
func authHandler(w http.ResponseWriter, r *http.Request) {
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"name":  "admin",
		"admin": true,
	})

	tokenString, _ := token.SignedString(signingKey)

	json.NewEncoder(w).Encode(jwtToken{Token: tokenString})
	return
}

func statusHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("API is up and running."))
}

func renderError(w http.ResponseWriter, message string, statusCode int) {
	w.WriteHeader(statusCode)
	w.Write([]byte(message))
}
