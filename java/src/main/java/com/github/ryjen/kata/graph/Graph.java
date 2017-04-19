package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.GraphIsCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphNotDirectedException;
import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.formatters.SimpleFormatter;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;
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

    private final Factory factory;
    private boolean directed;

    /**
     * default constructor
     * @param directed true if the graph is directed
     */
    protected Graph(Factory<Vertex> factory, boolean directed) {
        this.factory = factory;
        this.directed = directed;
    }

    /**
     * copy constructor
     * @param other
     */
    protected Graph(Graph<Vertex> other) {
        this.factory = other.factory;
        this.directed = other.directed;
    }

    /**
     * clones this instance
     * @return a copy of this graph
     */
    public abstract Graph<Vertex> clone();

    /**
     * add a list of vertices
     * @param list
     */
    public void addVertices(Vertex... list) {
        for (Vertex v : list) {
            addVertex(v);
        }
    }

    public void addEdge(Vertex a, Vertex b) {
        addEdge(a, b, factory.createEdge());
    }

    public Edge getEdgeOrDefault(Vertex v, Vertex b) {
        return isEdge(v, b) ? getEdge(v, b) : factory.emptyEdge();
    }

    /**
     * adds an edge with a weight between two vertices
     * @param a the first vertex
     * @param b the second vertex
     * @param weight the weight of the edge
     */
    public void addEdge(Vertex a, Vertex b, int weight) {
        addEdge(a, b, factory.createEdge(weight));
    }

    /**
     * gets the size of the graph in terms of number of vertices
     * @return the size
     */
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

    /**
     * tests if this graph is connected (no stray vertices)
     * @return true if all vertices are connected
     */
    public boolean isConnected() {
        Set<Vertex> visited = new HashSet<>();

        if (size() == 0) {
            return false;
        }

        dfs(vertices().iterator().next(), value -> visited.add(value), Ordering.Pre);

        return visited.size() == size();
    }

    /**
     * tests if this graph is cyclic
     * @return true if one or more vertices are connected in a loop
     */
    public boolean isCyclic() {

        if (!isDirected()) {
            return false;
        }

        try {
            TopologicalSort<Vertex> sorter = new TopologicalSort<>(this);

            sorter.sort();

            return false;
        } catch (GraphIsCyclicException e) {
            return true;
        } catch (GraphNotDirectedException e) {
            return false;
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


    /**
     * test for the number of connections into a vertex
     * @param vertex the vertex to check
     * @return the number of in connections
     */
    public abstract int inDegree(Vertex vertex);

    /**
     * test for the number of connections coming from a vertex
     * @param vertex the vertex to test
     * @return the number of out connections
     */
    public abstract int outDegree(Vertex vertex);

    /**
     * tests if this graph is directed
     * @return true if is directed
     */
    public boolean isDirected() {
        return directed;
    }


    @Override
    public String toString() {
        return toString(new SimpleFormatter<>(this));
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

    protected Factory<Vertex> getFactory() {
        return factory;
    }
}
