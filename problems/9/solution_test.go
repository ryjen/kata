package main

import "testing"

func TestSolution(t *testing.T) {
	input := []int{2, 4, 6, 8}

	expected := 12

	actual := Solution(input)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	input = []int{5, 1, 1, 5}

	expected = 10

	actual = Solution(input)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	input = []int{5, 4, 3, 2, 1}

	expected = 8

	actual = Solution(input)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	input = []int{-2, 5, 2, 6, 6}

	expected = 11

	actual = Solution(input)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}
}
