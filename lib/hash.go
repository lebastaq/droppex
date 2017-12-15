package hash

import (
    "crypto/sha256"
    "encoding/hex"
    "io"
    "log"
    "os"
)

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
