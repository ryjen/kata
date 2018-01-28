package main

import (
	"github.com/ryjen/kata/tree"
	"math/rand"
	"testing"
)

var (
	IntCompare = tree.Comparator{
		Comparer: func(a, b interface{}) int {
			x, ok := a.(int)

			if !ok {
				return -1
			}

			y, ok := b.(int)

			if !ok {
				return 1
			}
			return x - y
		},
	}
)

func generateTestTree(values []int) *tree.Tree {
	tree := tree.New(IntCompare)

	for _, val := range values {
		tree.Insert(val)
	}
	return tree
}

func generateBenchmarkTree() *tree.Tree {
	tree := tree.New(IntCompare)

	const size = 100000

	for i := 0; i < size; i++ {
		tree.Insert(rand.Int())
	}

	return tree
}

func BenchmarkSolution(b *testing.B) {
	tree := generateBenchmarkTree()

	cereal := SolutionSerialize(tree)

	_, err := SolutionDeserialize(cereal, IntCompare)

	if err != nil {
		b.Fatal(err.Error())
	}
}

func BenchmarkBruteFroce(b *testing.B) {
	tree := generateBenchmarkTree()

	cereal := BruteForceSerialize(tree)

	tree, err := BruteForceDeserialize(cereal, IntCompare)

	if err != nil {
		b.Fatal(err.Error())
	}
}

func TestSolution(t *testing.T) {
	tree := generateTestTree([]int{10, 15, 5, 12})

	expected := "10,5,15,12"

	actual := SolutionSerialize(tree)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	tree, err := SolutionDeserialize(actual, IntCompare)

	if err != nil {
		t.Fatal(err.Error())
	}

	actual = SolutionSerialize(tree)

	if expected != actual {
		t.Fatal("deserialize expected ", expected, " got ", actual)
	}
}

func TestBruteForce(t *testing.T) {
	tree := generateTestTree([]int{10, 15, 5, 12})

	expected := "10,5,15,12"

	actual := BruteForceSerialize(tree)

	if expected != actual {
		t.Fatal("expected ", expected, " got ", actual)
	}

	tree, err := BruteForceDeserialize(actual, IntCompare)

	if err != nil {
		t.Fatal(err.Error())
	}

	actual = SolutionSerialize(tree)

	if expected != actual {
		t.Fatal("deserialize expected ", expected, " got ", actual)
	}
}

func TestLargeTree(t *testing.T) {
	tree := generateBenchmarkTree()

	expected := SolutionSerialize(tree)

	tree, err := SolutionDeserialize(expected, IntCompare)

	if err != nil {
		t.Fatal(err.Error())
	}

	actual := SolutionSerialize(tree)

	if expected != actual {
		t.Fatal("Large tree differs")
	}

}

func TestTreeAdd(t *testing.T) {

	tree := generateTestTree([]int{10, 15, 5, 12})

	root := tree.Root

	if root.Value != 10 {
		t.Fatal("expected 10 got ", root.Value)
	}

	node := root.Left

	if node.Value != 5 {
		t.Fatal("expected 5 got ", node.Value)
	}

	node = root.Right

	if node.Value != 15 {
		t.Fatal("expected 15 got ", node.Value)
	}

	node = node.Left

	if node.Value != 12 {
		t.Fatal("expected 12 got ", node.Value)
	}
}
