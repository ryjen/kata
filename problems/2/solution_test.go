package main

import (
	"math/rand"
	"testing"
)

// compares two uint arrays
func compareIntArray(a1 []int, a2 []int) bool {

	if len(a1) != len(a2) {
		return false
	}

	for i, v := range a1 {
		if v != a2[i] {
			return false
		}
	}
	return true
}

func TestFastDiv(t *testing.T) {

	expected := 5

	actual, err := fastDiv(25, 5)

	if err != nil {
		t.Fatal(err.Error())
	}

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	expected = -5

	actual, err = fastDiv(-25, 5)

	if err != nil {
		t.Fatal(err.Error())
	}

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	expected = 5

	actual, err = fastDiv(-25, -5)

	if err != nil {
		t.Fatal(err.Error())
	}

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}
}

func TestSolution(t *testing.T) {
	input := []int{1, 2, 3, 4, 5}

	expected := []int{120, 60, 40, 30, 24}

	actual, err := Solution(input)

	if err != nil {
		t.Fatal(err.Error())
	}

	if !compareIntArray(expected, actual) {
		t.Fatal("expected ", expected, " got ", actual)
	}
}

func TestSolutionNegative(t *testing.T) {
	input := []int{1, 2, -3, 4, 5}

	expected := []int{-120, -60, 40, -30, -24}

	actual, err := Solution(input)

	if err != nil {
		t.Fatal(err.Error())
	}

	if !compareIntArray(expected, actual) {
		t.Fatal("expected ", expected, " got ", actual)
	}
}

func generateBenchmarkInput() []int {
	const size = 10000

	input := make([]int, size)

	for i := 0; i < size; i++ {
		input[i] = int(rand.Uint32())
	}

	return input
}

func BenchmarkSolution(b *testing.B) {

	input := generateBenchmarkInput()

	_, _ = Solution(input)

}

func BenchmarkBruteForce(b *testing.B) {

	input := generateBenchmarkInput()

	_ = SolutionBruteForce(input)
}
