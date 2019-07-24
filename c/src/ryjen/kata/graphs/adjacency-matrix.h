#ifndef CODA_KATA_ADJACENCY_MATRIX_H
#define CODA_KATA_ADJACENCY_MATRIX_H

typedef struct __coda_adj_matrix CodaAdjMatrix;

CodaAdjMatrix *coda_adj_matrix_new();

CodaAdjMatrix *coda_adj_matrix_new_with_flags(int directed);


#endif
