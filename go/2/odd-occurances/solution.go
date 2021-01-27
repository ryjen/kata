package solution

func Solution(A []int) int {

	table := make(map[int]int)

	for _, val := range A {
		table[val]++
	}

	for key, val := range table {
		if val%2 != 0 {
			return key
		}
	}

	return 0
}
