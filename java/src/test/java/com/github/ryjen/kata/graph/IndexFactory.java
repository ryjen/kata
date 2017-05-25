package com.github.ryjen.kata.graph;


import com.github.ryjen.kata.graph.model.DefaultFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan jennings on 2017-03-20.
 */
public class IndexFactory extends DefaultFactory<Integer> {

    private final int size;

    public IndexFactory(int size) {
        this.size = size;
    }

    @Override
    public List<Integer> initialVertices() {
        List<Integer> value = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            value.add(i);
        }
        return value;
    }
}
