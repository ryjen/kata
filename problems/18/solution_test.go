package main

import "testing"

func intArrayEquals(a []int, b []int) bool {
	for i := range a {
		if a[i] != a[i] {
			return false
		}
	}
	return true
}

func TestSolution(t *testing.T) {

	input := []int{10, 5, 2, 7, 8, 7}

	expected := []int{10, 7, 8, 8}

	actual := Solution(input, 3)

	if !intArrayEquals(expected, actual) {
		t.Fatal("Expected ", expected, ", got ", actual)
	}
}
