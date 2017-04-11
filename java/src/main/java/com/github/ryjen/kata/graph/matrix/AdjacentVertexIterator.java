package com.github.ryjen.kata.graph.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentVertexIterator<Vertex extends Comparable<Vertex>> implements Iterator<Vertex>, Iterable<Vertex> {

    private final AdjacencyMatrix<Vertex> graph;
    private final Stack<Entry> stack;

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentVertexIterator(AdjacencyMatrix<Vertex> graph, int vertex) {
        assert graph != null;
        assert vertex >= 0 && vertex < graph.size();
        this.graph = graph;
        this.stack = new Stack<>();
        this.stack.add(new Entry(vertex, 0));
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {

        for (Entry entry : stack) {
            if (entry.findNextEdge()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Vertex next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        while (!stack.isEmpty()) {
            Entry entry = stack.pop();

            if (entry.findNextEdge()) {
                Vertex vertex = graph.getVertexByRow(entry.y++);
                stack.push(new Entry(entry.x, entry.y));
                return vertex;
            }
        }

        throw new NoSuchElementException();
    }

    private final class Entry {
        public int x;
        public int y;

        public Entry(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean findNextEdge() {
            while (this.y < graph.size()) {
                if (graph.isEdgeByRowColumn(this.x, this.y)) {
                    return true;
                }
                this.y++;
            }
            return false;
        }
    }
}
