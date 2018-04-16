package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.Graphable;
import com.github.ryjen.kata.graph.Vertexable;
import com.github.ryjen.kata.graph.exceptions.NoSuchVertexException;
import com.github.ryjen.kata.graph.model.Edge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by ryan on 2017-03-18.
 * A graph implementation using an adjacency matrix
 */
public class AdjacencyMatrix<E extends Comparable<E>, V extends Comparable<V>> implements Graphable<E, V> {
    private static final int NOT_FOUND = -1;
    private final Matrix<Edge<E,V>> edges;
    private final List<V> vertices;

    public AdjacencyMatrix() {
        this.vertices = new ArrayList<>();
        this.edges = new Matrix<>();
    }

    public AdjacencyMatrix(AdjacencyMatrix<E, V> other) {
        this.vertices = new ArrayList<>(other.vertices);
        this.edges = new Matrix<>(other.edges);
    }

    @Override
    public Iterable<Edge<E,V>> edges() {
        return new EdgeIterator<>(this);
    }

    @Override
    public Iterable<Edge<E,V>> edges(V v) {
        return new AdjacentEdgeIterator<>(this, v);
    }

    /**
     * adds an edge between two vertices
     *
     * @param v    the vertical vertex
     * @param u    the upper vertex
     * @param edge the edge to set
     */
    public void addEdge(V v, V u, Edge<E,V> edge) {
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

        Edge<E,V> existing = edges.get(index1, index2);

        if (existing == null) {
            edges.set(index1, index2, edge);
            edge.setFrom(v).addVertex(u);
        } else {
            existing.addVertex(u);
        }
    }

    /**
     * removes an edge from a graph
     *
     * @param a - the first vertex
     * @param b - the second vertex
     * @return true if removed
     */
    @Override
    public boolean removeEdge(V a, V b) {
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

        return edges.remove(index1, index2);
    }

    /**
     * gets the index of a vertex
     *
     * @param vertex the vertex
     * @return the index of the vertex or NOT_FOUND
     */
    int indexOf(V vertex) {
        assert vertex != null;

        return vertices.indexOf(vertex);
    }

    /**
     * adds a vertex to the graph
     *
     * @param vertex the vertex
     */
    @Override
    public Vertexable<V> addVertex(V vertex) {
        addVertex(vertex, true);
        return this;
    }

    private void addVertex(V vertex, boolean resize) {
        assert vertex != null;
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
    public Vertexable<V> addVertices(Collection<V> list) {
        for (V v : list) {
            // don't resize on each vertex
            addVertex(v, false);
        }
        edges.resize(vertices.size());
        return this;
    }

    @Override
    public boolean removeVertex(V vertex) {
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

    @Override
    public boolean containsVertex(V vertex) {
        return vertices.contains(vertex);
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
    public Edge<E,V> getEdge(V v, V u) {
        assert v != null;
        assert u != null;

        return getEdgeByIndices(indexOf(v), indexOf(u));
    }

    Edge<E,V> getEdgeByIndices(int v, int u) {
        return edges.get(v, u);
    }

    V getVertexByRow(int v) {
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
    public boolean isEdge(V v, V u) {
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
    public Iterable<V> vertices() {
        return vertices;
    }

    /**
     * gets an iterable the the adjacent vertices for a vertex
     *
     * @param v the vertex to find adjacent vertices for
     * @return an iterable list of adjacent vertices
     */
    @Override
    public Iterable<V> adjacent(V v) {
        return new AdjacentVertexIterator<>(this, v);
    }

    /**
     * gets the degree of connections to a vertex
     *
     * @param vertex     the vertex
     * @param comparator how to test the graph for connections
     * @return the degree of the vertex
     */
    private long degree(V vertex, BiFunction<Integer, Integer, Boolean> comparator) {

        assert vertex != null;
        assert comparator != null;

        int index = indexOf(vertex);

        if (index == NOT_FOUND) {
            return 0;
        }

        long count = 0;

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
    @Override
    public long outDegree(V vertex) {
        return degree(vertex, this::isEdgeByRowColumn);
    }

    /**
     * gets the number of connections coming in from a vertex
     *
     * @param vertex the vertex
     * @return the degree
     */
    @Override
    public long inDegree(V vertex) {
        return degree(vertex, (v, u) -> isEdgeByRowColumn(u, v));
    }

    @Override
    public void clear() {
        vertices.clear();
        edges.clear();
    }
}
