
#include <assert.h>
#include <setjmp.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <cmocka.h>

#include <coda/life/algorithms/math.h>

static void test_coda_gcd_valid(void **state)
{
    assert_int_equal(coda_gcd(60, 24), 12);
    assert_int_equal(coda_gcd(123, 32), 1);
}

static void test_coda_gcd_invalid(void **state)
{
    assert_int_not_equal(coda_gcd(123, -32), 0);
}


int run_euclid_tests()
{
    const struct CMUnitTest valid_tests[] = {cmocka_unit_test_setup_teardown(test_coda_gcd_valid, NULL, NULL)};

    const struct CMUnitTest invalid_tests[] = {cmocka_unit_test_setup_teardown(test_coda_gcd_invalid, NULL, NULL)};

    int rval = cmocka_run_group_tests_name("euclid valid tests", valid_tests, NULL, NULL);

    if (rval) {
        return rval;
    }

    return cmocka_run_group_tests_name("euclid invalid tests", invalid_tests, NULL, NULL);
}
