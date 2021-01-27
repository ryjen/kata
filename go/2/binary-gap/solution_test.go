package solution

import (
	"io/ioutil"
	"strings"
	"testing"
)

var inputs []interface

func init() {
	inputs = [
	  [[3, 8, 9, 7, 6], 3],
    [[0, 0, 0], 1],
    [[1, 2, 3, 4], 4]
	]
)

func TestSolution(t *testing.T) {

	for input := range inputs {
		Solution(input...)
	}

}
