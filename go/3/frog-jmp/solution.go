package solution

import "math"

func Solution(X int, Y int, D int) int {

	steps := math.Ceil(float64(Y-X) / float64(D))

	return int(steps)
}
