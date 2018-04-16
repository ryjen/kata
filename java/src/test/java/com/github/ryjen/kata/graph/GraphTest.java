package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;
import com.github.ryjen.kata.graph.formatters.ListFormatter;
import com.github.ryjen.kata.graph.formatters.SimpleFormatter;
import com.github.ryjen.kata.graph.formatters.VertexFormatter;
import com.github.ryjen.kata.graph.model.*;
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


    protected abstract <E extends Comparable<E>, V extends Comparable<V>>  Graphable<E,V> getImplementation();

    protected Graph<Character,Integer> newIndexGraph(int size, boolean directed) {
        return new Graph<>(new IndexFactory(getImplementation(), size), directed);
    }

    @Test
    public void testUndirectedAdjacentVertices() {

        Graph<Character,Integer> graph = newIndexGraph(4, false);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(3, 2);

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

        Graph<Character,Integer> graph = newIndexGraph(4, true);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(3, 2);

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
        Graph<Character,Integer> graph = newIndexGraph(4, false);
        Assert.assertFalse(graph.isDirected());
        graph.addEdge(1, 2);
        Assert.assertTrue(graph.isEdge(1, 2));
        Assert.assertTrue(graph.isEdge(2, 1));
    }

    @Test
    public void testAddEdgeDirected() {
        Graph<Character,Integer> graph = newIndexGraph(4, true);
        Assert.assertTrue(graph.isDirected());
        graph.addEdge(1, 2);
        Assert.assertTrue(graph.isEdge(1, 2));
        Assert.assertFalse(graph.isEdge(2, 1));
    }

    @Test
    public void testMatrixToStringUndirected() {
        Graph<Character,Integer> graph = newIndexGraph(4, false);

        Assert.assertFalse(graph.isDirected());

        graph.addEdge(1, 2);
        graph.addEdge(0, 3);

        StringBuilder buf = new StringBuilder();
        buf.append("○ ○ ○ ● \n");
        buf.append("○ ○ ● ○ \n");
        buf.append("○ ● ○ ○ \n");
        buf.append("● ○ ○ ○ \n");

        String actual = graph.toString(new SimpleFormatter<>(graph));
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringVertices() {
        Graph<Character,Character> graph = new Graph<>(new SymbolFactory<Character>(getImplementation()), false);

        graph.addVertices(Arrays.asList('A', 'B', 'C', 'D', 'Z'));

        graph.addEdge('B', 'D');

        StringBuilder buf = new StringBuilder();
        buf.append("  │ A B C D Z \n");
        buf.append("──┼───────────\n");
        buf.append("A │ ○ ○ ○ ○ ○ \n");
        buf.append("B │ ○ ○ ○ ● ○ \n");
        buf.append("C │ ○ ○ ○ ○ ○ \n");
        buf.append("D │ ○ ● ○ ○ ○ \n");
        buf.append("Z │ ○ ○ ○ ○ ○ \n");

        String expected = buf.toString();
        String actual = graph.toString(new VertexFormatter<>(graph));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatrixToStringDirected() {
        Graph<Character,Integer> graph = newIndexGraph(4,true);

        Assert.assertTrue(graph.isDirected());

        graph.addEdge(1, 2);
        graph.addEdge(0, 3);

        StringBuilder buf = new StringBuilder();
        buf.append("○ ○ ○ ● \n");
        buf.append("○ ○ ● ○ \n");
        buf.append("○ ○ ○ ○ \n");
        buf.append("○ ○ ○ ○ \n");

        String actual = graph.toString(new SimpleFormatter<>(graph));
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testListToStringDirected() {
        Graph<Character,Integer> graph = newIndexGraph(5,true);

        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(0, 3);

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
        Graph<Character,Integer> graph = newIndexGraph(4,false);

        graph.addEdge(1, 2);

        Assert.assertEquals(2, graph.degree(1));

        graph.addEdge(3, 1);

        Assert.assertEquals(4, graph.degree(1));

        graph.addEdge(1, 0);

        Assert.assertEquals(6, graph.degree(1));

        // no connection to vertex 1
        graph.addEdge(2, 0);

        Assert.assertEquals(6, graph.degree(1));

        // already added edge
        graph.addEdge(2, 1);

        Assert.assertEquals(6, graph.degree(1));
    }

    @Test
    public void testDegreeDirected() {
        Graph<Character,Integer> graph = newIndexGraph(4,true);

        graph.addEdge(1, 2);

        Assert.assertEquals(0, graph.inDegree(1));

        Assert.assertEquals(1, graph.outDegree(1));

        Assert.assertEquals(1, graph.degree(1));

        graph.addEdge(3, 1);

        Assert.assertEquals(1, graph.inDegree(1));

        Assert.assertEquals(1, graph.outDegree(1));

        Assert.assertEquals(2, graph.degree(1));

        graph.addEdge(1, 0);

        Assert.assertEquals(1, graph.inDegree(1));

        Assert.assertEquals(2, graph.outDegree(1));

        Assert.assertEquals(3, graph.degree(1));

        // no connection to vertex 1
        graph.addEdge(2, 0);

        Assert.assertEquals(3, graph.degree(1));

        graph.addEdge(2, 1);

        Assert.assertEquals(2, graph.inDegree(1));

        Assert.assertEquals(2, graph.outDegree(1));

        Assert.assertEquals(4, graph.degree(1));
    }

    @Test
    public void testVertexFormatting() {
        Graph<Character, String> graph = new Graph<>(new SymbolFactory<String>(getImplementation()), false);

        graph.addVertices(Arrays.asList("Hello", "World", "Robot", "Unnecessary", "Point"));

        graph.addEdge("World", "Robot");

        graph.addEdge("Hello", "Point");

        StringBuilder buf = new StringBuilder();
        buf.append("            │ Hello       World       Robot       Unnecessary Point       \n");
        buf.append("────────────┼─────────────────────────────────────────────────────────────\n");
        buf.append("Hello       │ ○           ○           ○           ○           ●           \n");
        buf.append("World       │ ○           ○           ●           ○           ○           \n");
        buf.append("Robot       │ ○           ●           ○           ○           ○           \n");
        buf.append("Unnecessary │ ○           ○           ○           ○           ○           \n");
        buf.append("Point       │ ●           ○           ○           ○           ○           \n");

        String expected = buf.toString();

        String actual = graph.toString(new VertexFormatter<>(graph));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTopologicalSort() {

        Graph<Character,Integer> graph = newIndexGraph(6,true);

        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        try {
            TopologicalSort<Character,Integer> sorter = new TopologicalSort<>(graph);

            Collection<Integer> sorted = sorter.sort();

            Collection<Integer> expected = Arrays.asList(5, 4, 2, 3, 1, 0);

            Assert.assertEquals(expected, sorted);

        } catch (GraphCyclicException | GraphDirectedException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testDirectAcyclicGraph() {
        Graph<Character,Integer> graph = newIndexGraph(6,true);

        graph.addEdge(4, 2);
        graph.addEdge(2, 4);
        graph.addEdge(1, 5);
        graph.addEdge(3, 0);

        Assert.assertTrue(graph.isCyclic());

        graph.removeEdge(2, 4);

        Assert.assertFalse(graph.isCyclic());
    }


    @Test
    public void testDepthFirstSearchPreOrder() {
        final Graph<Character, Character> g = new Graph<>(new SymbolFactory<Character>(getImplementation()), false);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G'));

        g.addEdge('A', 'B');
        g.addEdge('B', 'D');
        g.addEdge('B', 'F');
        g.addEdge('A', 'C');
        g.addEdge('C', 'G');
        g.addEdge('A', 'E');
        g.addEdge('E', 'F');

        g.dfs('A', actual::add, Ordering.Pre);

        List<Character> expected = Arrays.asList('A', 'E', 'F', 'B', 'D', 'C', 'G');

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testDepthFirstSearchPostOrder() {
        final Graph<Character, Character> g = new Graph<>(new SymbolFactory<Character>(getImplementation()), false);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D'));

        g.addEdge('A', 'B');
        g.addEdge('B', 'D');
        g.addEdge('C', 'D');
        g.addEdge('A', 'C');

        g.dfs('A', actual::add, Ordering.Post);

        List<Character> expected = Arrays.asList('C', 'D', 'B', 'A');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDepthFirstSearchReversePostOrder() {
        final Graph<Character, Character> g = new Graph<>(new SymbolFactory<Character>(getImplementation()), true);

        List<Character> actual = new ArrayList<>();

        g.addVertices(Arrays.asList('A', 'B', 'C', 'D'));

        g.addEdge('A', 'B');
        g.addEdge('A', 'C');
        g.addEdge('B', 'D');
        g.addEdge('C', 'D');

        g.dfs('A', actual::add, Ordering.ReversePost);

        List<Character> expected = Arrays.asList('A', 'C', 'B', 'D');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConnectedGraph() {
        Graph<Character,Integer> graph = newIndexGraph(3,false);

        Assert.assertFalse(graph.isConnected());

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        Assert.assertTrue(graph.isConnected());

    }

    @Test
    public void testAdjacentEdges() {
        Graph<Integer, Integer> graph = new Graph<>(new DefaultFactory<Integer, Integer>(getImplementation()), false);

        graph.addVertices(Arrays.asList(0,1,2,3,4));

        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 1, 1);
        graph.addEdge(3, 4, 3);

        List<Integer> expected = Arrays.asList(1,3,4);

        List<Integer> actual = StreamSupport.stream(graph.edges(1).spliterator(), false).map(Edge::getLabel).sorted().collect(Collectors.toList());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrimsMinSpanningTree() {
        Graph<Integer,Character> graph = new Graph<>(new DefaultFactory<Integer,Character>(getImplementation()), false);


        graph.addVertices(Arrays.asList('a', 'b', 'c', 'd', 'e'));

        graph.addEdge('a', 'e', 1);
        graph.addEdge('c', 'd', 2);
        graph.addEdge('a', 'b', 3);
        graph.addEdge('b', 'e', 4);
        graph.addEdge('b', 'c', 5);
        graph.addEdge('e', 'c', 6);
        graph.addEdge('e', 'd', 7);

        try {
            Iterable<Edge<Integer,Character>> actual = new MinimumSpanningTree.Prims<>(graph).find();

            int expected = 11;

            Assert.assertEquals(expected, StreamSupport.stream(actual.spliterator(), false).mapToInt(Edge::getLabel).sum());
        } catch (GraphDirectedException | GraphCyclicException e) {
            Assert.assertTrue(false);
        }
    }


    @Test
    public void testKruskalsMinSpanningTree() {
        Graph<Integer, Character> graph = new Graph<>(new DefaultFactory<Integer, Character>(getImplementation()), false);

        graph.addVertices(Arrays.asList('a', 'b', 'c', 'd', 'e'));

        graph.addEdge('a', 'e', 1);
        graph.addEdge('c', 'd', 2);
        graph.addEdge('a', 'b', 3);
        graph.addEdge('b', 'e', 4);
        graph.addEdge('b', 'c', 5);
        graph.addEdge('e', 'c', 6);
        graph.addEdge('e', 'd', 7);

        // see kruskal wiki the example

        try {
            Iterable<Edge<Integer, Character>> actual = new MinimumSpanningTree.Kruskals<>(graph).find();

            int expected = 11;

            Assert.assertEquals(expected, StreamSupport.stream(actual.spliterator(), false).mapToInt(Edge::getLabel).sum());
        } catch (GraphDirectedException | GraphCyclicException e) {
            Assert.assertTrue(false);
        }
    }
}
