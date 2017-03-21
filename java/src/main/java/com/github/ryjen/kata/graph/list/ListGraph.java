package com.github.ryjen.kata.graph.list;

import com.github.ryjen.kata.graph.AdjacencyGraph;
import com.github.ryjen.kata.graph.exceptions.NoSuchVertexException;
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

    public ListGraph(Factory<Vertex> factory, boolean directed) {
        this.vertexList = new HashMap<>();
        this.factory = factory;
        this.directed = directed;
    }

    @Override
    public void addVertex(Vertex vertex) {
        if (vertexList.containsKey(vertex)) {
            return;
        }

        vertexList.put(vertex, new HashSet<>());
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

        if (!vertexList.containsKey(v) || !vertexList.containsKey(u)) {
            throw new NoSuchVertexException();
        }

        Set<Vertex> list1 = vertexList.get(v);
        Set<Vertex> list2 = vertexList.get(u);

        list2.add(v);

        if (isDirected()) {
            list1.add(u);
        }
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
        if (vertexList.containsKey(vertex)) {
            return 0;
        }
        return vertexList.get(vertex).size();
    }

    @Override
    public boolean isDirected() {
        return this.directed;
    }
}
