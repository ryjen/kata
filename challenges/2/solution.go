package main

/**

Given an array of integers, return a new array such that each element at index i of the new array is the product of all the numbers in the original array except the one at i.  Solve it without division and in O(n) time.

Examples:

input: [1,2,3,4,5]
output: [120,60,40,30,24]

input: [3,2,1]
output: [2,3,6]

**/

func SolutionBruteForce(input []uint) []uint {

	// better start with brute force

	output := make([]uint, len(input))

	for i := range output {
		output[i] = 1
	}

	// O(n^2)
	for i := range input {

		for j := range input {
			if i == j {
				continue
			}

			output[i] = output[i] * input[j]
		}
	}

	return output
}

// Knowns:
// so to loop once you have the product in total
// the answer for each index is the total product without the value at index

// Questions:
// How to get the answer without without using division. Bitwise operators? multiplication?

// a divide method that uses bit shifts instead
func fastDiv(dividend uint, divisor uint) uint {
	if divisor == 0 {
		return 0
	}

	var scaled_divisor uint = divisor
	var remain uint = dividend
	var result uint = 0
	var multiple uint = 1

	for scaled_divisor < dividend {
		scaled_divisor = scaled_divisor + scaled_divisor
		multiple = multiple + multiple
	}
	for ok := true; ok; ok = multiple != 0 {
		if remain >= scaled_divisor {
			remain = remain - scaled_divisor
			result = result + multiple
		}
		scaled_divisor = scaled_divisor >> 1
		multiple = multiple >> 1
	}
	return result
}

func Solution(input []uint) []uint {

	output := make([]uint, len(input))

	var total uint = 1

	// set the total product
	for _, val := range input {
		total *= val
	}

	// fast divide the index value for each output
	for i := range input {
		output[i] = fastDiv(total, input[i])
	}

	return output
}
