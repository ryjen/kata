package main

func Solution(input []int, k int) []int {

	result := make([]int, len(input))

	if len(input) == 0 {
		return result
	}

	sum := 0
	front := 0

	for i := 0; i < len(input); i++ {
		if i%k == 0 {
			sum -= input[front]
			front++
		}
		sum += input[i]
	}

	return result
}
