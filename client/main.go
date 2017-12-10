package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"strings"
)

// URL for app-server
const URL string = "http://localhost:8000/1/"

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
	fmt.Println("Listing all files")
	queryFiles("")

}

func searchFiles(pattern string) {
	fmt.Println("Searching for:", pattern)
	queryFiles(pattern)
}

func queryFiles(pattern string) {
	target := URL + "/list"
	if pattern != "" {
		target = target + "/" + pattern
	}

	// Grabbing response from URL (raw JSON)
	res, err := http.Get(target)
	if err != nil {
		// TODO: Handle
		panic(err)
	}
	defer res.Body.Close()

	// Read body of HTTP response (JSON)
	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	// Unmarshals JSON body into the payload struct by passing in the pointer
	payload := make([]File, 0)
	err = json.Unmarshal(body, &payload)
	if err != nil {
		// TODO: Handle
		panic(err)
	}

	printJSON(payload)

}

func uploadFile(filepath string) {
	fmt.Println("Upload request for file:", filepath)
}

func downloadFile(filename string) {
	fmt.Println("Download request for file:", filename)
}

func deleteFile(filename string) {
	fmt.Println("Deletion request for file:", filename)
}

func printJSON(payload []File) {
	for i, item := range payload {
		size := float64(item.SizeInBytes) / float64(1024)
		fmt.Printf("%d: %s (%f KB) \n", i+1, item.Filename, size)
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
