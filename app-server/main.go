package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strings"

	//"github.com/freddygv/droppex/app-server/utils"
	//"github.com/freddygv/droppex/lib"
	"github.com/gorilla/mux"
)

const Home = "api/1/"
var files = make(map[string]File)

func main() {
	router := mux.NewRouter()

	// TODO: Remove this dummy data
	files["homework1.pdf"] = File{Filename: "homework1.pdf", SizeInBytes: 2048}
	files["photo1.jpeg"] = File{Filename: "photo1.jpeg", SizeInBytes: 1024}

    router.PathPrefix(Home + "files/").Handler(
        http.StripPrefix(Home + "files/", http.FileServer(http.Dir(Home + "files/")))
    )

	router.HandleFunc(Home + "list", ListFiles).Methods("GET")
	router.HandleFunc(Home + "list/{pattern}", ListMatching).Methods("GET")
	router.HandleFunc(Home + "files_post", UploadFile).Methods("POST")
	router.HandleFunc(Home + "delete/{filename}", DeleteFile).Methods("POST")

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
	w.WriteHeader(200)
	json.NewEncoder(w).Encode(files)
}

// ListMatching lists all files matching a pattern
// curl test: curl localhost:8080/1/list/photo
func ListMatching(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(200)
	params := mux.Vars(r)
	output := make(map[string]File)

	for key, value := range files {
		if strings.Contains(key, params["pattern"]) {
			output[key] = value
		}
	}

	json.NewEncoder(w).Encode(output)
}

// UploadFile uploads a file to the servers
// TODO: Verify file doesn't already exist
func UploadFile(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(200)

	var Buf bytes.Buffer
	file, header, err := r.FormFile("file")
	if err != nil {
        renderError(w, "INVALID_FILE", http.StatusBadRequest)
		return
	}
	defer file.Close()

	name := strings.Split(header.Filename, ".")
	fmt.Prinf("File uploaded: %s \n", name[0])

	io.Copy(&Buf, file)
	// Make RPC call to controller for target pools

	// Send blocks over RPC to storage pools (pass in buffer)

	// Cleanup buffer
	Buf.Reset()

	// Only reply success if the RPC to storage pool is successful
	w.Write([]byte("File upload success."))
}

// DeleteFile deletes the file associated with the input
func DeleteFile(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(200)
	params := mux.Vars(r)

	if _, ok := files[params["filename"]]; ok {
		w.Write([]byte("Found."))
	}
	w.Write([]byte("File not found."))
}

func renderError(w http.ResponseWriter, message string, statusCode int) {
	w.WriteHeader(statusCode)
	w.Write([]byte(message))
}
