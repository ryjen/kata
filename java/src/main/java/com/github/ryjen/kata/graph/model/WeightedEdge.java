package com.github.ryjen.kata.graph.model;

/**
 * Created by ryan on 2017-04-18.
 */
public class WeightedEdge extends Edge {
    private int weight;

    public WeightedEdge(int weight) {
        this.weight = weight;
    }
    @Override
    public String display() {
        return String.valueOf(getWeight());
    }

    @Override
    public boolean isEmpty() {
        return weight <= 0;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
