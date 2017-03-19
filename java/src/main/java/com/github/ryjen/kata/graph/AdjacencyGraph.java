package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.NoSuchVertexException;
import com.github.ryjen.kata.graph.exceptions.VertexLimitException;
import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.formatters.GraphSimpleFormatter;
import com.github.ryjen.kata.graph.formatters.GraphVertexFormatter;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;
import com.github.ryjen.kata.graph.search.BreadthFirstSearch;
import com.github.ryjen.kata.graph.search.DepthFirstSearch;
import com.github.ryjen.kata.graph.search.Search;

import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * Created by ryan on 2017-03-18.
 * A graph implementation using an adjacency matrix
 */
public class AdjacencyGraph<Vertex extends Comparable<Vertex>> implements Graph<Vertex> {
    public static final int NOT_FOUND = -1;
    private final Edge[][] edges;
    private final boolean directed;
    private final int size;
    private final Vertex[] vertices;
    private final Factory factory;
    private final Comparator<Vertex> comparator;

    /**
     * creates an undirected graph
     *
     * @param factory the factor to create objects
     * @param size    the size of the graph
     */
    public AdjacencyGraph(Factory<Vertex> factory, int size) {
        this(factory, size, false);
    }

    /**
     * constructs a graph
     *
     * @param factory  the factory to create objects
     * @param size     the size of the number of vertices 0..size-1
     * @param directed true if this is a directed graph
     */
    public AdjacencyGraph(Factory<Vertex> factory, int size, boolean directed) {
        assert size > 0;
        assert factory != null;
        this.factory = factory;
        this.size = size;
        this.comparator = factory.createComparator();
        this.vertices = factory.createVertices(size);
        edges = new Edge[size][size];
        for (int i = 0; i < size; i++) {
            edges[i] = new Edge[size];
            for (int j = 0; j < size; j++) {
                edges[i][j] = factory.emptyEdge();
            }
        }
        this.directed = directed;
    }

    private boolean equals(Vertex a, Vertex b) {
        if (a == null || b == null) {
            return false;
        }
        if (comparator != null) {
            return comparator.compare(a, b) == 0;
        }
        return a.compareTo(b) == 0;
    }

    @Override
    public Iterable<Iterable<Edge>> edges() {
        return new GraphEdgeIterator(this);
    }

    /**
     * adds an edge between two vertices
     *
     * @param v    the vertical vertex
     * @param u    the upper vertex
     * @param edge the edge to set
     */
    public void addEdge(Vertex v, Vertex u, Edge edge) {
        assert v != null;
        assert u != null;

        int index1 = indexOf(v);

        if (index1 == NOT_FOUND) {
            throw new NoSuchVertexException();
        }

        int index2 = indexOf(u);

        if (index2 == NOT_FOUND) {
            throw new NoSuchVertexException();
        }

        edges[index1][index2] = edge;

        if (!directed) {
            edges[index2][index1] = edge;
        }
    }

    /**
     * adds a default edge between two vertices
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     */
    @Override
    public void addEdge(Vertex v, Vertex u) {
        addEdge(v, u, factory.createEdge());
    }

