package main

import (
	"bufio"
	"bytes"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"mime/multipart"
	"net/http"
	"net/http/httputil"
	"net/url"
	"os"
	"strconv"
	"strings"
	"time"

	"github.com/freddygv/droppex/client/shared"
)

// URL for app-server
const URL string = "https://localhost:8000/api/1/"

// DEBUGGING mode shows HTTP req details
const DEBUGGING bool = true

var token jwtToken
var client *http.Client

type file struct {
	Filename    string `json:"filename,omitempty"`
	SizeInBytes int    `json:"size,omitempty"`
}

type jwtToken struct {
	Token string `json:"token"`
}

func main() {
	// Skipping because of self-signed cert
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	client = &http.Client{Transport: tr,
		Timeout: time.Second * 10}

	authenticate()
	showHeader()

	scanner := bufio.NewScanner(os.Stdin)
	fmt.Print("> ")
	for scanner.Scan() {
		line := scanner.Text()

		switch {
		case strings.HasPrefix(line, "exit"):
			os.Exit(0)

		case strings.HasPrefix(line, "list"):
			err := listFiles()
			checkErr(err)

		case strings.HasPrefix(line, "search"):
			err := searchFiles(strings.Split(line, "search ")[1])
			checkErr(err)

		case strings.HasPrefix(line, "upload"):
			err := uploadFile(strings.Split(line, "upload ")[1])
			checkErr(err)

		case strings.HasPrefix(line, "download"):
			err := downloadFile(strings.Split(line, "download ")[1])
			checkErr(err)

		case strings.HasPrefix(line, "delete"):
			err := deleteFile(strings.Split(line, "delete ")[1])
			checkErr(err)

		case strings.HasPrefix(line, "help"):
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
	var target string

	if pattern == "" {
		target = URL + "list"
	} else {
		target = URL + "search/" + pattern
	}

	req, err := http.NewRequest("GET", target, nil)
	if err != nil {
		return err
	}

	req.Header.Add("Authorization", "Bearer "+token.Token)
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("list - bad response: %s", resp.Status)
	}

	// Unmarshals JSON body into the payload struct by passing in the pointer
	payload := make([]file, 0)
	err = json.NewDecoder(resp.Body).Decode(&payload)
	if err != nil {
		return err
	}

	printFiles(payload)

	return nil
}

func downloadFile(filename string) error {
	target := URL + "files/" + url.PathEscape(filename)

	req, err := http.NewRequest("GET", target, nil)
	if err != nil {
		return err
	}

	req.Header.Add("Authorization", "Bearer "+token.Token)
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if DEBUGGING {
		log.Println("Download request for file:", filename)
	}

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("download - bad response: %s", resp.Status)
	}

	file, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer file.Close()

	if _, err = io.Copy(file, resp.Body); err != nil {
		return err
	}

	return nil
}

func uploadFile(filepath string) error {
	target := URL + "files_post"

	// Actual upload
	var Buf bytes.Buffer
	mpw := multipart.NewWriter(&Buf)

	file, err := os.Open(filepath)
	if err != nil {
		fmt.Println("Invalid filepath, please try again.")
		return nil
	}
	defer file.Close()

	fw, err := mpw.CreateFormFile("file", filepath)
	if err != nil {
		return err
	}
	if _, err = io.Copy(fw, file); err != nil {
		return err
	}

	fileStats, err := file.Stat()
	if err != nil {
		return err
	}

	if fw, err = mpw.CreateFormField("filesize"); err != nil {
		return err
	}
	if _, err = fw.Write([]byte(strconv.FormatInt(fileStats.Size(), 10))); err != nil {
		return err
	}

	if fw, err = mpw.CreateFormField("hash"); err != nil {
		return err
	}
	if _, err = fw.Write([]byte(shared.HashFile(filepath))); err != nil {
		return err
	}

	mpw.Close()

	req, err := http.NewRequest("POST", target, &Buf)
	if err != nil {
		return err
	}

	req.Header.Set("Content-Type", mpw.FormDataContentType())
	req.Header.Add("Authorization", "Bearer "+token.Token)

	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if DEBUGGING {
		log.Println("Upload request for file:", filepath)
		debug(httputil.DumpResponse(resp, true))
	}

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("upload - bad response: %s", resp.Status)
	}

	return nil
}

func deleteFile(filename string) error {
	target := URL + "delete/" + filename

	req, err := http.NewRequest("POST", target, nil)
	if err != nil {
		return err
	}

	req.Header.Add("Authorization", "Bearer "+token.Token)
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if DEBUGGING {
		log.Println("Deletion request for file:", filename)
		debug(httputil.DumpResponse(resp, true))
	}

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("delete - bad response: %s", resp.Status)
	}

	return nil
}

// Get jwt authentication token from server
func authenticate() error {
	target := URL + "auth"

	resp, err := client.Get(target)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	json.NewDecoder(resp.Body).Decode(&token)
	return nil
}

func printFiles(payload []file) {
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
