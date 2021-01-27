package main

import (
	"math/bits"
)

func Solution(input int) int {
	result := 0
	count := 0
	for i := 0; i < bits.UintSize; i++ {
		if input&(1<<i) != 0 {
			if count > result {
				result = count
			}
			count = 0
			continue
		}
		count++
	}
	return result
}
