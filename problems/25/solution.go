package main

import (
	"errors"
)

func Solution(input string, regex string) (bool, error) {

	j := 0

	for i, c := range regex {

		if c == '.' {
			// match any character
			j++
			continue
		}

		if c == '*' {
			if i == 0 {
				return false, errors.New("invalid regex, a valid pattern or character must precede *")
			}

			last := regex[i-1]

			if last == '.' {
				// nothing left to match
				if i+1 == len(regex) {
					return true, nil
				}
				// zero or more until the next character is found
				last = regex[i+1]
			}

			// skip input as necessary
			for j < len(input) && input[j] != last {
				j++
			}

			// doesn't match next character
			if j == len(input) {
				return false, nil
			}
			continue
		}

		// base case, stright up character matching
		if c != ([]rune)(input)[j] {
			return false, nil
		}

		j++
	}

	if j < len(input) {
		return false, nil
	}

	return true, nil
}
