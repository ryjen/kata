package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;
import com.github.ryjen.kata.graph.formatters.*;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.search.Ordering;
import com.github.ryjen.kata.graph.sort.TopologicalSort;
import com.github.ryjen.kata.graph.tree.MinimumSpanningTree;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * runs all the unit tests for different graph implementations
 */
public abstract class GraphTest {

    protected abstract <E extends Comparable<E>, V extends Comparable<V>> Graphable<E, V> getImplementation();

    private Graph<Integer, Integer> newIndexGraph(int size, boolean directed) {
        Graph<Integer, Integer> graph = new Graph<Integer, Integer>(getImplementation(), directed);

        for (int i = 0; i < size; i++) {
            graph.addVertex(i);
        }

        return graph;
    }

    @Test
    public void testDefaultGraph() {
        Graph<?, ?> graph = new Graph();

        String expected = "";
        String actual = graph.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDefaultGraphWithVertices() {
        Graph<?, Character> graph = new Graph<>();

        graph.addVertices(Arrays.asList('A', 'B', 'C'));

        StringBuffer buf = new StringBuffer();
        buf.append("  │ A B C \n");
        buf.append("──┼───────\n");
        buf.append("A │       \n");
        buf.append("B │       \n");
        buf.append("C │       \n");
        String expected = buf.toString();
        String actual = graph.toString(new VertexFormatter<>(graph));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDefaultGraphWithEdges() {
        Graph<Integer, Character> graph = new Graph<>();

        graph.addVertices(Arrays.asList('A', 'B', 'C'));

        graph.addEdge('A', 'B', 2);
        graph.addEdge('B', 'C', 3);

        StringBuffer buf = new StringBuffer();
        buf.append("  2   \n");
        buf.append("2   3 \n");
        buf.append("  3   \n");
        String expected = buf.toString();
        String actual = graph.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUndirectedAdjacentVertices() {

        Graph<Integer, Integer> graph = newIndexGraph(4, false);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 2);
        graph.addEdge(0, 3, 3);
        graph.addEdge(3, 2, 4);

        Iterable<Integer> it = graph.adjacent(0);

        List<Integer> actual = StreamSupport.stream(it.spliterator(), false).collect(Collectors.toList());

        List<Integer> expected = Arrays.asList(1, 2, 3);

        Assert.assertEquals(expected, actual);

        it = graph.adjacent(3);

        actual = StreamSupport.stream(it.spliterator(), false).collect(Collectors.toList());

        expected = Arrays.asList(0, 2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDirectedAdjacentVertices() {

        Graph<Integer, Integer> graph = newIndexGraph(4, true);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 5);
        graph.addEdge(0, 3, 6);
        graph.addEdge(3, 2, 7);

        Iterable<Integer> it = graph.adjacent(0);

        List<Integer> actual = StreamSupport.stream(it.spliterator(), false).collect(Collectors.toList());

        List<Integer> expected = Arrays.asList(1, 2, 3);

        Assert.assertEquals(expected, actual);

        it = graph.adjacent(3);

        actual = StreamSupport.stream(it.spliterator(), false).collect(Collectors.toList());

        expected = Collections.singletonList(2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddEdgeUndirected() {
        Graph<Integer, Integer> graph = newIndexGraph(4, false);
        Assert.assertFalse(graph.isDirected());
        graph.addEdge(1, 2, 3);
        Assert.assertTrue(graph.isEdge(1, 2));
        Assert.assertTrue(graph.isEdge(2, 1));
    }

    @Test
    public void testAddEdgeDirected() {
        Graph<Integer, Integer> graph = newIndexGraph(4, true);
        Assert.assertTrue(graph.isDirected());
        graph.addEdge(1, 2, 3);
        Assert.assertTrue(graph.isEdge(1, 2));
        Assert.assertFalse(graph.isEdge(2, 1));
    }

    @Test
    public void testSimpleToStringUndirected() {
        Graph<Integer, Integer> graph = newIndexGraph(4, false);

        Assert.assertFalse(graph.isDirected());

        graph.addEdge(1, 2, 4);
        graph.addEdge(0, 3, 5);

        StringBuilder buf = new StringBuilder();
        buf.append("      5 \n");
        buf.append("    4   \n");
        buf.append("  4     \n");
        buf.append("5       \n");

        String actual = graph.toString(new SimpleFormatter<>(graph));
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyLabelToString() {
        Graph<Character, Character> graph = new Graph<Character, Character>(getImplementation(), false);

        graph.addVertices(Arrays.asList('A', 'B', 'C', 'D', 'Z'));

        graph.addEdge('B', 'D', 'X');

        StringBuilder buf = new StringBuilder();
        buf.append("  │ A B C D Z \n");
        buf.append("──┼───────────\n");
        buf.append("A │ ○ ○ ○ ○ ○ \n");
        buf.append("B │ ○ ○ ○ X ○ \n");
        buf.append("C │ ○ ○ ○ ○ ○ \n");
        buf.append("D │ ○ X ○ ○ ○ \n");
        buf.append("Z │ ○ ○ ○ ○ ○ \n");

        String expected = buf.toString();
        String actual = graph.toString(new LabelFormatter<>(graph));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSimpleToStringDirected() {
        Graph<Integer, Integer> graph = newIndexGraph(4, true);

        Assert.assertTrue(graph.isDirected());

        graph.addEdge(1, 2, 4);
        graph.addEdge(0, 3, 5);

        StringBuilder buf = new StringBuilder();
        buf.append("      5 \n");
        buf.append("    4   \n");
        buf.append("        \n");
        buf.append("        \n");

        String actual = graph.toString(new SimpleFormatter<>(graph));
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testListToStringDirected() {
        Graph<Integer, Integer> graph = newIndexGraph(5, true);

        graph.addEdge(1, 2, 5);
        graph.addEdge(1, 4, 6);
        graph.addEdge(0, 3, 7);

        StringBuilder buf = new StringBuilder();
        buf.append("0 → 3\n");
        buf.append("1 → 2, 4\n");
        buf.append("2 → \n");
        buf.append("3 → \n");
        buf.append("4 → \n");

        String actual = graph.toString(new ListFormatter<>(graph));
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDegreeUndirected() {
        Graph<Integer, Integer> graph = newIndexGraph(4, false);

        graph.addEdge(1, 2, 4);

        Assert.assertEquals(2, graph.degree(1));

        graph.addEdge(3, 1, 5);

        Assert.assertEquals(4, graph.degree(1));

        graph.addEdge(1, 0, 6);

        Assert.assertEquals(6, graph.degree(1));

        // no connection to vertex 1
        graph.addEdge(2, 0, 7);

        Assert.assertEquals(6, graph.degree(1));

        // already added edge
        graph.addEdge(2, 1, 4);

        Assert.assertEquals(6, graph.degree(1));
    }

    @Test
    public void testDegreeDirected() {
        Graph<Integer, Integer> graph = newIndexGraph(4, true);

        graph.addEdge(1, 2, 3);

        Assert.assertEquals(0, graph.inDegree(1));

        Assert.assertEquals(1, graph.outDegree(1));

        Assert.assertEquals(1, graph.degree(1));

        graph.addEdge(3, 1, 4);

        Assert.assertEquals(1, graph.inDegree(1));

        Assert.assertEquals(1, graph.outDegree(1));

        Assert.assertEquals(2, graph.degree(1));

        graph.addEdge(1, 0, 5);

        Assert.assertEquals(1, graph.inDegree(1));

        Assert.assertEquals(2, graph.outDegree(1));

        Assert.assertEquals(3, graph.degree(1));

        // no connection to vertex 1
        graph.addEdge(2, 0, 6);

        Assert.assertEquals(3, graph.degree(1));

        graph.addEdge(2, 1, 7);

        Assert.assertEquals(2, graph.inDegree(1));

        Assert.assertEquals(2, graph.outDegree(1));

        Assert.assertEquals(4, graph.degree(1));
    }

    @Test
    public void testVertexFormatting() {
        Graph<Integer, String> graph = new Graph<Integer, String>(getImplementation(), false);

        graph.addVertices(Arrays.asList("Hello", "World", "Robot", "Unnecessary", "Point"));

        graph.addEdge("World", "Robot", 1);

        graph.addEdge("Hello", "Point", 2);

        StringBuilder buf = new StringBuilder();
        buf.append("            │ Hello       World       Robot       Unnecessary Point       \n");
        buf.append("────────────┼─────────────────────────────────────────────────────────────\n");
        buf.append("Hello       │ ○           ○           ○           ○           ●           \n");
        buf.append("World       │ ○           ○           ●           ○           ○           \n");
        buf.append("Robot       │ ○           ●           ○           ○           ○           \n");
        buf.append("Unnecessary │ ○           ○           ○           ○           ○           \n");
        buf.append("Point       │ ●           ○           ○           ○           ○           \n");

        String expected = buf.toString();

        String actual = graph.toString(new SymbolFormatter<>(graph));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTopologicalSort() {

        Graph<Integer, Integer> graph = newIndexGraph(6, true);

        graph.addEdge(5, 2, 6);
        graph.addEdge(5, 0, 7);
        graph.addEdge(4, 0, 8);
        graph.addEdge(4, 1, 9);
        graph.addEdge(2, 3, 10);
        graph.addEdge(3, 1, 11);

        try {
            TopologicalSort<Integer, Integer> sorter = new TopologicalSort<>(graph);

            Collection<Integer> sorted = sorter.sort();

            Collection<Integer> expected = Arrays.asList(5, 4, 2, 3, 1, 0);

            Assert.assertEquals(expected, sorted);

        } catch (GraphCyclicException | GraphDirectedException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testDirectAcyclicGraph() {
        Graph<Integer, Integer> graph = newIndexGraph(6, true);

        graph.addEdge(4, 2, 6);
        graph.addEdge(2, 4, 7);
        graph.addEdge(1, 5, 8);
        graph.addEdge(3, 0, 9);

        Assert.assertTrue(graph.isCyclic());

        graph.removeEdge(2, 4);

        Assert.assertFalse(graph.isCyclic());
    }


    @Test
    public void testDepthFirstSearchPreOrder() {
        final Graph<Integer, Character> g = new Graph<Integer, Character>(getImplementation(), false);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G'));

        g.addEdge('A', 'B', 1);
        g.addEdge('B', 'D', 2);
        g.addEdge('B', 'F', 3);
        g.addEdge('A', 'C', 4);
        g.addEdge('C', 'G', 5);
        g.addEdge('A', 'E', 6);
        g.addEdge('E', 'F', 7);

        g.dfs('A', actual::add, Ordering.Pre);

        List<Character> expected = Arrays.asList('A', 'E', 'F', 'B', 'D', 'C', 'G');

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testDepthFirstSearchPostOrder() {
        final Graph<Integer, Character> g = new Graph<Integer, Character>(getImplementation(), false);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D'));

        g.addEdge('A', 'B', 1);
        g.addEdge('B', 'D', 1);
        g.addEdge('C', 'D', 2);
        g.addEdge('A', 'C', 2);

        g.dfs('A', actual::add, Ordering.Post);

        List<Character> expected = Arrays.asList('C', 'D', 'B', 'A');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDepthFirstSearchReversePostOrder() {
        final Graph<Integer, Character> g = new Graph<Integer, Character>(getImplementation(), true);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D'));

        g.addEdge('A', 'B', 1);
        g.addEdge('A', 'C', 2);
        g.addEdge('B', 'D', 3);
        g.addEdge('C', 'D', 3);

        g.dfs('A', actual::add, Ordering.ReversePost);

        List<Character> expected = Arrays.asList('A', 'C', 'B', 'D');

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testBreadthFirstSearch() {
        final Graph<Integer, Character> g = new Graph<Integer, Character>(getImplementation(), false);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G'));

        g.addEdge('A', 'B', 1);
        g.addEdge('B', 'D', 2);
        g.addEdge('B', 'F', 3);
        g.addEdge('A', 'C', 4);
        g.addEdge('C', 'G', 5);
        g.addEdge('A', 'E', 6);
        g.addEdge('E', 'F', 7);

        g.bfs('A', actual::add);

        List<Character> expected = Arrays.asList('A', 'B', 'C', 'E', 'D', 'F', 'G');


        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testConnectedGraph() {
        Graph<Integer, Integer> graph = newIndexGraph(3, false);

        Assert.assertFalse(graph.isConnected());

        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 4);

        Assert.assertTrue(graph.isConnected());

    }

    @Test
    public void testAdjacentEdges() {
        Graph<Integer, Integer> graph = new Graph<Integer, Integer>(getImplementation(), false);

        graph.addVertices(Arrays.asList(0, 1, 2, 3, 4));

        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 1, 1);
        graph.addEdge(3, 4, 3);

        List<Integer> expected = Arrays.asList(1, 3, 4);

        List<Integer> actual = StreamSupport.stream(graph.edges(1).spliterator(), false).map(Edge::getLabel).sorted().collect(Collectors.toList());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrimsMinSpanningTree() {
        Graph<Integer, Character> graph = new Graph<Integer, Character>(getImplementation(), false);


        graph.addVertices(Arrays.asList('a', 'b', 'c', 'd', 'e'));

        graph.addEdge('a', 'e', 1);
        graph.addEdge('c', 'd', 2);
        graph.addEdge('a', 'b', 3);
        graph.addEdge('b', 'e', 4);
        graph.addEdge('b', 'c', 5);
        graph.addEdge('e', 'c', 6);
        graph.addEdge('e', 'd', 7);

        try {
            Iterable<Edge<Integer, Character>> actual = new MinimumSpanningTree.Prims<>(graph).find();

            // sum of the minimum tree weights
            int expected = 11;

            Assert.assertEquals(expected, StreamSupport.stream(actual.spliterator(), false).mapToInt(Edge::getLabel).sum());
        } catch (GraphDirectedException | GraphCyclicException e) {
            Assert.assertTrue(false);
        }
    }


    @Test
    public void testKruskalsMinSpanningTree() {
        Graph<Integer, Character> graph = new Graph<Integer, Character>(getImplementation(), false);

        graph.addVertices(Arrays.asList('a', 'b', 'c', 'd', 'e'));

        graph.addEdge('a', 'e', 1);
        graph.addEdge('c', 'd', 2);
        graph.addEdge('a', 'b', 3);
        graph.addEdge('b', 'e', 4);
        graph.addEdge('b', 'c', 5);
        graph.addEdge('e', 'c', 6);
        graph.addEdge('e', 'd', 7);

        // see kruskal wiki for the expected tree

        try {
            Iterable<Edge<Integer, Character>> actual = new MinimumSpanningTree.Kruskals<>(graph).find();

            // sum of the minimum tree weights
            int expected = 11;

            Assert.assertEquals(expected, StreamSupport.stream(actual.spliterator(), false).mapToInt(Edge::getLabel).sum());
        } catch (GraphDirectedException | GraphCyclicException e) {
            Assert.assertTrue(false);
        }
    }
}
