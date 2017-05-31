#include <assert.h>
#include <setjmp.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <cmocka.h>

/**
 * sorts an array by separating a value to the front, keeping
 * the remaining values in order
 * effeciency is O(n^2)
 */
static void sort_value(int *values, int *result, size_t size, int to_sort)
{
    int indexes[size];

    int sort_index = 0;

    memset(indexes, 0, size);

    for (int i = 0; i < size; i++) {
        if (values[i] == to_sort) {
            indexes[i] = sort_index++;

            for (int j = 0; j < i; j++) {
                if (values[j] != to_sort) {
                    indexes[j]++;
                }
            }
        } else {
            indexes[i] = i;
        }
    }

    for (int i = 0; i < size; i++) {
        result[indexes[i]] = values[i];
    }
}

static void test_array_order_value_only(void **state)
{
    int values[] = {1, 4, 0, 5, 3, 0};
    int expected[] = {0, 0, 1, 4, 5, 3};

    const size_t size = sizeof(values) / sizeof(values[0]);

    int result[size];

    sort_value(values, result, size, 0);

    for (int i = 0; i < size; i++) {
        assert_int_equal(expected[i], result[i]);
    }
}

int run_challenge_tests()
{
    const struct CMUnitTest tests[] = {cmocka_unit_test_setup_teardown(test_array_order_value_only, NULL, NULL)};

    return cmocka_run_group_tests_name("list invalid tests", tests, NULL, NULL);
}
