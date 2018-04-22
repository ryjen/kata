package main

import (
	"fmt"
	"testing"
)

func stringArrayEquals(a []string, b []string) bool {
	if a == nil || b == nil {
		return false
	}

	if len(a) != len(b) {
		return false
	}

	for i, val := range a {
		if val != b[i] {
			return false
		}
	}
	return true
}

func TestSolution(t *testing.T) {

	words := []string{"the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"}
	k := 16

	expected := []string{"the  quick brown", "fox  jumps  over", "the   lazy   dog"}

	actual := Solution(k, words)

	if !stringArrayEquals(expected, actual) {
		t.Fatal(fmt.Sprintf("expected %#v got %#v", expected, actual))
	}
}
