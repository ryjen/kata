#include <assert.h>
#include <ryjen/kata/graphs/adjacency-matrix.h>
#include <ryjen/kata/graphs/edge.h>
#include <ryjen/kata/graphs/vertex.h>
#include <stdlib.h>
#include "internal.h"

#define CODA_ADJ_MATRIX_DIRECTED (1 << 0)

struct __coda_adj_matrix {
    int flags;
    CodaGraphVertex *vertices;
    unsigned num_vertices;
    CodaGraphEdge **matrix;
};

CodaAdjMatrix *coda_adj_matrix_new_with_flags(int flags)
{
    CodaAdjMatrix *graph = malloc(sizeof(CodaAdjMatrix));
    assert(graph != NULL);
    graph->flags = flags;
    graph->vertices = NULL;
    graph->matrix = NULL;
    graph->num_vertices = 0;

    return graph;
}

void __coda_adj_matrix_resize(CodaAdjMatrix *graph, unsigned size)
{
    assert(graph != NULL);

    if (graph->matrix == NULL) {
        graph->matrix = calloc(size, sizeof(CodaGraphEdge));
        assert(graph->matrix != NULL);
        for (int i = 0; i < size; i++) {
            graph->matrix[i] = calloc(size, sizeof(CodaGraphEdge));
            assert(graph->matrix[i] != NULL);
        }
        return;
    }

    if (size >= graph->num_vertices) {
        graph->matrix = realloc(graph->matrix, size * sizeof(CodaGraphEdge));
        for (int i = graph->num_vertices; i < size; i++) {
            graph->matrix[i] = calloc(size, sizeof(CodaGraphEdge));
        }
    }
}

void coda_adj_matrix_add_edge(CodaAdjMatrix *graph, CodaGraphVertex *v1, CodaGraphVertex *v2, CodaGraphEdge *edge)
{
    assert(graph != NULL && v1 != NULL && v2 != NULL && edge != NULL);
}
