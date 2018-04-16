package main

import "testing"

func TestSolutionList(t *testing.T) {
	input := "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir2\n\tsubdir2\n\t\tsubsubdir1\n\t\t\tfile2.ext"

	expected := 32

	actual := SolutionList(input)

	if expected != actual {
		t.Fatal("Got ", actual, " expected ", expected)
	}
}
