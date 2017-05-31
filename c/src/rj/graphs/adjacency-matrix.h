#ifndef RJ_KATA_ADJACENCY_MATRIX_H
#define RJ_KATA_ADJACENCY_MATRIX_H

typedef struct __adj_matrix RJAdjMatrix;

RJAdjMatrix *rj_adj_matrix_create();

RJAdjMatrix *rj_adj_matrix_create_with_flags(int directed);


#endif
