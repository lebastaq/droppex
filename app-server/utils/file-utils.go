package utils

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"os"
	"strconv"
	"strings"
	"sync"
)

// const chunkSizeBytes int = 1024 * 1024 * 10
const chunkSizeBytes int = 1024 * 1024

var wg sync.WaitGroup

// ChunkFile splits a file into
func ChunkFile(filepath string) error {
	tmpDir := createTempDir(filepath)
	file, err := os.Open(filepath)
	if err != nil {
		return err
	}

	for i := 0; ; i++ {
		buffer := make([]byte, chunkSizeBytes)

		n, err := file.Read(buffer)
		if err == nil {
			wg.Add(1)
			go prepareChunkUpload(tmpDir, i, buffer[:n])

		} else if err == io.EOF {
			break
		} else {
			return err
		}
	}
	wg.Wait()
	os.Remove(tmpDir)

	return nil
}

// Create temporary dir for chunks
func createTempDir(filepath string) string {
	// Remove file extension
	if lastPeriod := strings.LastIndex(filepath, "."); lastPeriod > 0 {
		runes := []rune(filepath)
		filepath = string(runes[0:lastPeriod])
	}

	os.Mkdir(filepath, os.FileMode(0755))

	return filepath
}

func prepareChunkUpload(dirPath string, index int, buffer []byte) error {
	defer wg.Done()
	// Filename format is index-randomnumber
	tmpfile, err := ioutil.TempFile(dirPath, strconv.Itoa(index)+"-")
	if err != nil {
		return err
	}
	defer func() {
		tmpfile.Close()
		os.Remove(tmpfile.Name())
	}()

	// Copy data to file
	if _, err = tmpfile.Write(buffer); err != nil {
		return err
	}

	// Upload file to storage pool
	err = uploadChunk(tmpfile)
	if err != nil {
		return err
	}

	return nil
}

func uploadChunk(f *os.File) error {
	fmt.Println(f.Name())
	return nil
}

// AssembleFile re-assembles chunks into a whole file
func AssembleFile(path string) error {
	chunks, err := ioutil.ReadDir(path)
	if err != nil {
		return err
	}

	for _, file := range chunks {
		fmt.Println(file)
	}

	return nil
}

// HashFile Returns SHA-256 checksum of the file as a string
func HashFile(file string) string {
	f, err := os.Open(file)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()

	h := sha256.New()
	if _, err := io.Copy(h, f); err != nil {
		log.Fatal(err)
	}

	checkSum := h.Sum(nil)

	return hex.EncodeToString(checkSum)
}
