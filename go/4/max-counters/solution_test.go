package solution

import "testing"

func TestSolution(t *testing.T) {
	input := []int{3, 4, 4, 6, 1, 4, 4}
	expected := []int{3, 2, 2, 4, 2}
	actual := Solution(5, input)

	verify := func() {
		for i, val := range actual {
			if val != expected[i] {
				t.Fatal("expected", expected, "got", actual)
			}
		}
	}

	verify()

	actual = Solution(0, input)
	if actual != nil {
		t.Fatal("expected", expected, "got", actual)
	}

	input = []int{3, 1, 3, 5, 2, 1, 3, 2, 5, 2, 1, 3}
	actual = Solution(4, input)
	expected = []int{5, 5, 5, 4}
	verify()
}
