package com.github.ryjen.kata.graph.list;

import com.github.ryjen.kata.graph.AdjacencyGraph;
import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.formatters.ListFormatter;
import com.github.ryjen.kata.graph.model.DefaultFactory;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;
import com.github.ryjen.kata.graph.search.BreadthFirstSearch;
import com.github.ryjen.kata.graph.search.DepthFirstSearch;
import com.github.ryjen.kata.graph.search.Search;

import java.util.*;

/**
 * Created by ryanjennings on 2017-03-20.
 */
public class ListGraph<Vertex extends Comparable<Vertex>> implements AdjacencyGraph<Vertex> {
    private final Map<Vertex, Set<Vertex>> vertexList;
    private final Factory<Vertex> factory;
    private final boolean directed;

    public ListGraph() {
        this(new DefaultFactory<>(), false);
    }

    public ListGraph(boolean directed) {
        this(new DefaultFactory<>(), false);
    }

    public ListGraph(Factory<Vertex> factory) {
        this(factory, false);
    }

    public ListGraph(Factory<Vertex> factory, boolean directed) {
        assert factory != null;

        this.vertexList = new LinkedHashMap<>();
        this.factory = factory;
        this.directed = directed;

        List<Vertex> initial = factory.initialVertices();

        if (initial != null) {
            for (Vertex v : initial) {
                addVertex(v);
            }
        }
    }

    private static <T extends Comparable<T>> Set<T> createAdjacencySet() {
        return new LinkedHashSet<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        if (vertexList.containsKey(vertex)) {
            return;
        }

        vertexList.put(vertex, new LinkedHashSet<>());
    }

    @Override
    public void addVertices(Vertex... list) {
        for (Vertex v : list) {
            addVertex(v);
        }
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        if (vertexList.containsKey(vertex)) {
            vertexList.remove(vertex);

            for (Set<Vertex> list : vertexList.values()) {
                list.remove(vertex);
            }
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return vertexList.size();
    }

    @Override
    public void addEdge(Vertex v, Vertex u) {
        assert v != null;
        assert u != null;

        Set<Vertex> list;

        if (!vertexList.containsKey(v)) {
            list = createAdjacencySet();
            vertexList.put(v, list);
        } else {
            list = vertexList.get(v);
        }

        list.add(u);

        if (!isDirected()) {
            if (!vertexList.containsKey(u)) {
                list = createAdjacencySet();
                vertexList.put(u, list);
            } else {
                list = vertexList.get(u);
            }
            list.add(v);
        }
    }

    @Override
    public boolean isEdge(Vertex a, Vertex b) {
        assert a != null;
        assert b != null;

        boolean value = false;

        if (vertexList.containsKey(a)) {
            Set<Vertex> list = vertexList.get(a);
            value = list.contains(b);
        }

        if (!isDirected() && !value && vertexList.containsKey(b)) {
            Set<Vertex> list = vertexList.get(b);
            value = list.contains(a);
        }

        return value;
    }

    @Override
    public Edge getEdge(Vertex a, Vertex b) {
        return isEdge(a, b) ? factory.createEdge() : null;
    }

    @Override
    public Edge getEdgeOrDefault(Vertex a, Vertex b) {
        return isEdge(a, b) ? factory.createEdge() : factory.emptyEdge();
    }

    @Override
    public void dfs(Search.OnVisit<Vertex> callback) {
        Search<Vertex> dfs = new DepthFirstSearch<>(this, callback);
        dfs.search();
    }

    @Override
    public void bfs(Search.OnVisit<Vertex> callback) {
        Search<Vertex> bfs = new BreadthFirstSearch<Vertex>(this, callback);
        bfs.search();
    }

    @Override
    public Iterable<Vertex> adjacent(Vertex v) {
        if (!vertexList.containsKey(v)) {
            return Collections.emptySet();
        }
        return vertexList.get(v);
    }

    @Override
    public Iterable<Vertex> vertices() {
        return vertexList.keySet();
    }

    @Override
    public Iterable<Edge> edges() {
        return new EdgeIterator(this, factory);
    }

    @Override
    public int degree(Vertex vertex) {
        return inDegree(vertex) + outDegree(vertex);
    }

    @Override
    public int outDegree(Vertex vertex) {

        if (!vertexList.containsKey(vertex)) {
            return 0;
        }
        return vertexList.get(vertex).size();
    }

    @Override
    public int inDegree(Vertex vertex) {
        int count = 0;

        for (Set<Vertex> list : vertexList.values()) {
            if (list.contains(vertex)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isDirected() {
        return this.directed;
    }

    /**
     * custom toString() with option to show vertices
     *
     * @param formatter how to format the graph
     * @return the string representation
     */
    public String toString(Formatter formatter) {
        StringBuilder buf = new StringBuilder();

        formatter.format(buf);

        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(new ListFormatter<>(this));
    }

}
