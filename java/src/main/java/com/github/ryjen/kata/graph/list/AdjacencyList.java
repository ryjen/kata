package com.github.ryjen.kata.graph.list;

import com.github.ryjen.kata.graph.Graphable;
import com.github.ryjen.kata.graph.Vertexable;
import com.github.ryjen.kata.graph.exceptions.NoSuchVertexException;
import com.github.ryjen.kata.graph.model.Edge;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ryan jennings on 2017-03-20.
 */
public class AdjacencyList<E extends Comparable<E>, V extends Comparable<V>> implements Graphable<E,V> {
    private final Map<V, Set<Edge<E, V>>> vertexList;

    public AdjacencyList() {
        this.vertexList = new LinkedHashMap<>();
    }

    public AdjacencyList(AdjacencyList<E, V> other) {
        this.vertexList = new LinkedHashMap<>(other.vertexList);
    }

    private static <E extends Comparable<E>, T extends Comparable<T>> Set<Edge<E, T>> createEntrySet() {
        return new HashSet<>();
    }

    @Override
    public Vertexable<V> addVertex(V vertex) {
        if (vertexList.containsKey(vertex)) {
            return this;
        }

        vertexList.put(vertex, createEntrySet());
        return this;
    }

    @Override
    public Vertexable<V> addVertices(Collection<V> list) {
        for (V v : list) {
            addVertex(v);
        }
        return this;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (vertexList.containsKey(vertex)) {
            vertexList.remove(vertex);

            for (Set<Edge<E, V>> list : vertexList.values()) {
                for (Edge<E,V> edge : list) {
                    if (edge.containsVertex(vertex)) {
                        edge.removeVertex(vertex);
                    }
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
    public void addEdge(V a, V b, Edge<E,V> edge) {
        assert a != null;
        assert b != null;

        if (!vertexList.containsKey(a) || !vertexList.containsKey(b)) {
            throw new NoSuchVertexException();
        }

        Set<Edge<E,V>> list = vertexList.get(a);

        Optional<Edge<E,V>> existing = list.stream().filter(e -> Objects.equals(e.getLabel(), edge.getLabel())).findFirst();
        if (!existing.isPresent()) {
            edge.setFrom(a).addVertex(b);
            list.add(edge);
        } else {
            existing.get().addVertex(b);
        }
    }

    @Override
    public boolean removeEdge(V a, V b) {
        assert a != null;
        assert b != null;

        if (!vertexList.containsKey(a)) {
            throw new NoSuchVertexException();
        }

        Set<Edge<E,V>> list = vertexList.get(a);

        return list.removeIf(e -> e.getEndpoints().contains(b));
    }

    @Override
    public boolean isEdge(V a, V b) {
        assert a != null;
        assert b != null;

        boolean value = false;

        if (vertexList.containsKey(a)) {
            Set<Edge<E, V>> list = vertexList.get(a);
            value = list.stream().anyMatch(e -> e.getEndpoints().contains(b));
        }

        return value;
    }

    @Override
    public Edge<E,V> getEdge(V a, V b) {
        if (vertexList.containsKey(a)) {
            Set<Edge<E, V>> list = vertexList.get(a);
            Optional<Edge<E, V>> edge = list.stream().filter(e -> e.getEndpoints().contains(b)).findFirst();
            return edge.orElse(null);
        }
        return null;
    }

    @Override
    public Iterable<V> adjacent(V v) {
        if (!vertexList.containsKey(v)) {
            return Collections.emptySet();
        }
        return vertexList.get(v).stream().map(Edge::getEndpoints).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Iterable<V> vertices() {
        return vertexList.keySet();
    }

    @Override
    public boolean containsVertex(V v) {
        return vertexList.containsKey(v);
    }

    @Override
    public Iterable<Edge<E,V>> edges() {
        return endpoints();
    }

    @Override
    public Iterable<Edge<E,V>> edges(V v) {
        if (!vertexList.containsKey(v)) {
            return Collections.emptySet();
        }
        return vertexList.get(v);
    }

    private Collection<Edge<E,V>> endpoints() {
        return vertexList.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public long outDegree(V vertex) {

        if (!vertexList.containsKey(vertex)) {
            return 0;
        }
        return vertexList.get(vertex).stream().map(Edge::getEndpoints).mapToInt(Collection::size).sum();
    }

    @Override
    public long inDegree(V vertex) {
        return vertexList.entrySet().stream().filter(e -> e.getKey() != vertex).map(Map.Entry::getValue)
                .flatMap(Collection::stream).map(Edge::getEndpoints)
                .flatMap(Collection::stream).filter(e -> e == vertex).count();
    }

    @Override
    public void clear() {
        vertexList.clear();
    }
}
