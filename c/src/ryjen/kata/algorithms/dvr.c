#include <stdio.h>

struct node {
  unsigned dist[20];
  unsigned from[20];
} rt[10];

char node_name(int n) { return ('A' + n); }

void print_nodes(int sz) {
  int i, j;
  for (i = 0; i < sz; i++) {
    for (j = 0; j < sz; j++) {
      printf("%2d ", rt[i].dist[j]);
    }
    printf("\n");
  }
  printf("\n");
}

int main() {
  int dmat[20][20];
  int n, i, j, k, count = 0, t = 0;
  printf("\nEnter the number of nodes : ");
  scanf("%d", &n);
  printf("\nEnter the cost matrix :\n");
  for (i = 0; i < n; i++)
    for (j = 0; j < n; j++) {
      scanf("%d", &dmat[i][j]);
      dmat[i][i] = 0;
      rt[i].dist[j] = dmat[i][j];
      rt[i].from[j] = j;
    }
  do {
    count = 0;
    printf("\n\niteration %d\n\n", ++t);
    for (i = 0; i < n; i++) {
      for (j = 0; j < n; j++) {
        for (k = 0; k < n; k++) {
          if (rt[i].dist[j] > dmat[i][k] + rt[k].dist[j]) {
            rt[i].dist[j] = rt[i].dist[k] + rt[k].dist[j];
            rt[i].from[j] = k;
            count++;
          }
        }
      }
      print_nodes(n);
    }

  } while (count != 0);
  print_nodes(n);
  printf("\n\n");
}