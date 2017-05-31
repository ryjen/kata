#include <assert.h>
#include <rj/graphs/adjacency-matrix.h>
#include <rj/graphs/edge.h>
#include <rj/graphs/vertex.h>
#include <stdlib.h>
#include "private.h"

#define RJ_ADJ_MATRIX_DIRECTED (1 << 0)

struct __adj_matrix {
    int flags;
    RJGraphVertex *vertices;
    unsigned num_vertices;
    RJGraphEdge **matrix;
};

RJAdjMatrix *rj_adj_matrix_create_with_flags(int flags)
{
    RJAdjMatrix *graph = malloc(sizeof(RJAdjMatrix));
    assert(graph != NULL);
    graph->flags = flags;
    graph->vertices = NULL;
    graph->matrix = NULL;
    graph->num_vertices = 0;

    return graph;
}

void __rj_adj_matrix_resize(RJAdjMatrix *graph, unsigned size)
{
    assert(graph != NULL);

    if (graph->matrix == NULL) {
        graph->matrix = calloc(size, sizeof(RJGraphEdge));
        assert(graph->matrix != NULL);
        for (int i = 0; i < size; i++) {
            graph->matrix[i] = calloc(size, sizeof(RJGraphEdge));
            assert(graph->matrix[i] != NULL);
        }
        return;
    }

    if (size >= graph->num_vertices) {
        graph->matrix = realloc(graph->matrix, size * sizeof(RJGraphEdge));
        for (int i = graph->num_vertices; i < size; i++) {
            graph->matrix[i] = calloc(size, sizeof(RJGraphEdge));
        }
    }
}

void rj_adj_matrix_add_edge(RJAdjMatrix *graph, RJGraphVertex *v1, RJGraphVertex *v2, RJGraphEdge *edge)
{
    assert(graph != NULL && v1 != NULL && v2 != NULL && edge != NULL);
}
