package main

import (
	"bufio"
	"bytes"
	"fmt"
	"github.com/ryjen/kata/tree"
	"strconv"
	"strings"
)

func BruteForceSerialize(tree *tree.Tree) string {
	values := []string{}

	tree.Walk(func(value interface{}) {
		values = append(values, fmt.Sprint(value))
	})

	return strings.Join(values, ",")
}

func BruteForceDeserialize(cereal string, comparer tree.Comparer) (*tree.Tree, error) {
	tree := tree.New(comparer)

	values := strings.Split(cereal, ",")

	for _, value := range values {
		v, err := strconv.Atoi(value)
		if err != nil {
			return tree, err
		}
		tree.Insert(v)
	}

	return tree, nil
}

func SolutionSerialize(tree *tree.Tree) string {

	buf := bytes.NewBuffer(nil)

	tree.Walk(func(value interface{}) {
		switch v := value.(type) {
		case string:
			buf.WriteString(v)
		default:
			buf.WriteString(fmt.Sprint(v))
		}
		buf.WriteRune(',')
	})
	if buf.Len() > 0 {
		buf.Truncate(buf.Len() - 1)
	}
	return buf.String()
}

func SolutionDeserialize(cereal string, comparer tree.Comparer) (*tree.Tree, error) {
	tree := tree.New(comparer)

	reader := strings.NewReader(cereal)

	buf := bufio.NewReader(reader)

	value, err := buf.ReadString(',')

	for len(value) > 0 && err == nil {
		value = strings.TrimRight(value, ",")

		i, err := strconv.Atoi(value)

		if err != nil {
			return tree, err
		}

		tree.Insert(i)

		value, err = buf.ReadString(',')
	}

	return tree, nil
}
