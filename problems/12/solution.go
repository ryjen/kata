package main

func sum(X []int, S []int, N int) [][]int {
	for _, x := range X {
		
		if x < N {
			S = append(S, x)
			sum(X, S, N-x)
			S = S[:len(S)-1]
		} else {

		}

	}
}

func Solution(N int, X []int) [][]int {
	
	result := make([][]int)

	S := 

}
