package solution

import "testing"

var (
	inputs = [][]int{
		[]int{
			3, 1, 2, 4, 3,
		},
		[]int{
			4, 3, 5, 2, 8,
		},
		[]int{},
		[]int{
			1, 200,
		},
	}

	outputs = []int{
		1, 2, 0, 199,
	}
)

func TestSolution(t *testing.T) {
	for i, input := range inputs {
		expected := outputs[i]
		actual := Solution(input)
		if actual != expected {
			t.Fatal("expected ", expected, " actual ", actual)
		}
	}
}
