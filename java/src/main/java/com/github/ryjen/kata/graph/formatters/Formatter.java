package com.github.ryjen.kata.graph.formatters;

/**
 * interface for formatting a graph
 */
public interface Formatter {

    /**
     * formats
     *
     * @param buf the buffer to write to
     */
    void format(StringBuilder buf);
}
