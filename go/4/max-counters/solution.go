package solution

func Solution(N int, A []int) []int {
	if N <= 0 || len(A) == 0 {
		return nil
	}

	currentMax := 0
	currentReset := 0

	counters := make([]int, N)

	for _, val := range A {
		if val == N+1 {
			currentReset = currentMax
			continue
		}

		i := val - 1

		count := counters[i]

		if count < currentReset {
			count = currentReset
		}

		count++

		if count > currentMax {
			currentMax = count
		}

		counters[i] = count
	}

	for i, val := range counters {
		if val < currentReset {
			counters[i] = currentReset
		}
	}

	return counters
}
