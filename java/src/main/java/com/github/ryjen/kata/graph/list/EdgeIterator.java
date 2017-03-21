package com.github.ryjen.kata.graph.list;

import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by ryanjennings on 2017-03-20.
 */
public class EdgeIterator<Vertex extends Comparable<Vertex>> implements Iterator<Edge>, Iterable<Edge> {

    private final ListGraph<Vertex> graph;
    private final Iterator<Vertex> x;
    private final Factory<Vertex> factory;
    private Iterator<Vertex> y;

    public EdgeIterator(ListGraph<Vertex> graph, Factory<Vertex> factory) {
        this.graph = graph;
        this.factory = factory;
        this.x = graph.vertices().iterator();
        this.y = Collections.emptyIterator();
    }

    @Override
    public Iterator<Edge> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {

        if (!y.hasNext()) {
            while (x.hasNext()) {
                Vertex vertex = x.next();

                y = graph.adjacent(vertex).iterator();

                if (y.hasNext()) {
                    break;
                }
            }
        }
        return x.hasNext() && y.hasNext();
    }

    @Override
    public Edge next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return factory.createEdge();
    }
}
