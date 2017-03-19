package com.github.ryjen.kata.heap;

/**
 * Created by ryan on 2017-03-19.
 */
class Util {

    static int parent(int index) {
        if (index < 0) {
            return 0;
        }
        return (index - 1) / 2;
    }

    static int leftChild(int index) {
        if (index < 0) {
            return 0;
        }
        return (2 * index) + 1;
    }

    static int rightChild(int index) {
        return leftChild(index) + 1;
    }

}
