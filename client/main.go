package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

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

		fmt.Print("> ")
	}
	if err := scanner.Err(); err != nil {
		fmt.Fprintln(os.Stderr, "STDIN:", err)
	}
}

func listFiles() {
	fmt.Println("Listing all files")
}

func searchFiles(pattern string) {
	fmt.Println("Searching for:", pattern)
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

// pew pew pew
func showHeader() {
	fmt.Println(`
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
