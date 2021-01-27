package solution

func Solution(A []int) int {

	n := len(A) + 1

	if n == 1 {
		return n
	}

	total := (n + 1) * n / 2

	sum := 0

	for _, val := range A {
		sum += val
	}

	return total - sum
}
