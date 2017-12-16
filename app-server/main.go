package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strings"
	"time"

	//"github.com/freddygv/droppex/app-server/utils"
	//"github.com/freddygv/droppex/lib"
	"github.com/boltdb/bolt"
	"github.com/gorilla/mux"
)

const Version = "api/1"
const maxUploadSize = 500 * 1024 * 1024 // 500 MB

var db bolt.DB

func main() {
	// key-value store for files
	bolt, err := bolt.Open("files.db", 0600, nil)
	if err != nil {
		log.Fatal(err)
	}
	defer bolt.Close()
	db = *bolt

	router := mux.NewRouter()

	router.HandleFunc(Version+"/list", listAll).Methods("GET")
	router.HandleFunc(Version+"/list/{pattern}", listMatching).Methods("GET")
	router.HandleFunc(Version+"/files/{filename}", downloadFile).Methods("GET")
	router.HandleFunc(Version+"/files_post", uploadFile).Methods("POST")
	router.HandleFunc(Version+"/delete/{filename}", deleteFile).Methods("POST")

	dummyData("123")
	files, err := searchDB("123", "")
	if err != nil {
		panic(err)
	}
	fmt.Println(files)

	// log.Fatal(http.ListenAndServe(":8000", router))
}

// File struct holds file name, size, and upload date
type File struct {
	Filename    string `json:"filename,omitempty"`
	SizeInBytes int    `json:"size,omitempty"`
	Created     time.Time
}

// listAll lists all files or files matching a pattern
func listAll(w http.ResponseWriter, r *http.Request) {
	// json.NewEncoder(w).Encode(files)
}

// listMatching lists all files matching a pattern
func listMatching(w http.ResponseWriter, r *http.Request) {
	// params := mux.Vars(r)
	// pattern := params["pattern"]

	// json.NewEncoder(w).Encode(output)
}

// downloadFile downloads a file with a given filename
func downloadFile(w http.ResponseWriter, r *http.Request) {
	// params := mux.Vars(r)
	// filename := params["filename"]

}

// uploadFile uploads a file to the servers
// TODO: Verify file doesn't already exist
func uploadFile(w http.ResponseWriter, r *http.Request) {
	// Verify that filesize is below max allowed
	r.Body = http.MaxBytesReader(w, r.Body, maxUploadSize)
	if err := r.ParseMultipartForm(maxUploadSize); err != nil {
		renderError(w, "FILESIZE_LIMIT_EXCEEDED", http.StatusBadRequest)
		return
	}

	var Buf bytes.Buffer

	// fileType := r.PostFormValue("type")
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
}

// deleteFile deletes the file associated with the input
func deleteFile(w http.ResponseWriter, r *http.Request) {
	// params := mux.Vars(r)
	// filename := params["filename"]
}

func renderError(w http.ResponseWriter, message string, statusCode int) {
	w.WriteHeader(statusCode)
	w.Write([]byte(message))
}

func searchDB(token string, pattern string) ([]File, error) {
	var files []File

	// Create a bucket in case the first API call is a search
	if err := db.Update(func(tx *bolt.Tx) error {
		_, err := tx.CreateBucketIfNotExists([]byte(token))
		return err
	}); err != nil {
		return files, err
	}

	err := db.View(func(tx *bolt.Tx) error {
		var file File
		b := tx.Bucket([]byte(token))
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

func updateDB(token string, f File) error {
	err := db.Update(func(tx *bolt.Tx) error {
		b, err := tx.CreateBucketIfNotExists([]byte(token))
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

// TODO: Remove after testing
func dummyData(token string) error {
	files := []File{
		File{"homework1.pdf", 1024 * 1024, time.Now()},
		File{"homework2.pdf", 2 * 1024 * 1024, time.Now()}}

	for _, f := range files {
		if err := updateDB(token, f); err != nil {
			panic(err)
		}

	}
	return nil
}
