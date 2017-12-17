package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"strings"
	"time"

	//"github.com/freddygv/droppex/app-server/utils"
	//"github.com/freddygv/droppex/lib"

	jwtmiddleware "github.com/auth0/go-jwt-middleware"
	"github.com/boltdb/bolt"
	jwt "github.com/dgrijalva/jwt-go"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
)

const (
	Version       string = "/api/1"
	maxUploadSize int64  = 500 * 1024 * 1024 // 500 MB
)

var signingKey []byte = []byte("fY6k6km3Pw1txgPa7Qc73AYqUNZj6Wg8")
var db *bolt.DB

func main() {
	// key-value store for files
	db, _ = bolt.Open("files.db", 0600, nil)
	defer db.Close()

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
	token := r.Header.Get("Authorization")

	// TODO: Remove
	dummyData(token)

	files, err := searchDB(token, "")
	if err != nil {
		renderError(w, "ERROR_LISTING_FILES", http.StatusInternalServerError)
		return
	}
	json.NewEncoder(w).Encode(files)
})

// searchHandler lists all files matching a pattern
var searchHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	token := r.Header.Get("Authorization")
	params := mux.Vars(r)

	matches, err := searchDB(token, params["pattern"])
	if err != nil {
		renderError(w, "ERROR_LISTING_FILES", http.StatusInternalServerError)
		return
	}
	json.NewEncoder(w).Encode(matches)
})

// downloadHandler downloads a file with a given filename
var downloadHandler = http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	w.Write([]byte(fmt.Sprintf("Downloading %s", params["filename"])))
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

	// TODO: Get real filename
	filename := "homework1.pdf"

	// Verify file doesn't already exist
	if err := db.View(func(tx *bolt.Tx) error {
		b := tx.Bucket([]byte(token))
		v := b.Get([]byte(filename))
		if v != nil {
			return fmt.Errorf("FILE_ALREADY_EXISTS")
		}
		return nil
	}); err != nil {
		renderError(w, err.Error(), http.StatusBadRequest)
		return
	}

	var Buf bytes.Buffer

	// TODO: fileType := r.PostFormValue("type")
	file, header, err := r.FormFile("file")
	if err != nil {
		renderError(w, "INVALID_FILE", http.StatusBadRequest)
		return
	}
	defer file.Close()

	name := strings.Split(header.Filename, ".")
	fmt.Printf("File uploaded: %s \n", name[0])

	// Read into buffer
	io.Copy(&Buf, file)

	// Make RPC call to controller for target pools

	// Send blocks over RPC to storage pools (pass in buffer)

	// Store name and size in files

	// Cleanup buffer
	Buf.Reset()

	// Only reply success if the RPC to storage pool is successful
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

func searchDB(bucket string, pattern string) ([]File, error) {
	var files []File

	// Create a bucket in case the first API call for this user was a search
	if err := db.Update(func(tx *bolt.Tx) error {
		if _, err := tx.CreateBucketIfNotExists([]byte(bucket)); err != nil {
			return err
		}
		return nil

	}); err != nil {
		return files, err
	}

	// Loop over all entries in the DB to match a string or list all
	err := db.View(func(tx *bolt.Tx) error {
		var file File
		b := tx.Bucket([]byte(bucket))
		b.ForEach(func(k, v []byte) error {
			json.Unmarshal(v, &file)
			// We only skip if there is a pattern and the pattern doesn't match the name
			if !(pattern != "" && !strings.Contains(file.Filename, pattern)) {
				files = append(files, file)
			}
			return nil
		})
		return nil
	})

	return files, err
}

func updateDB(bucket string, f File) error {
	err := db.Update(func(tx *bolt.Tx) error {
		b, err := tx.CreateBucketIfNotExists([]byte(bucket))
		if err != nil {
			return err
		}

		encoded, err := json.Marshal(f)
		if err != nil {
			return err
		}
		b.Put([]byte(f.Filename), []byte(encoded))
		return nil
	})

	return err
}

func renderError(w http.ResponseWriter, message string, statusCode int) {
	w.WriteHeader(statusCode)
	w.Write([]byte(message))
}

// TODO: Remove after testing
func dummyData(bucket string) error {
	files := []File{
		File{"homework1.pdf", 1024 * 1024, time.Now()},
		File{"homework2.pdf", 2 * 1024 * 1024, time.Now()}}

	for _, f := range files {
		if err := updateDB(bucket, f); err != nil {
			panic(err)
		}

	}
	return nil
}
