package main

import (
				"container/heap"
)

/**

Given an array of integers, find the first missing positive integer in linear time and constant space.
In other words, find the lowest positive integer that does not exist in the array.
The array can contain duplicates and negative numbers as well.  You can modify the array in-place.

Examples:

input: [3,4,-1,1]
output: 2

input: [1,2,0]
output: 3

**/

// O(n^2 using selection sort
func SolutionBruteForce(input []int) int {
				
				n := len(input)

				// selection sort in-place algorithm until we find the first missing positive
				
				for j := 0; j < n - 1; j++ {
								
								imin := j

								for i := j + 1; i < n; i++ {
												if input[i] < input[imin] {
																imin = i
												}
								}

								if imin != j {
												tmp := input[j]
												input[j] = input[imin]
												input[imin] = tmp
								}
				}

				for j := 0; j < n - 1; j++ {
								val := input[j] + 1

								if val > 0 && val != input[j + 1] {
												return val
								}
				}

				return input[n-1] + 1
}

// array based heap math
type IntHeap []int

// heap.Interface implementations

func (h IntHeap) Len() int { return len(h) }

func (h IntHeap) Less(i, j int) bool { return h[i] < h[j] }

// a beatiful swap method
func (h IntHeap) Swap(i, j int) { h[i], h[j] = h[j], h[i] }

func (h *IntHeap) Push(x interface{}) {
				// pointers to modify the array
				// push appends value
				*h = append(*h, x.(int))
}

func (h *IntHeap) Pop() interface{} {
				// get the array value
				old := *h
				// and the size
				n := len(old)
				// get the last value
				x := old[n-1]
				// assign the new array without the last value
				*h = old[0 : n-1]
				// return the leftover value
				return x
}

func Solution(input []int) int {

				impl := new(IntHeap)
				
				heap.Init(impl)

				for _, val := range input {
								heap.Push(impl, val)
				}

				if impl.Len() == 0 {
								return -1
				}

				last := heap.Pop(impl).(int)

				for impl.Len() > 0 {
								next := heap.Pop(impl).(int)

								val := last + 1

								if val > 0 && val != next {
												return val
								}

								last = next
				}

				return last + 1
}


