package solution

func sum(A []int) int {
	sum := 0
	for _, val := range A {
		sum += val
	}
	return sum
}

func Solution(A []int) int {
	N := len(A)

	if N == 0 {
		return 0
	}

	result := (1 << 31) - 1

	left := A[0]
	right := sum(A[1:])

	for i := 1; i < N; i++ {
		diff := left - right

		if diff < 0 {
			diff = -diff
		}

		if diff < result {
			result = diff
		}

		left += A[i]
		right -= A[i]
	}

	return result
}
