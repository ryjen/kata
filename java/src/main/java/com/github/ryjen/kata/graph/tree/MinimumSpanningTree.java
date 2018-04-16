package com.github.ryjen.kata.graph.tree;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;
import com.github.ryjen.kata.graph.model.Edge;

import java.util.*;

/**
 * Created by ryan on 2017-04-18.
 */
public abstract class MinimumSpanningTree<E extends Comparable<E>, V extends Comparable<V>> {

    final Graph<E, V> graph;

    MinimumSpanningTree(Graph<E, V> graph) throws GraphDirectedException, GraphCyclicException {
        assert graph != null;
        this.graph = graph;
        if (graph.isDirected()) {
            throw new GraphDirectedException();
        }
        if (graph.isCyclic()) {
            throw new GraphCyclicException(true);
        }
    }

    /**
     * finds the minimum spanning tree for the graph
     *
     * @return the set of vertexes and edges that makes the minimum spanning tree
     */
    public abstract Iterable<Edge<E, V>> find();

    public static class Prims<E extends Comparable<E>, V extends Comparable<V>> extends MinimumSpanningTree<E, V> {

        public Prims(Graph<E, V> graph) throws GraphDirectedException, GraphCyclicException {
            super(graph);
        }

        public Iterable<Edge<E, V>> find() {
            Iterator<V> it = graph.vertices().iterator();

            if (!it.hasNext()) {
                return Collections.emptySet();
            }
            return find(it.next());
        }

        public Iterable<Edge<E, V>> find(V vertex) {

            if (vertex == null) {
                return null;
            }

            Set<Edge<E, V>> result = new HashSet<>();

            Set<V> reached = new HashSet<>();

            reached.add(vertex);

            while (reached.size() < graph.size()) {
                Edge<E, V> min = findNextPriority(reached);

                if (min == null) {
                    break;
                }

                reached.addAll(min.getEndpoints());
                result.add(min);
            }

            return result;
        }

        // TODO: improve
        private Edge<E, V> findNextPriority(Set<V> reached) {
            Edge<E, V> min = null;

            for (V vertex : reached) {
                for (Edge<E, V> adj : graph.edges(vertex)) {

                    if (!reached.containsAll(adj.getEndpoints()) && (min == null || min.getLabel().compareTo(adj.getLabel()) > 0)) {
                        min = adj;
                    }
                }
            }
            return min;
        }

    }

    public static class Kruskals<E extends Comparable<E>, V extends Comparable<V>> extends MinimumSpanningTree<E, V> {

        final DisjointedSet<V> sets;
        final PriorityQueue<Edge<E, V>> queue;

        public Kruskals(Graph<E, V> graph) throws GraphDirectedException, GraphCyclicException {
            super(graph);

            sets = new DisjointedSet<>();

            queue = new PriorityQueue<>(Edge::compareTo);
        }

        @Override
        public Iterable<Edge<E, V>> find() {

            Set<Edge<E,V>> result = new TreeSet<>(Edge::compareTo);

            for (V v : graph.vertices()) {
                sets.makeSet(v);
            }

            for (Edge<E, V> endpoint : graph.edges()) {
                queue.offer(endpoint);
            }

            while (!queue.isEmpty() && result.size() < graph.numberOfEdges() - 1) {
                Edge<E, V> edge = queue.poll();

                Set<V> a = sets.find(edge.getFrom());

                if (a == null) {
                    continue;
                }

                for (V v : edge.getEndpoints()) {

                    Set<V> b = sets.find(v);

                    if (b == null) {
                        continue;
                    }

                    if (!a.containsAll(b)) {
                        if (sets.union(a, b)) {
                            result.add(edge);
                        }
                    }
                }
            }

            return result;
        }
    }

}
