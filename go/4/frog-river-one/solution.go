package solution

func Solution(X int, A []int) int {

	if X <= 0 || len(A) == 0 {
		return -1
	}

	path := make(map[int]bool, X)

	count := 0

	for i, val := range A {
		if path[val] {
			continue
		}

		path[val] = true
		count++

		if count == X {
			return i
		}
	}
	return -1
}
