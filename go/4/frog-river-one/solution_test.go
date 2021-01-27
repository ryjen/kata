package solution

import (
	"testing"

	"k8s.io/apimachinery/pkg/util/rand"
)

func init() {
	rand.Seed(42)
}

func genSequence(N int) []int {
	input := make([]int, N)

	for i := 0; i < N; i++ {
		input[i] = i + 1
	}

	return input
}

func chance() bool {
	chance := rand.Intn(10)
	return chance < 5
}

func genSequenceWithDupes(N int, X int) ([]int, int) {
	input := make([]int, N)

	actual := 1
	expected := 0

	for i := 0; i < N; i++ {

		if i > 0 && N-i-1 > X-actual && chance() {
			prev := rand.IntnRange(0, i-1)
			input[i] = input[prev]
		} else {
			input[i] = actual
			if actual == X {
				expected = i
			}
			actual++
		}
	}
	return input, expected
}

func TestSolution(t *testing.T) {
	input := []int{1, 3, 1, 4, 2, 3, 5, 4}
	expected := 6
	actual := Solution(5, input)
	if actual != expected {
		t.Fatal("expected", expected, "got", actual)
	}

	input = genSequence(100000)
	X := len(input) - 10
	expected = X - 1
	actual = Solution(X, input)
	if actual != expected {
		t.Fatal("expected", expected, "got", actual)
	}

	input = []int{}
	expected = -1
	actual = Solution(5, input)
	if actual != expected {
		t.Fatal("expected", expected, "got", actual)
	}

	input = genSequence(500)
	actual = Solution(0, input)
	if actual != expected {
		t.Fatal("expected", expected, "got", actual)
	}

	input, expected = genSequenceWithDupes(20, 15)
	actual = Solution(15, input)
	if actual != expected {
		t.Fatal("expected", expected, "got", actual)
	}
}
