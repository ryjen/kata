package queue

type Queue struct {
	values []interface{}
}

func (queue *Queue) Push(val interface{}) {
	queue.values = append(queue.values, val)
}

func (queue *Queue) Pop() interface{} {
	val := queue.values[0]
	queue.values = queue.values[1:]
	return val
}

func (queue *Queue) Empty() bool {
	return len(queue.values) == 0
}
