package main

import (
	"container/heap"
)

type item struct {
	index int
	value int
}

type PriorityQueue []*item

func (pq PriorityQueue) Len() int {
	return len(pq)
}

func (pq PriorityQueue) Less(i, j int) bool {
	return pq[i].value > pq[j].value
}

func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
}

func (pq *PriorityQueue) Push(x interface{}) {
	item := x.(*item)
	*pq = append(*pq, item)
}

func (pq *PriorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	*pq = old[0 : n-1]
	return item
}

func Solution(input []int) int {
	if len(input) == 0 {
		return 0
	}

	pq := make(PriorityQueue, len(input))

	for i, value := range input {
		pq[i] = &item{
			value: value,
			index: i,
		}
	}
	heap.Init(&pq)

	top := heap.Pop(&pq).(*item)

	for pq.Len() > 0 {
		adj := heap.Pop(&pq).(*item)

		if adj.index+1 != top.index && adj.index != top.index+1 {
			return top.value + adj.value
		}
	}
	return 0
}
