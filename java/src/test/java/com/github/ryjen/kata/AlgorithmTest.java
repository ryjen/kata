package com.github.ryjen.kata;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.search.Dijkstra;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AlgorithmTest {

    @Test
    public void testDijkstra() {
        Graph<Integer,Integer> graph = new Graph<>(true);

        graph.addVertices(Arrays.asList(1,2,3,4,5,6));

        graph.addEdge(1, 2, 7);
        graph.addEdge(1, 3, 9);
        graph.addEdge(1, 6, 14);
        graph.addEdge(2, 3, 10);
        graph.addEdge(2, 4, 15);
        graph.addEdge(3, 4, 11);
        graph.addEdge(3, 6, 2);
        graph.addEdge(6, 5, 9);
        graph.addEdge(5, 4, 6);

        List<Integer> actual = new ArrayList<>();

        Dijkstra<Integer> path = new Dijkstra<>(graph, actual::add);

        path.search(1);

        List<Integer> expected = Arrays.asList(1,2,3,6,5);

        Assert.assertEquals(expected, actual);
    }
}
