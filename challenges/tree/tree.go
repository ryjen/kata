package tree

import (
	"bytes"
	"os"
)

type Tree struct {
	Root     *node
	comparer Comparer
}

func New(comparer Comparer) *Tree {
	tree := new(Tree)
	tree.comparer = comparer
	return tree
}

func (t *Tree) Insert(value interface{}) {
	if t.Root == nil {
		t.Root = newNode(value)
		return
	}
	t.Root.insert(value, t.comparer)

	if t.Root.unbalanced() {
		t.rebalance()
	}
}

func (t *Tree) rebalance() {
	parent := newNode(0)
	parent.Left = t.Root
	parent.rebalance(t.Root)
	t.Root = parent.Left
}

func (t *Tree) Walk(visit Visitor) {
	if t.Root == nil {
		return
	}

	t.Root.Walk(visit)
}

func (t *Tree) Find(value interface{}) interface{} {
	if t.Root == nil {
		return nil
	}
	return t.Root.Find(value, t.comparer)
}

func (t *Tree) Dump() {
	t.Root.Dump(os.Stdout, 0, "")
}

func (t *Tree) String() string {

	if t.Root == nil {
		return ""
	}
	buf := bytes.NewBuffer(nil)

	t.Root.Dump(buf, 0, "")

	return buf.String()
}
