package main

/**

Given an array of integers, return a new array such that each element at index i of the new array is the product of all the numbers in the original array except the one at i.  Solve it without division and in O(n) time.

Examples:

input: [1,2,3,4,5]
output: [120,60,40,30,24]

input: [3,2,1]
output: [2,3,6]

**/

import (
	"errors"
)

func SolutionBruteForce(input []int) []int {

	// better start with brute force

	output := make([]int, len(input))

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

const MaxUint = ^uint(0)
const MaxInt = int(MaxUint >> 1)

func abs(x int) uint {
	if x < 0 {
		return uint(-x)
	}
	if x == 0 {
		return 0
	}
	return uint(x)
}

// Knowns:
// so to loop once you have the product in total
// the answer for each index is the total product without the value at index

// Questions:
// How to get the answer without without using division. Bitwise operators? multiplication?

// a divide method that uses bit shifts instead
func fastDiv(numerator int, divisor int) (int, error) {
	neg := false

	var n uint
	var d uint

	if numerator < 0 {
		n = abs(numerator)
		neg = true
	} else {
		n = uint(numerator)
	}

	if divisor < 0 {
		d = abs(divisor)
		neg = !neg
	} else {
		d = uint(divisor)
	}

	if d == 0 {
		return 0, errors.New("divide by zero")
	}

	var quotient uint = 0
	var remainder uint = 0
	var i uint
	for i = 31; i >= 0; i-- {
		if i > 31 {
			break
		}
		remainder <<= 1
		// set the least significant bit of remainder to bit i of the numerator
		remainder = (remainder & 0xFE) | ((n >> i) & 1)
		if remainder >= d {
			remainder -= d
			quotient |= (1 << i)
		}
	}

	if quotient >= uint(MaxInt) {
		return int(quotient), errors.New("overflow")
	}

	if neg {
		return -int(quotient), nil
	} else {
		return int(quotient), nil
	}
}

func Solution(input []int) ([]int, error) {

	output := make([]int, len(input))

	var total int = 1

	// set the total product
	for _, val := range input {
		total *= val
	}

	// fast divide the index value for each output
	for i := range input {
		val, err := fastDiv(total, input[i])
		if err != nil {
			return output, err
		}
		output[i] = val
	}

	return output, nil
}
