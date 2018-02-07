package main

// O(n^2)
func SolutionBruteForce(s string, k int) int {
	size := len(s)
	for i := 0; i < size; i++ {
		count := 1
		for j := i + 1; j < size; j++ {
			if s[i] == s[j] {
				count++

				if count == k {
					sub := j - i + 1

					if sub < size {
						return sub
					}
				}
			}
		}
	}
	return 0
}

func Solution(s string, k int) int {

	size := len(s)

	counts := make([]int, 255)
	starts := make([]int, 255)
	ends := make([]int, 255)

	for i := 0; i < size; i++ {
		c := s[i]
		counts[c]++

		if starts[c] == 0 {
			starts[c] = i
		} else {
			ends[c] = i
		}

		if counts[c] == k {
			sub := ends[c] - starts[c] + 1

			if sub != size {
				return sub
			}
		}
	}

	return 0
}
