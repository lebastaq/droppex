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
	"strconv"
	"time"

	jwtmiddleware "github.com/auth0/go-jwt-middleware"
	jwt "github.com/dgrijalva/jwt-go"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
)

const (
	Version       string = "/api/1"
	maxUploadSize int64  = 500 * 1024 * 1024 // 500 MB
)

var signingKey []byte = []byte("fY6k6km3Pw1txgPa7Qc73AYqUNZj6Wg8")

func main() {
	router := mux.NewRouter()

	router.HandleFunc(Version+"/status", statusHandler).Methods("GET")
	router.HandleFunc(Version+"/auth", authHandler).Methods("GET")

	router.Handle(Version+"/list", jwtMiddleware.Handler(listHandler)).Methods("GET")
	router.Handle(Version+"/search/{pattern}", jwtMiddleware.Handler(searchHandler)).Methods("GET")
	router.Handle(Version+"/files/{filename}", jwtMiddleware.Handler(downloadHandler)).Methods("GET")
	router.Handle(Version+"/files_post", jwtMiddleware.Handler(uploadHandler)).Methods("POST")
	router.Handle(Version+"/delete/{filename}", jwtMiddleware.Handler(deleteHandler)).Methods("POST")

	log.Fatal(http.ListenAndServeTLS(":8000", "domain.crt", "domain.key", handlers.LoggingHandler(os.Stdout, router)))
}

// File struct holds file name, size, and upload date
type File struct {
	Filename    string    `json:"filename,omitempty"`
	SizeInBytes int       `json:"size,omitempty"`
	Hash        string    `json:"hash,omitempty"`
	Created     time.Time `json:"time,omitempty"`
}

type JwtToken struct {
	Token string `json:"token"`
}

var jwtMiddleware = jwtmiddleware.New(jwtmiddleware.Options{
	ValidationKeyGetter: func(token *jwt.Token) (interface{}, error) {
		return signingKey, nil
	},
	SigningMethod: jwt.SigningMethodHS256,
})

// listHandler lists all files or files matching a pattern
var listHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	// token := r.Header.Get("Authorization")

	// // Send message to Storage Controller asking for files

	// json.NewEncoder(w).Encode(files)
})

// searchHandler lists all files matching a pattern
var searchHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	// token := r.Header.Get("Authorization")
	// params := mux.Vars(r)

	// // Send message to Storage Controller asking for files that match a pattern

	// json.NewEncoder(w).Encode(matches)
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

	conn, err := net.Dial("tcp", "localhost:29200")
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

	filename := r.PostFormValue("filename")
	fileSize := r.PostFormValue("filesize")
	fileHash := r.PostFormValue("hash")
	log.Printf("Upload requested for %s (%s Bytes) by token: %s with hash: %s\n", filename, fileSize, token, fileHash)

	file, _, err := r.FormFile("file")
	if err != nil {
		renderError(w, "INVALID_FILE", http.StatusBadRequest)
		return
	}
	defer file.Close()

	conn, err := net.Dial("tcp", "localhost:29200")
	if err != nil {
		renderError(w, "STORAGE_CONTROLLER_CONNECTION_FAILED", http.StatusInternalServerError)
	}
	defer conn.Close()

	// Notify that it's a file upload
	io.WriteString(conn, "upload\n")
	io.WriteString(conn, filename+"\n")

	// Save the file
	tempName := strconv.Itoa(int(time.Now().Nanosecond()))
	infile, err := os.Create(tempName)
	if err != nil {
		renderError(w, "IO_ERROR", http.StatusInternalServerError)
	}
	os.Remove(tempName)
	infile.Close()

	// Forward the file to StorageController
	io.Copy(conn, file)

	w.Write([]byte("File upload success."))
})

// deleteHandler deletes the file associated with the input
var deleteHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	w.Write([]byte(fmt.Sprintf("Deleting %s", params["filename"])))
})

// Generate token for hardcoded admin user
// Obviously not secure, can move this auth0 later
func authHandler(w http.ResponseWriter, r *http.Request) {
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"name":  "admin",
		"admin": true,
	})

	tokenString, _ := token.SignedString(signingKey)

	json.NewEncoder(w).Encode(JwtToken{Token: tokenString})
	return
}

func statusHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("API is up and running."))
}

func renderError(w http.ResponseWriter, message string, statusCode int) {
	w.WriteHeader(statusCode)
	w.Write([]byte(message))
}
