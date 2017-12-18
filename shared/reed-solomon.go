package shared

import (
	"fmt"
	"io/ioutil"
	"os"
	"path/filepath"
	"strings"

	"github.com/klauspost/reedsolomon"
)

// Decode combines data from input shards
func Decode(filename string, dataShards int, parityShards int) error {
	dec, err := reedsolomon.New(dataShards, parityShards)
	if err != nil {
		return fmt.Errorf("Error: creating decoder for %s", filename)
	}

	dir := stripExtension(filename)
	shards := make([][]byte, dataShards+parityShards)

	invalid := 0
	for i := range shards {
		current := fmt.Sprintf("%s/%s.%d", dir, filename, i)
		shards[i], err = ioutil.ReadFile(current)
		if err != nil {
			shards[i] = nil
			invalid++
			fmt.Println(err)
		}
	}

	if invalid > parityShards {
		return fmt.Errorf("Error: more invalid shards than parity shards")
	}

	ok, err := dec.Verify(shards)
	if err != nil {
		return fmt.Errorf("Error: shard verification failed")
	}
	if !ok {
		if err = dec.Reconstruct(shards); err != nil {
			return fmt.Errorf("Error: could not reconstruct data from parity")
		}

		ok, err = dec.Verify(shards)
		if err != nil {
			return fmt.Errorf("Error: shard verification failed")
		}
		if !ok {
			return fmt.Errorf("Error: data remains corrupted after reconstruction")
		}

	}

	f, err := os.Create("out-" + filename)
	if err != nil {
		return fmt.Errorf("Error: creating creating output file")
	}

	if err = dec.Join(f, shards, len(shards[0])*dataShards); err != nil {
		return fmt.Errorf("Error: joining shards into output file")
	}

	err = os.RemoveAll(dir)
	if err != nil {
		fmt.Println(err)
		return fmt.Errorf("Error: cleaning up temp directory '%s'", dir)
	}

	return nil
}

// Encode splits data into shards and encodes a specific number of parity shards
func Encode(localpath string, dataShards int, parityShards int) (string, error) {
	enc, err := reedsolomon.New(dataShards, parityShards)
	if err != nil {
		return "", fmt.Errorf("Error: creading EC encoder for %s", localpath)
	}

	b, err := ioutil.ReadFile(localpath)
	if err != nil {
		return "", fmt.Errorf("Error: reading %s from file", localpath)
	}

	shards, err := enc.Split(b)
	if err != nil {
		return "", fmt.Errorf("Error: splitting buffer into desired shardcount: %d", dataShards)
	}

	err = enc.Encode(shards)
	if err != nil {
		return "", fmt.Errorf("Error: encoding %d parity shards", parityShards)
	}

	dir := stripExtension(localpath)
	err = os.Mkdir(dir, os.FileMode(0755))
	if err != nil {
		return "", fmt.Errorf("Error: creating temp directory '%s'", dir)
	}

	for i, shard := range shards {
		outfn := fmt.Sprintf("%s.%d", localpath, i)
		target := filepath.Join(dir, outfn)

		err = ioutil.WriteFile(target, shard, os.ModePerm)
		if err != nil {
			return "", fmt.Errorf("Error: writing shards to %s", target)
		}
	}

	return dir, nil
}

func stripExtension(filepath string) string {
	if lastPeriod := strings.LastIndex(filepath, "."); lastPeriod > 0 {
		runes := []rune(filepath)
		return string(runes[0:lastPeriod])
	}
	return filepath
}
