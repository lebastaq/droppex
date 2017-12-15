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
	//"github.com/freddygv/droppex/lib"
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
			err := listFiles()
			checkErr(err)

		case "search":
			err := searchFiles(line[1])
			checkErr(err)

		case "upload":
			err := uploadFile(line[1])
			checkErr(err)

		case "download":
			err := downloadFile(line[1])
			checkErr(err)

		case "delete":
			err := deleteFile(line[1])
			checkErr(err)

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
		log.Println("STDIN: ", err)
	}
}

func listFiles() error {
	if DEBUGGING {
		log.Println("Listing all files")
	}
	err := queryFiles("")

	return err
}

func searchFiles(pattern string) error {
	if DEBUGGING {
		log.Println("Searching for:", pattern)
	}
	err := queryFiles(pattern)

	return err
}

func queryFiles(pattern string) error {
	target := URL + "list"
	if pattern != "" {
		target = target + "/" + pattern
	}

	// Grabbing response from URL (raw JSON)
	resp, err := http.Get(target)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("Bad response: %s", resp.Status)
	}

	// Read body of HTTP response (JSON)
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return err
	}

	// Unmarshals JSON body into the payload struct by passing in the pointer
	payload := make(map[string]File)
	err = json.Unmarshal(body, &payload)
	if err != nil {
		return err
	}

	printJSON(payload)

	return nil
}

func downloadFile(filename string) error {
	target := URL + "files/" + filename

	resp, err := http.Get(target)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("Bad response: %s", resp.Status)
	}

	// Actual download

	if DEBUGGING {
		log.Println("Download request for file:", filename)
		debug(httputil.DumpResponse(resp, true))
	}

	return nil
}

func uploadFile(filepath string) error {
	target := URL + "files_post"

	values := map[string]string{"filepath": filepath}
	jsonValue, _ := json.Marshal(values)

	resp, err := http.Post(target, "application/json", bytes.NewBuffer(jsonValue))
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("Bad response: %s", resp.Status)
	}

	// Actual upload

	if DEBUGGING {
		log.Println("Upload request for file:", filepath)
		debug(httputil.DumpResponse(resp, true))
	}

	return nil
}

func deleteFile(filename string) error {
	target := URL + "delete/" + filename

	resp, err := http.Post(target, "", nil)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("Bad response: %s", resp.Status)
	}

	if DEBUGGING {
		log.Println("Deletion request for file:", filename)
		debug(httputil.DumpResponse(resp, true))
	}

	return nil
}

func printJSON(payload map[string]File) {
	i := 1
	for _, value := range payload {
		size := float64(value.SizeInBytes) / float64(1024)
		fmt.Printf("%d: %s (%d KB) \n", i, value.Filename, int(size))
		i++
	}

	if i == 1 {
		log.Println("Your search did not match any files.")
	}
}

func checkErr(err error) {
	if err != nil {
		fmt.Println("Connection to server failed, try again later.")
		log.Fatal(err)
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
	log.Println(`
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
