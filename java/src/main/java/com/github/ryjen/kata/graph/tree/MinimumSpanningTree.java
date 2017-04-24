package com.github.ryjen.kata.graph.tree;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;
import com.github.ryjen.kata.graph.model.Connection;

import java.util.*;

/**
 * Created by ryan on 2017-04-18.
 */
public abstract class MinimumSpanningTree<Vertex extends Comparable<Vertex>> {

    final Graph<Vertex> graph;

    MinimumSpanningTree(Graph<Vertex> graph) throws GraphDirectedException, GraphCyclicException {
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
     * @return the graph that makes the minimum spanning tree
     */
    public abstract Iterable<Connection<Vertex>> find();

    public static class Prims<Vertex extends Comparable<Vertex>> extends MinimumSpanningTree<Vertex> {

        public Prims(Graph<Vertex> graph) throws GraphDirectedException, GraphCyclicException {
            super(graph);
        }

        public Iterable<Connection<Vertex>> find() {
            Iterator<Vertex> it = graph.vertices().iterator();

            if (!it.hasNext()) {
                return Collections.emptySet();
            }
            return find(it.next());
        }

        public Iterable<Connection<Vertex>> find(Vertex vertex) {

            if (vertex == null) {
                return null;
            }

            Set<Connection<Vertex>> result = new HashSet<>();

            Set<Vertex> reached = new HashSet<>();

            reached.add(vertex);

            while (reached.size() < graph.size()) {
                Connection<Vertex> min = findNextPriority(reached);

                if (min == null) {
                    break;
                }

                reached.add(min.getTo());
                result.add(min);
            }

            return result;
        }

        private Connection<Vertex> findNextPriority(Set<Vertex> reached) {
            Connection<Vertex> min = null;

            for (Vertex vertex : reached) {
                for (Connection<Vertex> adj : graph.connections(vertex)) {

                    if (!reached.contains(adj.getTo()) && (min == null || min.getEdge().getWeight() > adj.getEdge().getWeight())) {
                        min = adj;
                    }
                }
            }
            return min;
        }

    }

    public static class Kruskals<Vertex extends Comparable<Vertex>> extends MinimumSpanningTree<Vertex> {

        final DisjointedSet<Vertex> sets;
        final PriorityQueue<Connection<Vertex>> queue;

        public Kruskals(Graph<Vertex> graph) throws GraphDirectedException, GraphCyclicException {
            super(graph);

            sets = new DisjointedSet<>();

            Comparator<Connection<Vertex>> comparator = Comparator.comparing(Connection::getEdge);
            queue = new PriorityQueue<>(comparator);
        }

        @Override
        public Iterable<Connection<Vertex>> find() {

            Set<Connection<Vertex>> result = new TreeSet<>((o1, o2) -> {
                int edge = o1.getEdge().compareTo(o2.getEdge());

                if (edge == 0) {
                    return o1.getTo().compareTo(o2.getFrom());
                }
                return edge;
            });

            for (Vertex v : graph.vertices()) {
                sets.makeSet(v);
            }

            for (Connection<Vertex> endpoint : graph.connections()) {
                queue.offer(endpoint);
            }

            while (!queue.isEmpty() && result.size() < graph.numberOfEdges() - 1) {
                Connection<Vertex> edge = queue.poll();

                Set<Vertex> a = sets.find(edge.getFrom());

                if (a == null) {
                    continue;
                }

                Set<Vertex> b = sets.find(edge.getTo());

                if (b == null) {
                    continue;
                }

                if (!a.containsAll(b)) {
                    if (sets.union(a, b)) {
                        result.add(new Connection<>(edge.getFrom(), edge.getTo(), edge.getEdge()));
                    }
                }
            }

            return result;
        }
    }

}
