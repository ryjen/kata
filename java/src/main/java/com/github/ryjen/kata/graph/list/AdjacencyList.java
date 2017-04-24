package com.github.ryjen.kata.graph.list;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.exceptions.NoSuchVertexException;
import com.github.ryjen.kata.graph.formatters.ListFormatter;
import com.github.ryjen.kata.graph.model.Connection;
import com.github.ryjen.kata.graph.model.DefaultFactory;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ryan jennings on 2017-03-20.
 */
public class AdjacencyList<Vertex extends Comparable<Vertex>> extends Graph<Vertex> {
    private final Map<Vertex, Set<Connection<Vertex>>> vertexList;

    public AdjacencyList() {
        this(new DefaultFactory<>(), false);
    }

    public AdjacencyList(boolean directed) {
        this(new DefaultFactory<>(), directed);
    }

    public AdjacencyList(Factory<Vertex> factory) {
        this(factory, false);
    }

    public AdjacencyList(Factory<Vertex> factory, boolean directed) {
        super(factory, directed);

        this.vertexList = new LinkedHashMap<>();

        List<Vertex> initial = factory.initialVertices();

        if (initial != null) {
            for (Vertex v : initial) {
                addVertex(v);
            }
        }
    }

    public AdjacencyList(AdjacencyList<Vertex> other) {
        super(other);
        this.vertexList = new LinkedHashMap<>(other.vertexList);
    }

    private static <T extends Comparable<T>> Set<Connection<T>> createEntrySet() {
        return new HashSet<>();
    }

    @Override
    public Graph<Vertex> clone() {
        return new AdjacencyList<>(this);
    }

    @Override
    public Graph<Vertex> emptyClone() {
        return new AdjacencyList<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        if (vertexList.containsKey(vertex)) {
            return;
        }

        vertexList.put(vertex, createEntrySet());
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        if (vertexList.containsKey(vertex)) {
            vertexList.remove(vertex);

            for (Set<Connection<Vertex>> list : vertexList.values()) {
                if (list.contains(vertex)) {
                    list.remove(vertex);
                }
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
    public void addEdge(Vertex a, Vertex b, Edge edge) {
        assert a != null;
        assert b != null;

        if (!vertexList.containsKey(a)) {
            throw new NoSuchVertexException();
        }

        if (!isDirected()) {
            if (!vertexList.containsKey(b)) {
                throw new NoSuchVertexException();
            }
            Set<Connection<Vertex>> list = vertexList.get(b);
            list.add(new Connection<>(b, a, edge));
        }

        if (!vertexList.containsKey(a)) {
            throw new NoSuchVertexException();
        }

        Set<Connection<Vertex>> list = vertexList.get(a);
        list.add(new Connection<>(a, b, edge));
    }

    @Override
    public boolean removeEdge(Vertex a, Vertex b) {
        assert a != null;
        assert b != null;

        if (!vertexList.containsKey(a)) {
            throw new NoSuchVertexException();
        }

        if (!isDirected()) {
            if (!vertexList.containsKey(b)) {
                throw new NoSuchVertexException();
            }
        }

        for (Set<Connection<Vertex>> list : vertexList.values()) {
            list.removeIf(e -> (e.getTo() == a || e.getTo() == b));
        }

        return true;
    }

    @Override
    public boolean isEdge(Vertex a, Vertex b) {
        assert a != null;
        assert b != null;

        boolean value = false;

        if (vertexList.containsKey(a)) {
            Set<Connection<Vertex>> list = vertexList.get(a);
            value = list.stream().anyMatch(e -> e.getTo() == b);
        }

        if (!isDirected() && !value && vertexList.containsKey(b)) {
            Set<Connection<Vertex>> list = vertexList.get(b);
            value = list.stream().anyMatch(e -> e.getTo() == a);
        }

        return value;
    }

    @Override
    public Edge getEdge(Vertex a, Vertex b) {
        if (vertexList.containsKey(a)) {
            Set<Connection<Vertex>> list = vertexList.get(a);
            Optional<Connection<Vertex>> edge = list.stream().filter(e -> e.getTo().equals(b)).findFirst();
            return edge.isPresent() ? edge.get().getEdge() : null;
        }
        return null;
    }

    @Override
    public Iterable<Vertex> adjacent(Vertex v) {
        if (!vertexList.containsKey(v)) {
            return Collections.emptySet();
        }
        return vertexList.get(v).stream().map(Connection::getTo).collect(Collectors.toList());
    }

    @Override
    public Iterable<Connection<Vertex>> connections(Vertex v) {
        if (!vertexList.containsKey(v)) {
            return Collections.emptySet();
        }
        return vertexList.get(v);
    }

    @Override
    public Iterable<Connection<Vertex>> connections() {
        return vertexList.values().stream().flatMap(Set::stream).collect(Collectors.toList());
    }

    @Override
    public Iterable<Vertex> vertices() {
        return vertexList.keySet();
    }

    @Override
    public Iterable<Edge> edges() {
        return endpoints().stream().map(Connection::getEdge).collect(Collectors.toList());
    }

    @Override
    public Iterable<Edge> edges(Vertex v) {
        if (!vertexList.containsKey(v)) {
            return Collections.emptySet();
        }
        return vertexList.get(v).stream().map(Connection::getEdge).collect(Collectors.toList());
    }

    private Collection<Connection<Vertex>> endpoints() {
        return vertexList.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
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

        for (Set<Connection<Vertex>> list : vertexList.values()) {
            if (list.stream().anyMatch(e -> e.getTo() == vertex)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return toString(new ListFormatter<>(this));
    }

    @Override
    public void clear() {
        vertexList.clear();
    }
}
