package main

import "testing"

func TestSolution(t *testing.T) {

	input := "ray"

	regex := "ra."

	expected := true

	actual, err := Solution(input, regex)

	if err != nil {
		t.Fatal(err.Error())
	}

	if actual != expected {
		t.Fatal("expected ", expected, " got ", actual)
	}

	input = "raymond"

	expected = false

	actual, err = Solution(input, regex)

	if err != nil {
		t.Fatal(err.Error())
	}

	if actual != expected {
		t.Fatal("expected ", expected, " got ", actual)
	}

	input = "chat"

	regex = ".*at"

	expected = true

	actual, err = Solution(input, regex)

	if err != nil {
		t.Fatal(err.Error())
	}

	if actual != expected {
		t.Fatal("expected ", expected, " got ", actual)
	}

	input = "chats"

	expected = false

	actual, err = Solution(input, regex)

	if err != nil {
		t.Fatal(err.Error())
	}

	if actual != expected {
		t.Fatal("expected ", expected, " got ", actual)
	}
}
