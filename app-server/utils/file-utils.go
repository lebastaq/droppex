package utils

import (
	"bufio"
	"fmt"
	"io"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
	"sync"
)

// const chunkSizeBytes int = 1024 * 1024 * 10
const chunkSizeBytes int = 1024 * 1024

// ChunkFile splits a file into
func ChunkFile(filepath string) error {
	tmpDir := createTempDir(filepath)
	file, err := os.Open(filepath)
	if err != nil {
		return err
	}

	var wg sync.WaitGroup
	for i := 0; ; i++ {
		buffer := make([]byte, chunkSizeBytes)

		n, err := file.Read(buffer)
		if err == nil {
			wg.Add(1)
			go prepareChunkUpload(tmpDir, i, buffer[:n], &wg)

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
	// Reuse the filename but remove extension first
	if lastPeriod := strings.LastIndex(filepath, "."); lastPeriod > 0 {
		runes := []rune(filepath)
		filepath = string(runes[0:lastPeriod])
	}

	os.Mkdir(filepath, os.FileMode(0755))

	return filepath
}

func prepareChunkUpload(dirPath string, index int, buffer []byte, wg *sync.WaitGroup) error {
	defer wg.Done()
	// Filename format is "index-randomnumber"
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
func AssembleFile(directory string, ext string) error {
	outfile, err := os.Create(directory + ext)
	if err != nil {
		return err
	}
	defer outfile.Close()

	chunks, err := ioutil.ReadDir(directory)
	if err != nil {
		return err
	}

	w := bufio.NewWriter(outfile)
	for _, chunk := range chunks {
		inbytes, err := ioutil.ReadFile(directory + "/" + chunk.Name())
		if err != nil {
			return err
		}

		_, err = w.Write(inbytes)
		if err != nil {
			return err
		}
	}

	w.Flush()

	return nil
}
