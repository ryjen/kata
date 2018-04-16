package main

// O(n^2)
func SolutionBruteForce(s string, k int) int {
	size := len(s)
	for i := 0; i < size; i++ {
		count := 1
		for j := i + 1; j < size; j++ {
			if s[i] == s[j] {
				count++

				if count == k {
					sub := j - i + 1

					if sub < size {
						return sub
					}
				}
			}
		}
	}
	return 0
}

const (
	MaxUint = ^uint(0)
	MaxInt  = int(MaxUint >> 1)
)

/*
 * trade some time complexity for space complexity.
 *
 */
func Solution(s string, k int, unicode bool) int {

	var tableSize int

	if unicode {
		// max rune size (defined as int32)
		tableSize = MaxInt
	} else {
		// max char size
		tableSize = 255
	}

	size := len(s)

	type Entry struct {
		count int
		start int
		end   int
	}

	values := make([]*Entry, tableSize)

	for i, c := range s {

		val := values[c]

		if val == nil {
			val = &Entry{1, i, i}
			values[c] = val
		} else {
			val.count++
			val.end = i
		}

		if val.count == k {
			sub := val.end - val.start + 1

			if sub != size {
				return sub
			}
		}
	}

	return 0
}
