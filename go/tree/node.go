package tree

import (
	"fmt"
	"github.com/ryjen/kata/queue"
	"io"
	"strings"
)

type node struct {
	Value   interface{}
	Left    *node
	Right   *node
	balance int
}

type Visitor func(value interface{})

type Comparer interface {
	Compare(a, b interface{}) int
}

type Comparator struct {
	Comparer func(a, b interface{}) int
}

func (c Comparator) Compare(a, b interface{}) int { return c.Comparer(a, b) }

func newNode(value interface{}) *node {
	node := new(node)
	node.Value = value
	node.balance = 0
	return node
}

func (n *node) unbalanced() bool {
	return n.balance < -1 || n.balance > 1
}

func (n *node) insert(value interface{}, comparer Comparer) bool {
	switch {
	case comparer.Compare(value, n.Value) == 0:
		n.Value = value
		return false
	case comparer.Compare(value, n.Value) < 0:
		if n.Left == nil {
			n.Left = newNode(value)
			if n.Right == nil {
				n.balance = -1
			} else {
				n.balance = 0
			}
		} else if n.Left.insert(value, comparer) {
			if n.Left.unbalanced() {
				n.rebalance(n.Left)
			} else {
				n.balance--
			}
		}
	default:
		if n.Right == nil {
			n.Right = newNode(value)
			if n.Left == nil {
				n.balance = 1
			} else {
				n.balance = 0
			}
		} else if n.Right.insert(value, comparer) {
			if n.Right.unbalanced() {
				n.rebalance(n.Right)
			} else {
				n.balance++
			}
		}
	}

	return n.balance != 0
}

func (n *node) rebalance(c *node) {
	switch {
	// Left subtree is too high, and left child has a left child.
	case c.balance == -2 && c.Left.balance == -1:
		n.rotateRight(c)
	// Right subtree is too high, and right child has a right child.
	case c.balance == 2 && c.Right.balance == 1:
		n.rotateLeft(c)
	// Left subtree is too high, and left child has a right child.
	case c.balance == -2 && c.Left.balance == 1:
		n.rotateLeftChildThenRight(c)
	// Right subtree is too high, and right child has a left child.
	case c.balance == 2 && c.Right.balance == -1:
		n.rotateRightChildThenLeft(c)
	}
}

func (n *node) rotateLeft(c *node) {
	// Save `c`'s right child.
	r := c.Right
	// `r`'s left subtree gets reassigned to `c`.
	c.Right = r.Left
	// `c` becomes the left child of `r`.
	r.Left = c
	// Make the parent node (that is, the current one) point to the new root node.
	if c == n.Left {
		n.Left = r
	} else {
		n.Right = r
	}
	// Finally, adjust the balances. After a single rotation, the subtrees are always of the same height.
	c.balance = 0
	r.balance = 0
}

// `rotateRight` is the mirrored version of `rotateLeft`.
func (n *node) rotateRight(c *node) {
	l := c.Left
	c.Left = l.Right
	l.Right = c
	if c == n.Left {
		n.Left = l
	} else {
		n.Right = l
	}
	c.balance = 0
	l.balance = 0
}

// `rotateRightLeft` first rotates the right child of `c` to the right, then `c` to the left.
func (n *node) rotateRightChildThenLeft(c *node) {
	// `rotateRight` assumes that the left child has a left child, but as part of the rotate-right-left process,
	// the left child of `c.Right` is a leaf. We therefore have to tweak the balance factors before and after
	// calling `rotateRight`.
	// If we did not do that, we would not be able to reuse `rotateRight` and `rotateLeft`.
	c.Right.Left.balance = 1
	c.rotateRight(c.Right)
	c.Right.balance = 1
	n.rotateLeft(c)
}

func (n *node) rotateLeftChildThenRight(c *node) {
	c.Left.Right.balance = -1
	c.rotateLeft(c.Left)
	c.Left.balance = -1
	n.rotateRight(c)
}

func (n *node) Find(value interface{}, comparer Comparer) interface{} {

	if n == nil {
		return nil
	}

	switch {
	case comparer.Compare(value, n.Value) == 0:
		return n.Value
	case comparer.Compare(value, n.Value) < 0:
		return n.Left.Find(value, comparer)
	default:
		return n.Right.Find(value, comparer)
	}
}

func (n *node) Walk(visit Visitor) {
	if n.Value == nil {
		return
	}

	queue := queue.Queue{}

	queue.Push(n)

	for !queue.Empty() {
		node := queue.Pop().(*node)
		if node.Value != nil {
			visit(node.Value)
		}
		if node.Left != nil {
			queue.Push(node.Left)
		}
		if node.Right != nil {
			queue.Push(node.Right)
		}
	}
}

func (n *node) Dump(w io.Writer, i int, lr string) {
	if n == nil {
		return
	}
	indent := ""
	if i > 0 {
		indent = strings.Repeat(" ", (i-1)*4) + "+" + lr + "--"
	}
	fmt.Fprintf(w, "%s%v[%d]\n", indent, n.Value, n.balance)
	n.Left.Dump(w, i+1, "L")
	n.Right.Dump(w, i+1, "R")
}

func (n *node) Print(w io.Writer, prefix string, isLeft bool) {
	if n == nil || n.Value == nil {
		return
	}
	if isLeft {
		fmt.Fprintln(w, prefix, "|-- ", n.Value)
		n.Left.Print(w, "|   ", true)
		n.Right.Print(w, "|   ", false)
	} else {
		fmt.Fprintln(w, prefix, "\\-- ", n.Value)
		n.Left.Print(w, prefix+"    ", true)
		n.Right.Print(w, prefix+"    ", false)
	}
}
