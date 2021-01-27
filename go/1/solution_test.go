package main

import "testing"

func TestSolution(t *testing.T) {

	input := 1041
	expected := 5

	actual := Solution(input)

	if actual != expected {
		t.Fatal("expected", expected, "actual", actual)
	}
}
