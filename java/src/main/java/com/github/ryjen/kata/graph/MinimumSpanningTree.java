package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.exceptions.GraphConnectivityException;
import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;
import com.github.ryjen.kata.graph.model.Endpoint;

import java.util.*;

/**
 * Created by ryan on 2017-04-18.
 */
public abstract class MinimumSpanningTree<Vertex extends Comparable<Vertex>> {

    final Graph<Vertex> graph;

    MinimumSpanningTree(Graph<Vertex> graph) throws GraphDirectedException {
        assert graph != null;
        this.graph = graph;
        if (graph.isDirected()) {
            throw new GraphDirectedException();
        }
    }

    public Graph<Vertex> find() throws GraphConnectivityException, GraphCyclicException, GraphDirectedException {
        Iterator<Vertex> it = graph.vertices().iterator();

        if (!it.hasNext()) {
            return graph.emptyClone();
        }
        return find(it.next());
    }

    /**
     * finds the minimum spanning tree for the graph
     *
     * @param v the starting vertex
     * @return the graph that makes the minimum spanning tree
     */
    public abstract Graph<Vertex> find(Vertex v);

    public static class Prims<Vertex extends Comparable<Vertex>> extends MinimumSpanningTree<Vertex> implements Comparator<Vertex> {

        private final Map<Vertex, Integer> priorities;

        private final PriorityQueue<Vertex> queue;

        public Prims(Graph<Vertex> graph) throws GraphDirectedException {
            super(graph);
            priorities = new HashMap<>();
            queue = new PriorityQueue<>(this);
            for (Vertex v : graph.vertices()) {
                priorities.put(v, Integer.MAX_VALUE);
            }
        }

        public Graph<Vertex> find(Vertex vertex) {

            if (vertex == null) {
                return null;
            }

            priorities.put(vertex, 0);

            for (Vertex v : graph.vertices()) {
                queue.offer(v);
            }

            Graph<Vertex> tree = graph.emptyClone();

            while (!queue.isEmpty()) {

                Vertex next = queue.poll();

                tree.addVertex(next);

                if (next != null && next != vertex) {
                    tree.addEdge(vertex, next, graph.getEdge(vertex, next));
                }

                vertex = next;

                for (Endpoint<Vertex> adj : graph.endpoints(vertex)) {

                    if (queue.contains(adj.getVertex()) &&
                            priorities.get(adj.getVertex()) > adj.getEdge().getWeight()) {

                        // remove greater priority from queue
                        queue.remove(adj.getVertex());

                        // set new priority
                        priorities.put(adj.getVertex(), adj.getEdge().getWeight());

                        // re-add less priority
                        queue.offer(adj.getVertex());
                    }
                }
            }

            return tree;
        }

        @Override
        public int compare(Vertex o1, Vertex o2) {
            return priorities.getOrDefault(o1, Integer.MAX_VALUE).compareTo(priorities.get(o2));
        }
    }

}
