package main

import (
				"testing"
)

func TestSolution1(t *testing.T) {
				
				input := []int{3,4,-1,1}
				
				expected := 2

				actual := Solution(input)

				if expected != actual {
								t.Fatal("expected ", expected, " got ", actual)
				}

				actual = SolutionBruteForce(input)

				if expected != actual {
								t.Fatal("brute force expected ", expected, " got ", actual)
				}
}

func TestSolution2(t *testing.T) {

				input := []int{1,2,0}

				expected := 3

				actual := Solution(input)

				if expected != actual {
								t.Fatal("expected ", expected, " got ", actual)
				}

				actual = SolutionBruteForce(input)

				if expected != actual {
								t.Fatal("expected ", expected, " got ", actual)
				}
}

func generateBenchmarkValue() []int {
				const size = 100000

				input := make([]int, size)

				for i := 0; i < size; i++ {
								input[i] = i
				}

				input[size-2] = input[size-3]

				return input
}

func BenchmarkSolutionBruteForce(t *testing.B) {

				input := generateBenchmarkValue()

				_ = SolutionBruteForce(input)
}

func BenchmarkSolution(t *testing.B) {
				
				input := generateBenchmarkValue()

				_ = Solution(input)
}


