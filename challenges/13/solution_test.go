package main

import (
	"testing"
)

func TestBruteForce(t *testing.T) {
	input := "abcba"

	k := 2

	expected := 3

	actual := SolutionBruteForce(input, k)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}
}

func TestSolution(t *testing.T) {
	input := "abcba"

	k := 2

	expected := 3

	actual := Solution(input, k, false)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}
}

const letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYZ"

func generateBenchmarkInput() (string, int) {
	numLetters := len(letters)

	size := numLetters * 10000000

	values := make([]byte, size)

	k := 50

	mod := size / k

	for i := 0; i < size; i++ {
		if i%mod == 0 {
			values[i] = 'X'
		} else {
			values[i] = letters[i%numLetters]
		}
	}

	return string(values), k
}

func BenchmarkBruteForce(b *testing.B) {

	input, k := generateBenchmarkInput()

	_ = SolutionBruteForce(input, k)
}

func BenchmarkSolution(b *testing.B) {

	input, k := generateBenchmarkInput()

	_ = Solution(input, k, false)
}