    /**
     * gets the index of a vertex
     *
     * @param vertex the vertex
     * @return the index of the vertex or NOT_FOUND
     */
    int indexOf(Vertex vertex) {
        for (int i = 0; i < size; i++) {
            if (equals(vertices[i], vertex)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * adds a vertex to the graph
     *
     * @param vertex the vertex
     * @throws VertexLimitException if there is no more room in the graph
     */
    @Override
    public void addVertex(Vertex vertex) {
        for (int i = 0; i < size; i++) {
            if (vertices[i] == null) {
                vertices[i] = vertex;
                return;
            }
            if (equals(vertices[i], vertex)) {
                return;
            }
        }

        throw new VertexLimitException();
    }

    /**
     * adds a list of vertices to the graph
     *
     * @param vertices the vertices to add
     * @throws VertexLimitException if there is no more room in the graph
     */
    public void addVertices(Vertex... list) {
        for (Vertex v : list) {
            addVertex(v);
        }
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        assert vertex != null;

        int index = indexOf(vertex);

        if (index != NOT_FOUND) {
            vertices[index] = null;
            for (int i = 0; i < size; i++) {
                if (isEdge(i, index)) {
                    edges[i][index] = factory.emptyEdge();
                }
                if (isEdge(index, i)) {
                    edges[index][i] = factory.emptyEdge();
                }
            }
            return true;
        }

        return false;
    }


    /**
     * gets the size of the graph
     *
     * @return the size of the graph
     */
    public int size() {
        return this.size;
    }

    /**
     * tests if two vertices have an edge
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     * @return true if there is an edge between vertices
     */
    public Edge getEdge(Vertex v, Vertex u) {
        assert v != null;
        assert u != null;

        return getEdge(indexOf(v), indexOf(u));
    }

    /**
     * tests if two vertices have an edge
     *
     * @param v the vertical index
     * @param u the upper index
     * @return the edge between two vertices
     */
    Edge getEdge(int v, int u) {
        assert v >= 0 && v < size;
        assert u >= 0 && u < size;

        return edges[v][u];
    }

    Vertex getVertex(int v) {
        assert v >= 0 && v < size;

        return vertices[v];
    }

    /**
     * tests if there is a non-empty edge between two vertices
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     * @return true if there is a non-empty edge
     */
    public boolean isEdge(Vertex v, Vertex u) {
        assert v != null;
        assert u != null;

        return isEdge(indexOf(v), indexOf(u));
    }

    /**
     * tests if there is a non-empty edge on two vertices
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     * @return true if there is a non-empty edge
     */
    boolean isEdge(int v, int u) {
        assert v >= 0 && v < size;
        assert u >= 0 && u < size;

        return !edges[v][u].isEmpty();
    }

    /**
     * performs a depth first search
     *
     * @see Search
     */
    @Override
    public void dfs(Search.OnVisit<Vertex> callback) {
        Search<Vertex> dfs = new DepthFirstSearch<>(this, callback);

        dfs.search();
    }

    /**
     * performs a breadth first search
     *
     * @see Search
     */
    @Override
    public void bfs(Search.OnVisit<Vertex> callback) {
        Search<Vertex> bfs = new BreadthFirstSearch<>(this, callback);

        bfs.search();
    }

    /**
     * gets an iterator to the list of vertices
     *
     * @return an iterator of vertices
     */
    @Override
    public Iterable<Vertex> vertices() {
        return new GraphVertexIterator<>(this);
    }

    /**
     * gets an iterable the the adjacent vertices for a vertex
     *
     * @param v the vertex to find adjacent vertices for
     * @return an iterable list of adjacent vertices
     */
    @Override
    public Iterable<Vertex> adjacent(Vertex v) {
        return new AdjacentVertexIterator<>(this, indexOf(v));
    }

    /**
     * gets the degree of connections to a vertex
     *
     * @param vertex     the vertex
     * @param comparator how to test the graph for connections
     * @return the degree of the vertex
     */
    private int degree(Vertex vertex, BiFunction<Integer, Integer, Boolean> comparator) {

        assert vertex != null;
        assert comparator != null;

        int index = indexOf(vertex);

        assert index >= 0 && index < size;

        int count = 0;

        for (int i = 0; i < size; i++) {

            if (i == index) {
                continue;
            }

            if (comparator.apply(index, i)) {
                count++;
            }
        }

        return count;
    }

    /**
     * gets the number of connections going out from a vertex
     *
     * @param vertex the vertex
     * @return the degree
     */
    public int outDegree(Vertex vertex) {
        return degree(vertex, (v, u) -> isEdge(v, u));
    }

    /**
     * gets the number of connections coming in from a vertex
     *
     * @param vertex the vertex
     * @return the degree
     */
    public int inDegree(Vertex vertex) {
        return degree(vertex, (v, u) -> isEdge(u, v));
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
     * tests if this graph is directed or undirected
     *
     * @return true if the graph is directed
     */
    public boolean isDirected() {
        return directed;
    }

    @Override
    public String toString() {
        return toString(new GraphVertexFormatter<>(this));
    }

    /**
     * gets a simple representation of the graph as a string
     *
     * @return the formatted string
     */
    public String toSimpleString() {
        return toString(new GraphSimpleFormatter(this));
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
}
