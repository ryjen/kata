package main

import (
	"testing"
)

func TestSolution(t *testing.T) {
	// start a new log
	log := Solution()

	// add 4 orders
	log.Record(&Order{Id: 2})
	log.Record(&Order{Id: 3})
	log.Record(&Order{Id: 4})
	log.Record(&Order{Id: 5})

	expected := 4

	// should have 4 orders in log
	if log.Len() != expected {
		t.Fatal("expected length ", expected, " got ", log.Len())
	}

	// get the second last item
	order := log.GetLast(2)

	expected = 4

	// id should equal 4
	if order.Id != expected {
		t.Fatal("expected id ", expected, " got ", log.Len())
	}
}

const MaxOrder = 10000000

func buildBenchmark() *OrderLog {

	log := Solution()

	for i := 0; i < MaxOrder; i++ {
		log.Record(&Order{Id: i + 1})
	}

	return log
}

func BenchmarkBegining(b *testing.B) {

	log := buildBenchmark()

	log.GetLast(MaxOrder - 5)
}

func BenchmarkEnding(b *testing.B) {

	log := buildBenchmark()

	log.GetLast(5)
}
