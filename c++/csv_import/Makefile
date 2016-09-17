CC=g++
CFLAGS=-I.
DEPS = import.hpp

%.o: %.cpp $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

all: import test

test: test.o
	$(CC) -o $@ $^ $(CFLAGS)

import: main.o
	$(CC) -o $@ $^ $(CFLAGS)

clean:
	rm -f test import
