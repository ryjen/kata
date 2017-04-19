package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.exceptions.NoSuchVertexException;
import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.formatters.VertexFormatter;
import com.github.ryjen.kata.graph.model.DefaultFactory;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by ryan on 2017-03-18.
 * A graph implementation using an adjacency matrix
 */
public class AdjacencyMatrix<Vertex extends Comparable<Vertex>> extends Graph<Vertex> {
    private static final int NOT_FOUND = -1;
    private final Matrix<Edge> edges;
    private final List<Vertex> vertices;
    private final Comparator<Vertex> comparator;

    public AdjacencyMatrix() {
        this(new DefaultFactory<>(), false);
    }

    public AdjacencyMatrix(boolean directed) {
        this(new DefaultFactory<>(), directed);
    }

    /**
     * creates an undirected graph
     *
     * @param factory the factor to create objects
     */
    public AdjacencyMatrix(Factory<Vertex> factory) {
        this(factory, false);
    }

    /**
     * constructs a graph
     *
     * @param factory  the factory to create objects
     * @param directed true if this is a directed graph
     */
    public AdjacencyMatrix(Factory<Vertex> factory, boolean directed) {
        super(factory, directed);
        this.comparator = factory.createComparator();
        List<Vertex> initial = factory.initialVertices();
        if (initial == null) {
            this.vertices = new ArrayList<>();
        } else {
            this.vertices = initial;
        }
        this.edges = new Matrix<>(Edge.class);
    }

    public AdjacencyMatrix(AdjacencyMatrix<Vertex> other) {
        super(other);
        this.comparator = other.comparator;
        this.vertices = new ArrayList<>(other.vertices);
        this.edges = new Matrix<>(other.edges);
    }

    public Graph<Vertex> clone() {
        return new AdjacencyMatrix<>(this);
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
    public Iterable<Edge> edges() {
        return new EdgeIterator(this);
    }

    @Override
    public Iterable<Edge> edges(Vertex v) {
        return new AdjacentEdgeIterator<>(this, indexOf(v));
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

        if (!isDirected()) {

            edges.set(index2, index1, edge);
        }

        edges.set(index1, index2, edge);
    }

    /**
     * removes an edge from a graph
     *
     * @param a - the first vertex
     * @param b - the second vertex
     * @return true if removed
     */
    @Override
    public boolean removeEdge(Vertex a, Vertex b) {
        assert a != null;
        assert b != null;

        boolean rval = true;

        int index1 = indexOf(a);

        if (index1 == NOT_FOUND) {
            throw new NoSuchVertexException();
        }

        int index2 = indexOf(b);

        if (index2 == NOT_FOUND) {
            throw new NoSuchVertexException();
        }

        if (!isDirected()) {
            rval = edges.remove(index2, index1);
        }

        return edges.remove(index1, index2) && rval;
    }

    /**
     * gets the index of a vertex
     *
     * @param vertex the vertex
     * @return the index of the vertex or NOT_FOUND
     */
    private int indexOf(Vertex vertex) {
        for (int i = 0; i < vertices.size(); i++) {
            if (equals(vertices.get(i), vertex)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * adds a vertex to the graph
     *
     * @param vertex the vertex
     */
    @Override
    public void addVertex(Vertex vertex) {
        addVertex(vertex, true);
    }

    private void addVertex(Vertex vertex, boolean resize) {

        if (!vertices.contains(vertex)) {
            vertices.add(vertex);

            if (resize) {
                edges.resize(vertices.size());
            }
        }
    }

    /**
     * adds a list of vertices to the graph
     *
     * @param list the vertices to add
     */
    @Override
    public void addVertices(Vertex... list) {
        for (Vertex v : list) {
            // don't resize on each vertex
            addVertex(v, false);
        }
        edges.resize(vertices.size());
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        assert vertex != null;

        int index = indexOf(vertex);

        if (index != NOT_FOUND) {
            int size = vertices.size();
            if (vertices.remove(vertex)) {
                for (int i = 0; i < size; i++) {
                    if (edges.get(i, index) != null) {
                        edges.remove(i, index);
                    }
                    if (edges.get(index, i) != null) {
                        edges.remove(index, i);
                    }
                }
                edges.resize(vertices.size());
                return true;
            }
        }
        return false;
    }

    /**
     * gets the size of the graph
     *
     * @return the size of the graph
     */
    @Override
    public int size() {
        return vertices.size();
    }

    /**
     * tests if two vertices have an edge
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     * @return true if there is an edge between vertices
     */
    @Override
    public Edge getEdge(Vertex v, Vertex u) {
        assert v != null;
        assert u != null;

        return getEdgeByIndices(indexOf(v), indexOf(u));
    }

    Edge getEdgeByIndices(int v, int u) {
        return edges.get(v, u);
    }

    Vertex getVertexByRow(int v) {
        assert v >= 0 && v < size();

        return vertices.get(v);
    }

    /**
     * tests if there is a non-empty edge between two vertices
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     * @return true if there is a non-empty edge
     */
    @Override
    public boolean isEdge(Vertex v, Vertex u) {
        assert v != null;
        assert u != null;

        return isEdgeByRowColumn(indexOf(v), indexOf(u));
    }

    /**
     * tests if there is a non-empty edge on two vertices
     *
     * @param v the vertical vertex
     * @param u the upper vertex
     * @return true if there is a non-empty edge
     */
    boolean isEdgeByRowColumn(int v, int u) {
        return edges.get(v, u) != null;
    }

    /**
     * gets an iterator to the list of vertices
     *
     * @return an iterator of vertices
     */
    @Override
    public Iterable<Vertex> vertices() {
        return new VertexIterator<>(this);
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

        if (index == NOT_FOUND) {
            return 0;
        }

        int count = 0;

        for (int i = 0; i < size(); i++) {

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
        return degree(vertex, (v, u) -> isEdgeByRowColumn(v, u));
    }

    /**
     * gets the number of connections coming in from a vertex
     *
     * @param vertex the vertex
     * @return the degree
     */
    public int inDegree(Vertex vertex) {
        return degree(vertex, (v, u) -> isEdgeByRowColumn(u, v));
    }

    @Override
    public String toString() {
        return toString(new VertexFormatter<>(this));
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
