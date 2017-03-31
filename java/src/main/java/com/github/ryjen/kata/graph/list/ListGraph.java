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
import java.util.stream.Collectors;

/**
 * Created by ryanjennings on 2017-03-20.
 */
public class ListGraph<Vertex extends Comparable<Vertex>> implements AdjacencyGraph<Vertex> {
    private final Map<Vertex, Set<Entry<Vertex>>> vertexList;
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

    private static <T extends Comparable<T>> Set<Entry<T>> createAdjacencySet() {
        return new LinkedHashSet<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        if (vertexList.containsKey(vertex)) {
            return;
        }

        vertexList.put(vertex, createAdjacencySet());
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

            for (Set<Entry<Vertex>> list : vertexList.values()) {
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
    public void addEdge(Vertex a, Vertex b) {
        addEdge(a, b, factory.createEdge());
    }

    @Override
    public void addEdge(Vertex a, Vertex b, Edge edge) {
        assert a != null;
        assert b != null;

        Set<Entry<Vertex>> list;

        if (!vertexList.containsKey(a)) {
            list = createAdjacencySet();
            vertexList.put(a, list);
        } else {
            list = vertexList.get(a);
        }

        list.add(new Entry(b, edge));

        if (!isDirected()) {
            if (!vertexList.containsKey(b)) {
                list = createAdjacencySet();
                vertexList.put(b, list);
            } else {
                list = vertexList.get(b);
            }
            list.add(new Entry(a, edge));
        }
    }

    @Override
    public boolean isEdge(Vertex a, Vertex b) {
        assert a != null;
        assert b != null;

        boolean value = false;

        if (vertexList.containsKey(a)) {
            Set<Entry<Vertex>> list = vertexList.get(a);
            value = list.stream().anyMatch(e -> e.getVertex() == b);
        }

        if (!isDirected() && !value && vertexList.containsKey(b)) {
            Set<Entry<Vertex>> list = vertexList.get(b);
            value = list.stream().anyMatch(e -> e.getVertex() == a);
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
        return vertexList.get(v).stream().map(e -> e.getVertex()).collect(Collectors.toSet());
    }

    @Override
    public Iterable<Vertex> vertices() {
        return vertexList.keySet();
    }

    @Override
    public Iterable<Edge> edges() {
        return entries().stream().map(e -> e.getEdge()).collect(Collectors.toList());
    }

    Set<Entry<Vertex>> entries() {
        return vertexList.values().stream().flatMap(e -> e.stream()).collect(Collectors.toSet());
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

        for (Set<Entry<Vertex>> list : vertexList.values()) {
            if (list.stream().anyMatch(e -> e.getVertex() == vertex)) {
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
