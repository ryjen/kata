#include <stdlib.h>

int gcd(int m, int n) {
	
	if (m <= 0 || n <= 0) {
		return -1;
	}
	while (n > 0) {
		int r = m % n;
		m = n;
		n = r;
	}
  return m;
}

