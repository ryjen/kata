package solution

import "testing"

func TestSolution(t *testing.T) {
	inputs := [][]int{
		[]int{2, 3, 1, 5},
		[]int{9, 5, 3, 1, 6, 8, 7, 4},
	}
	outputs := []int{
		4, 2,
	}
	for i, input := range inputs {
		expected := outputs[i]
		actual := Solution(input)
		if actual != expected {
			t.Fatal("expected", expected, "got", actual)
		}
	}
}
