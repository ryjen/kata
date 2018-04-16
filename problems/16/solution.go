package main

import (
	"container/list"
)

// an Order with an id
type Order struct {
	Id int
}

//a log of orders.  uses a linked list
type OrderLog struct {
	list *list.List
}

// adding orders pushes to the front which is O(1)
func (log *OrderLog) Record(order *Order) {
	log.list.PushFront(order)
}

// get the length of the list
func (log *OrderLog) Len() int {
	return log.list.Len()
}

// get the last 'i' item from the log which is O(n)
func (log *OrderLog) GetLast(index int) *Order {
	pos := 0
	for e := log.list.Back(); e != nil; e = e.Prev() {
		if pos == index {
			return e.Value.(*Order)
		}
		pos++
	}
	return nil
}

// the solution returns the log structure
func Solution() *OrderLog {
	return &OrderLog{
		list: list.New(),
	}
}
