# Java kata

Various code kata in Java


[Graphs](src/main/java/com/github/ryjen/kata/graph)
======

```Java

// create a graph with a vertex type of string
// could also be AdjacencyList<>() with the same result
// as the output isn't implementation dependent

Graph<String> graph = new AdjacencyMatrix<>();

// add some vertices

graph.addVertices("Hello", "World", "Robot", "Unnecessary", "Point");

// add some edges between vertices
        
graph.addEdge("World", "Robot");

graph.addEdge("Hello", "Point");

// print as a matrix
System.out.println(graph.toString(new VertexFormatter<>(graph)));

/*
 * OUTPUT:
 *             │ Hello       World       Robot       Unnecessary Point       
 * ────────────┼─────────────────────────────────────────────────────────────
 * Hello       │ ○           ○           ○           ○           ●           
 * World       │ ○           ○           ●           ○           ○           
 * Robot       │ ○           ●           ○           ○           ○           
 * Unnecessary │ ○           ○           ○           ○           ○           
 * Point       │ ●           ○           ○           ○           ○           
 *
 */
````

[Heaps](src/main/java/com/github/ryjen/kata/heap)
=====

```Java
// create a max heap with capcity of 5
// could also be a MinHeap<>(5) for reverse order output

Heap<Integer> heap = new MaxHeap<>(5);

// offer values to the heap
assert heap.offer(3);
assert heap.offer(8);
assert heap.offer(1);
assert heap.offer(4);
assert heap.offer(10);

// we've reach capacity and this value is not greater than the top
assert !heap.offer(7);

// however this value is greater
assert heap.offer(13);

while (!heap.isEmpty()) {
	System.out.println(heap.remove());
}

/*
 * OUTPUT:
 * 13
 * 10
 * 8
 * 4
 * 3
 */
```


[Permutations](src/main/java/com/github/ryjen/kata/Permutations.java)
============

```Java

// create an array of items
List<Character> items = Arrays.asList('A', 'B', 'C');

// find the permutations of the array
Permutation<Character> permutations = Permutation.generate(items);

// print the result
for(List<Character> perm : permutations) {
	for(Character ch : perm) {
		System.out.printf("%c ", ch);
	}
	System.out.println();
}

/*
 * OUTPUT:
 * C B A
 * B C A
 * B A C 
 * C A B
 * A C B
 * A B C
 */

```


Setup
=====

use maven or import maven project


TODO
====

- minimum spanning trees
- shortest path
- dynamic programming
- string matching
- ropes (text editor strings)
- binary trees
- backtracking