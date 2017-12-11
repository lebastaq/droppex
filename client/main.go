package main

import (
	"bufio"
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"net/http/httputil"
	"os"
	"strings"
)

// URL for app-server
const URL string = "http://localhost:8000/1/"

// DEBUGGING mode shows HTTP req details
const DEBUGGING bool = true

// File struct holds file name and size
type File struct {
	Filename    string `json:"filename,omitempty"`
	SizeInBytes int    `json:"size,omitempty"`
}

func main() {
	showHeader()

	scanner := bufio.NewScanner(os.Stdin)
	fmt.Print("> ")
	for scanner.Scan() {
		line := strings.Split(scanner.Text(), " ")

		switch line[0] {
		case "exit":
			os.Exit(0)

		case "list":
			listFiles()

		case "search":
			searchFiles(line[1])

		case "upload":
			uploadFile(line[1])

		case "download":
			downloadFile(line[1])

		case "delete":
			deleteFile(line[1])

		case "help":
			showHelp()

		default:
			fmt.Printf("droppex: %s is not a valid command.\n", line)
			fmt.Println("see 'help'")
		}

		fmt.Println()
		fmt.Print("> ")
	}
	if err := scanner.Err(); err != nil {
		fmt.Fprintln(os.Stderr, "STDIN:", err)
	}
}

func listFiles() {
	if DEBUGGING {
		fmt.Println("Listing all files")
	}
	queryFiles("")

}

func searchFiles(pattern string) {
	if DEBUGGING {
		fmt.Println("Searching for:", pattern)
	}
	queryFiles(pattern)
}

func queryFiles(pattern string) {
	target := URL + "list"
	if pattern != "" {
		target = target + "/" + pattern
	}

	// Grabbing response from URL (raw JSON)
	resp, err := http.Get(target)
	defer resp.Body.Close()
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	// Read body of HTTP response (JSON)
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	// Unmarshals JSON body into the payload struct by passing in the pointer
	payload := make(map[string]File)
	err = json.Unmarshal(body, &payload)
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	printJSON(payload)
}

func downloadFile(filename string) {
	target := URL + "files/" + filename

	resp, err := http.Get(target)
	defer resp.Body.Close()
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	if DEBUGGING {
		fmt.Println("Download request for file:", filename)
		debug(httputil.DumpResponse(resp, true))
	}
}

func uploadFile(filepath string) {
	target := URL + "files_post"

	values := map[string]string{"filepath": filepath}
	jsonValue, _ := json.Marshal(values)

	resp, err := http.Post(target, "application/json", bytes.NewBuffer(jsonValue))
	defer resp.Body.Close()
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	// TODO: Put into use?
	// if 200 != resp.StatusCode {
	// 	return nil, fmt.Errorf("%s", body)
	// }

	if DEBUGGING {
		fmt.Println("Upload request for file:", filepath)
		debug(httputil.DumpResponse(resp, true))
	}
}

func deleteFile(filename string) {
	target := URL + "delete/" + filename

	resp, err := http.Post(target, "", nil)
	defer resp.Body.Close()
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	if DEBUGGING {
		fmt.Println("Deletion request for file:", filename)
		debug(httputil.DumpResponse(resp, true))
	}
}

func printJSON(payload map[string]File) {
	i := 1
	for _, value := range payload {
		size := float64(value.SizeInBytes) / float64(1024)
		fmt.Printf("%d: %s (%d KB) \n", i, value.Filename, int(size))
		i++
	}

	if i == 1 {
		fmt.Println("Your search did not match any files.")
	}
}

// TODO: Remove
func debug(data []byte, err error) {
	if err != nil {
		log.Fatalf("%s", err)
	} else {
		fmt.Printf("%s", data)
	}
}

// pew pew pew
func showHeader() {
	fmt.Print(`
 ___
|   \ _ _ ___ _ __ _ __  ___ _ __
| |) | '_/ _ \ '_ \ '_ \/ -_) \ /
|___/|_| \___/ .__/ .__/\___/_\_\
             |_|  |_|
==================================
    `)

	showHelp()
}

func showHelp() {
	fmt.Println(`
Commands Available:
    list                    Lists all files on the server.
    search {query}          Searches for files that match the query.
    upload {path}           Uploads file from a given filepath.
    download {filename}     Downloads file.
    delete {filename}       Deletes file.
    help                    Prints this help info.
    exit                    Exits the program.
    `)
}
