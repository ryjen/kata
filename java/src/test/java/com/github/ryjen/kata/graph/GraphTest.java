package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.formatters.ListFormatter;
import com.github.ryjen.kata.graph.formatters.SimpleFormatter;
import com.github.ryjen.kata.graph.formatters.VertexFormatter;
import com.github.ryjen.kata.graph.list.ListGraph;
import com.github.ryjen.kata.graph.matrix.MatrixGraph;
import com.github.ryjen.kata.graph.model.DefaultFactory;
import com.github.ryjen.kata.graph.model.Factory;
import com.github.ryjen.kata.graph.search.Search;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ryan on 2017-03-18.
 */
public class GraphTest {

    private final static boolean GraphType = true;

    private static <T extends Comparable<T>> Graph<T> newGraph(Factory<T> factory, boolean directed) {
        if (GraphType) {
            return new ListGraph(factory, directed);
        } else {
            return new MatrixGraph(factory, directed);
        }
    }

    private static <T extends Comparable<T>> Graph<T> newGraph() {
        return newGraph(new DefaultFactory<T>(), false);
    }

    public static void main(String[] args) {

        MatrixGraph<Integer> graph = new MatrixGraph<>(new IndexFactory(5));

        System.out.println("Test 1:\n");

        graph.addEdge(1, 0);
        graph.addEdge(3, 0);
        graph.addEdge(3, 2);
        graph.addEdge(2, 1);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(4, 3);

        System.out.println(graph);

        graph = new MatrixGraph<>(new IndexFactory(4));

        System.out.println("Test 2:\n");

        graph.addEdge(1, 0);
        graph.addEdge(2, 1);
        graph.addEdge(3, 2);

        System.out.println(graph);
    }

    @Test
    public void testAddEdgeUndirected() {
        Graph<Integer> graph = newGraph(new IndexFactory(4), false);
        Assert.assertFalse(graph.isDirected());
        graph.addEdge(1, 2);
        Assert.assertTrue(graph.isEdge(1, 2));
        Assert.assertTrue(graph.isEdge(2, 1));
    }

    @Test
    public void testAddEdgeDirected() {
        Graph<Integer> graph = newGraph(new IndexFactory(4), true);
        Assert.assertTrue(graph.isDirected());
        graph.addEdge(1, 2);
        Assert.assertTrue(graph.isEdge(1, 2));
        Assert.assertFalse(graph.isEdge(2, 1));
    }

    @Test
    public void testMatrixToStringUndirected() {
        MatrixGraph<Integer> graph = new MatrixGraph(new IndexFactory(4), false);

        Assert.assertFalse(graph.isDirected());

        graph.addEdge(1, 2);
        graph.addEdge(0, 3);

        StringBuilder buf = new StringBuilder();
        buf.append("○ ○ ○ ● \n");
        buf.append("○ ○ ● ○ \n");
        buf.append("○ ● ○ ○ \n");
        buf.append("● ○ ○ ○ \n");

        String actual = graph.toSimpleString();
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringVertices() {
        Graph<Character> graph = newGraph();

        graph.addVertices('A', 'B', 'C', 'D', 'Z');

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
        Graph<Integer> graph = newGraph(new IndexFactory(4), true);

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
        Graph<Integer> graph = newGraph(new IndexFactory(4), true);

        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(0, 3);

        StringBuilder buf = new StringBuilder();
        buf.append("0 → 3\n");
        buf.append("1 → 2, 4\n");
        buf.append("2 → \n");
        buf.append("3 → \n");

        String actual = graph.toString(new ListFormatter<>(graph));
        String expected = buf.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDegreeUndirected() {
        Graph<Integer> graph = newGraph(new IndexFactory(4), false);

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
        Graph<Integer> graph = newGraph(new IndexFactory(4), true);

        graph.addEdge(1, 2);

        Assert.assertEquals(1, graph.degree(1));

        Assert.assertEquals(0, graph.inDegree(1));

        Assert.assertEquals(1, graph.outDegree(1));

        graph.addEdge(3, 1);

        Assert.assertEquals(2, graph.degree(1));

        Assert.assertEquals(1, graph.inDegree(1));

        Assert.assertEquals(1, graph.outDegree(1));

        graph.addEdge(1, 0);

        Assert.assertEquals(3, graph.degree(1));

        Assert.assertEquals(1, graph.inDegree(1));

        Assert.assertEquals(2, graph.outDegree(1));

        // no connection to vertex 1
        graph.addEdge(2, 0);

        Assert.assertEquals(3, graph.degree(1));

        graph.addEdge(2, 1);

        Assert.assertEquals(4, graph.degree(1));

        Assert.assertEquals(2, graph.inDegree(1));

        Assert.assertEquals(2, graph.outDegree(1));
    }

    @Test
    public void testVertexFormatting() {
        Graph<String> graph = newGraph();

        graph.addVertices("Hello", "World", "Robot", "Unnecessary", "Point");

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
    public void testSearch() {

        final Graph<Integer> graph = newGraph(new IndexFactory(8), false);

        final List<Integer> results = new ArrayList<>();

        graph.addEdge(1, 0);
        graph.addEdge(2, 0);
        graph.addEdge(3, 1);
        graph.addEdge(3, 2);
        graph.addEdge(4, 0);
        graph.addEdge(5, 1);
        graph.addEdge(5, 4);
        graph.addEdge(6, 2);
        graph.addEdge(6, 4);
        graph.addEdge(7, 3);
        graph.addEdge(7, 5);
        graph.addEdge(7, 6);

        Search.OnVisit<Integer> callback = vertex -> results.add(vertex);

        graph.dfs(callback);

        List<Integer> expected = Arrays.asList(0, 1, 3, 2, 6, 4, 5, 7);

        Assert.assertEquals(expected, results);

        results.clear();

        graph.bfs(callback);

        expected = Arrays.asList(0, 1, 2, 4, 3, 5, 6, 7);

        Assert.assertEquals(expected, results);
    }
}
