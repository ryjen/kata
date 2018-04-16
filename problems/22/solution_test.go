package main

import "testing"

func compareStringArray(a []string, b []string) bool {
	for _, s := range a {
		for _, t := range b {
			if s != t {
				return false
			}
		}
	}
	return true
}

func TestSolution(t *testing.T) {

	dictionary := []string{
		"bed", "bath", "bedbath", "and", "beyond",
	}

	input := "bedbathandbeyond"

	expected := []string{
		"bed", "bath", "and", "beyond",
	}

	result := Solution(dictionary, input)

	if compareStringArray(result, expected) {
		t.Fatal("got ", result, " expected ", expected)
	}
}
