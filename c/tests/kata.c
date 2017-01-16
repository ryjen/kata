#include <stdlib.h>

extern int run_euclid_tests();
extern int run_list_tests();

int main(int argc, char *argv[]) {
	int rval = run_euclid_tests();

	if (rval) {
		return rval;
	}

	return run_list_tests();
}