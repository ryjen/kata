
package main

import (
				"testing"
)

func compareIntArray(a1 []uint, a2 []uint) bool {
			
				if len(a1) != len(a2) {
								return false
				}

				for i, v := range a1 {
								if v != a2[i] {
												return false
								}
				}
				return true
}

func TestChallengeOne(t *testing.T) {
				input := []uint{ 1,2,3,4,5 }

				// haha that was kotlin.  cobwebs in the head.

				expected := []uint{ 120,60,40,30,24 }

				actual := ChallengeOne(input)

				if !compareIntArray(expected, actual) {
								t.Fatal("expected ", expected, " got ", actual)
				}
}
