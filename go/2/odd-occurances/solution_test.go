package solution

import "testing"

var (
	inputs = [][]int{
		[]int{
			9, 3, 9, 3, 9, 7, 9,
		},
		[]int{
			4, 2, 6, 4, 3, 2, 3,
		},
	}
	outputs = []int{7, 6}
)

func TestSolution(t *testing.T) {
	for i, input := range inputs {
		actual := Solution(input)

		if actual != outputs[i] {
			t.Fatal("expected", outputs[i], "got", actual)
		}
	}
}
