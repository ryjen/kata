package solution

import "testing"

func TestSolution(t *testing.T) {
	inputs := [][]int{
		[]int{
			10, 85, 30,
		},
		[]int{
			12, 96, 15,
		},
	}
	outputs := []int{
		3,
	}

	for i, input := range inputs {
		actual := Solution(input[0], input[1], input[2])
		if actual != outputs[i] {
			t.Fatal("expected", outputs[i], "got", actual)
		}
	}
}
