package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strings"

	"github.com/gorilla/mux"
)

var files []File

func main() {
	const Version = "/1"
	router := mux.NewRouter()

	// Dummy data
	files = append(files, File{Filename: "homework1.pdf", SizeInBytes: 10})
	files = append(files, File{Filename: "photo1.jpeg", SizeInBytes: 5})

	router.HandleFunc(Version+"/list", ListFiles).Methods("GET")
	router.HandleFunc(Version+"/list/{pattern}", ListMatching).Methods("GET")
	router.HandleFunc(Version+"/files_put", UploadFile).Methods("PUT")
	router.HandleFunc(Version+"/files", DownloadFile).Methods("GET")
	router.HandleFunc(Version+"/delete", DeleteFile).Methods("DELETE")

	log.Fatal(http.ListenAndServe(":8000", router))
}

// File struct holds file name and size
type File struct {
	Filename    string `json:"filename,omitempty"`
	SizeInBytes int    `json:"size,omitempty"`
}

// ListFiles lists all files or files matching a pattern
// curl test: curl localhost:8080/1/list
func ListFiles(w http.ResponseWriter, r *http.Request) {
	json.NewEncoder(w).Encode(files)
}

// ListMatching lists all files matching a pattern
// curl test: curl localhost:8080/1/list/photo
func ListMatching(w http.ResponseWriter, r *http.Request) {
	output := make([]File, 0)
	params := mux.Vars(r)

	for _, item := range files {
		if strings.Contains(item.Filename, params["pattern"]) {
			output = append(output, item)
		}
	}

	if len(output) > 0 {
		json.NewEncoder(w).Encode(output)
		return
	}

	json.NewEncoder(w).Encode(&File{})
}

// UploadFile uploads a file to the servers
// Temporarily just uploading metadata
// curl test: curl -d '{"filename":"blergh.jpeg","size":5}' -X PUT localhost:8080/1/files_put
func UploadFile(w http.ResponseWriter, r *http.Request) {
	var file File
	json.NewDecoder(r.Body).Decode(&file)

	if file.Filename != "" {
		files = append(files, file)

	}

	json.NewEncoder(w).Encode(files)
}

// DownloadFile downloads a file with a given filename
func DownloadFile(w http.ResponseWriter, r *http.Request) {

}

// DeleteFile deletes the file associated with the input
func DeleteFile(w http.ResponseWriter, r *http.Request) {

}
