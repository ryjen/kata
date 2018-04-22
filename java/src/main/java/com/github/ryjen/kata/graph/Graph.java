package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;
import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.formatters.SimpleFormatter;
import com.github.ryjen.kata.graph.list.AdjacencyList;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.search.BreadthFirstSearch;
import com.github.ryjen.kata.graph.search.DepthFirstSearch;
import com.github.ryjen.kata.graph.search.Ordering;
import com.github.ryjen.kata.graph.search.Search;
import com.github.ryjen.kata.graph.sort.TopologicalSort;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * base class for graph implementations
 */
public class Graph<E extends Comparable<E>, V extends Comparable<V>> implements Edgable<E, V>, Vertexable<V>, Degreable<V>, Cloneable {

    private final Graphable<E, V> impl;
    private final boolean directed;

    /**
     * @param impl
     * @param directed
     */
    public Graph(Graphable<E, V> impl, boolean directed) {
        assert impl != null;
        this.directed = directed;
        this.impl = impl;
    }

    public Graph(boolean directed) {
        this(new AdjacencyList<>(), directed);
    }

    public Graph() {
        this(false);
    }

    /**
     * copy constructor
     *
     * @param other the graph to copy from
     */
    public Graph(Graph<E, V> other) {
        this.directed = other.directed;
        this.impl = other.impl;
    }

    @Override
    public Vertexable<V> addVertex(V v) {
        return impl.addVertex(v);
    }

    /**
     * add a list of vertices
     *
     * @param list the list of vertices to add
     */
    @Override
    public Vertexable<V> addVertices(Collection<V> list) {
        for (V v : list) {
            addVertex(v);
        }
        return this;
    }

    @Override
    public boolean removeVertex(V v) {
        return impl.removeVertex(v);
    }

    @Override
    public boolean containsVertex(V v) {
        return impl.containsVertex(v);
    }

    @Override
    public Iterable<V> adjacent(V v) {
        return impl.adjacent(v);
    }

    @Override
    public Iterable<V> vertices() {
        return impl.vertices();
    }

    /**
     * adds an edge with a weight between two vertices
     *
     * @param a     the first vertex
     * @param b     the second vertex
     * @param label the label of the edge
     */
    public void addEdge(V a, V b, E label) {
        addEdge(a, b, new Edge<>(label));
    }

    public int numberOfEdges() {
        return Long.valueOf(StreamSupport.stream(edges().spliterator(), false).count()).intValue();
    }

    public int size() {
        return impl.size();
    }

    /**
     * performs a depth first search
     *
     * @see Search
     */
    public void dfs(V start, Search.OnVisit<V> callback, Ordering ordering) {
        Search<E, V> dfs = new DepthFirstSearch<>(this, callback, ordering);

        dfs.search(start);
    }

    /**
     * performs a breadth first search
     *
     * @see Search
     */
    public void bfs(V start, Search.OnVisit<V> callback) {
        Search<E, V> bfs = new BreadthFirstSearch<>(this, callback);

        bfs.search(start);
    }

    /**
     * tests if this graph is connected (no stray vertices)
     *
     * @return true if all vertices are connected
     */
    public boolean isConnected() {
        Set<V> visited = new HashSet<>();

        int size = impl.size();

        if (size == 0) {
            return false;
        }

        dfs(vertices().iterator().next(), visited::add, Ordering.Pre);

        return visited.size() == size;
    }

    /**
     * tests if this graph is cyclic
     *
     * @return true if one or more vertices are connected in a loop
     */
    public boolean isCyclic() {

        if (!isDirected()) {
            return false;
        }

        try {
            TopologicalSort<E, V> sorter = new TopologicalSort<>(this);

            sorter.sort();

            return false;
        } catch (GraphCyclicException e) {
            return true;
        } catch (GraphDirectedException e) {
            return false;
        }
    }

    /**
     * gets the degree of a vertex
     *
     * @param vertex the vertex
     * @return the degree
     */
    public long degree(V vertex) {
        return impl.inDegree(vertex) + impl.outDegree(vertex);
    }


    /**
     * tests if this graph is directed
     *
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

    @Override
    public void addEdge(V a, V b, Edge<E, V> edge) {
        impl.addEdge(a, b, edge);

        if (!isDirected()) {
            impl.addEdge(b, a, new Edge<>(edge));
        }
    }

    @Override
    public boolean isEdge(V a, V b) {
        return impl.isEdge(a, b);
    }

    @Override
    public Edge<E, V> getEdge(V a, V b) {
        return impl.getEdge(a, b);
    }

    @Override
    public boolean removeEdge(V a, V b) {
        return impl.removeEdge(a, b);
    }

    @Override
    public Iterable<Edge<E, V>> edges() {
        return impl.edges();
    }

    @Override
    public Iterable<Edge<E, V>> edges(V v) {
        return impl.edges(v);
    }

    @Override
    public long inDegree(V vertex) {
        return impl.inDegree(vertex);
    }

    @Override
    public long outDegree(V vertex) {
        return impl.outDegree(vertex);
    }
}
