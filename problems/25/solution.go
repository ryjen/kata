package main

import (
	"errors"
)

func Solution(input string, regex string) (bool, error) {

	j := 0

	for i, c := range regex {
		// ran out of input to match
		if j == len(input) {
			return false, nil
		}

		if c == '.' {
			// match any character
			j++
			continue
		}

		if c == '*' {
			if i == 0 {
				return false, errors.New("invalid regex, a valid pattern or character must precede *")
			}

			match := regex[i-1]

			if match == '.' {
				// perform a match any character until next

				// no next pattern, then the rest of input matches
				if i+1 == len(regex) {
					return true, nil
				}

				// get the next pattern match
				match = regex[i+1]

				// skip input that doesn't match the next pattern
				for j < len(input) && input[j] != match {
					j++
				}

			} else {
				// skip input that matches
				for j < len(input) && input[j] == match {
					j++
				}
			}
			continue
		}

		// base case, stright up character matching
		if c != ([]rune)(input)[j] {
			return false, nil
		}

		j++
	}

	// if we still have input after the whole regex
	// we did not match everything
	if j < len(input) {
		return false, nil
	}

	// win
	return true, nil
}
