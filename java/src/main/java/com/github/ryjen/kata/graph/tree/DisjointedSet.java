package com.github.ryjen.kata.graph.tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ryan on 2017-04-20.
 */
public class DisjointedSet<Vertex extends Comparable<Vertex>> {

    private final Set<Set<Vertex>> value;

    public DisjointedSet() {
        value = new HashSet<>();
    }


    public void makeSet(Vertex v) {
        Set<Vertex> set = new HashSet<>(1);
        set.add(v);
        value.add(set);
    }

    public int size() {
        return value.size();
    }

    public Set<Vertex> find(Vertex v) {
        for (Set<Vertex> set : value) {
            if (set.contains(v)) {
                return set;
            }
        }
        return null;
    }

    private Set<Vertex> find(Set<Vertex> v) {
        for (Set<Vertex> set : value) {
            if (set.containsAll(v)) {
                return set;
            }
        }
        return null;
    }

    private boolean remove(Set<Vertex> v) {
        Iterator<Set<Vertex>> it = value.iterator();

        while (it.hasNext()) {
            Set<Vertex> list = it.next();

            if (list.containsAll(v)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean union(Set<Vertex> a, Set<Vertex> b) {

        Set<Vertex> x = find(a);

        if (x == null) {
            return false;
        }

        if (!remove(b)) {
            return false;
        }

        x.addAll(b);
        return true;
    }

}
