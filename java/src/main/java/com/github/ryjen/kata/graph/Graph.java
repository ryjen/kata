package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.GraphIsCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphNotDirectedException;
import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.search.BreadthFirstSearch;
import com.github.ryjen.kata.graph.search.DepthFirstSearch;
import com.github.ryjen.kata.graph.search.Ordering;
import com.github.ryjen.kata.graph.search.Search;
import com.github.ryjen.kata.graph.sort.TopologicalSort;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ryan on 2017-03-19.
 */
public abstract class Graph<Vertex extends Comparable<Vertex>> implements Edgable<Vertex>, Vertexable<Vertex>, Cloneable {

    private boolean directed;

    protected Graph(boolean directed) {
        this.directed = directed;
    }

    protected Graph(Graph<Vertex> other) {
        this.directed = other.directed;
    }

    public abstract Graph<Vertex> clone();

    public void addVertices(Vertex... list) {
        for (Vertex v : list) {
            addVertex(v);
        }
    }

    public abstract int size();


    /**
     * performs a depth first search
     *
     * @see Search
     */
    public void dfs(Vertex start, Search.OnVisit<Vertex> callback, Ordering ordering) {
        Search<Vertex> dfs = new DepthFirstSearch<>(this, callback, ordering);

        dfs.search(start);
    }

    /**
     * performs a breadth first search
     *
     * @see Search
     */
    public void bfs(Vertex start, Search.OnVisit<Vertex> callback) {
        Search<Vertex> bfs = new BreadthFirstSearch<>(this, callback);

        bfs.search(start);
    }

    public boolean isConnected() {
        Set<Vertex> visited = new HashSet<>();

        for (Vertex v : vertices()) {
            dfs(v, value -> visited.add(value), Ordering.Pre);
        }

        return visited.size() == size();
    }

    public boolean isCyclic() {

        if (!isDirected()) {
            return false;
        }

        try {
            TopologicalSort<Vertex> sorter = new TopologicalSort<>(this);

            sorter.sort();

            return false;
        } catch (GraphNotDirectedException | GraphIsCyclicException e) {
            return true;
        }
    }

    /**
     * gets the degree of a vertex
     *
     * @param vertex the vertex
     * @return the degree
     */
    public int degree(Vertex vertex) {
        return inDegree(vertex) + outDegree(vertex);
    }


    public abstract int inDegree(Vertex vertex);

    public abstract int outDegree(Vertex vertex);

    public boolean isDirected() {
        return directed;
    }


    public abstract String toString(Formatter formatter);
}
