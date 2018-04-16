package com.github.ryjen.kata.graph;


import com.github.ryjen.kata.graph.model.SimpleFactory;


/**
 * Created by ryan jennings on 2017-03-20.
 */
public class IndexFactory extends SimpleFactory<Character,Integer> {

    public IndexFactory(Graphable<Character, Integer> impl, int size) {
        super(impl, '●', '○');

        for (int i = 0; i < size; i++) {
            impl.addVertex(i);
        }
    }
}
