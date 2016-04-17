
#include <assert.h>
#include <setjmp.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>

#include <cmocka.h>

#include "collections/list.h"

static int create_test_list(void **state)
{
    a3list *list = a3list_create();
    *state = list;

    return 0;
}

static a3list_item *random_list_item()
{
    size_t size = sizeof(int);
    int *data = (int *)malloc(size);
    assert(data != NULL);
    *data = rand() % 1000;
    return a3list_item_create(data, size);
}

static int create_and_populate_test_list(void **state)
{
    a3list *list = a3list_create();

    a3list_add(list, random_list_item());

    a3list_add(list, random_list_item());

    a3list_add(list, random_list_item());

    *state = list;

    return 0;
}

static int destroy_test_list(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_destroy(list);

    return 0;
}

static void test_list_add_valid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_add(list, random_list_item());

    assert_int_equal(a3list_size(list), 1);
}

static void test_list_add_invalid(void **state)
{
    a3list *list = (a3list *)*state;
    a3list_item *item = random_list_item();

    a3list_add(NULL, NULL);

    a3list_add(NULL, item);

    assert_int_equal(a3list_size(list), 0);

    a3list_item_destroy(item);
}

static void test_list_add_index_valid(void **state)
{
    a3list *list = (a3list *)*state;

    size_t size = a3list_size(list);

    a3list_item *item = random_list_item();

    void *data = NULL;

    assert_int_equal(size, 3);

    a3list_add_index(list, 1, item);

    assert_int_equal(a3list_size(list), 4);

    data = a3list_get(list, 2);

    assert_non_null(data);

    assert_int_equal(a3list_item_compare(item, data), 0);
}

static void test_list_add_index_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    size_t size = a3list_size(list);

    a3list_item *item = random_list_item();

    void *data = NULL;

    assert_int_equal(size, 3);

    a3list_add_index(list, 100, item);

    a3list_add_index(NULL, 1, item);

    assert_int_equal(a3list_size(list), 3);

    a3list_item_destroy(item);
}

static void test_list_add_all_valid(void **state)
{
    a3list *list = (a3list *)*state;

    void *data = a3list_get(list, 1);

    a3list *other = a3list_create();

    a3list_add(other, random_list_item());

    assert_int_equal(a3list_size(other), 1);

    a3list_add_all(other, list);

    assert_int_equal(a3list_size(other), a3list_size(list) + 1);

    assert_int_not_equal(a3list_contains(other, data), 0);

    a3list_destroy(other);
}

static void test_list_add_all_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_add_all(list, NULL);

    a3list_add_all(NULL, list);

    assert_int_equal(a3list_size(list), 3);
}

static void test_list_add_all_index_valid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list *other = a3list_create();

    a3list_item *item = random_list_item();

    size_t size = a3list_size(list);

    a3list_add(other, item);

    a3list_add(other, random_list_item());

    assert_int_equal(a3list_size(other), 2);

    a3list_add_all_index(list, 1, other);

    assert_int_equal(a3list_size(list), a3list_size(other) + size);

    assert_int_not_equal(a3list_contains(list, item->data), 0);

    a3list_destroy(other);
}
static void test_list_add_all_index_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list *other = a3list_create();

    a3list_add(other, random_list_item());

    a3list_add_all_index(list, 100, other);

    a3list_add_all_index(NULL, 1, other);

    assert_int_equal(a3list_size(list), 3);

    a3list_destroy(other);
}

static void test_list_clear_valid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_equal(a3list_size(list), 3);

    a3list_clear(list);

    assert_int_equal(a3list_size(list), 0);
}
static void test_list_clear_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_clear(NULL);
}

static void test_list_contains_valid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_item *item = random_list_item();

    a3list_add(list, item);

    assert_int_equal(a3list_size(list), 4);

    assert_int_not_equal(a3list_contains(list, item->data), 0);
}

static void test_list_contains_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_item *item = random_list_item();

    assert_int_equal(a3list_contains(list, item->data), 0);

    assert_int_equal(a3list_contains(list, NULL), 0);

    assert_int_equal(a3list_contains(NULL, item->data), 0);

    assert_int_equal(a3list_contains(NULL, NULL), 0);
}

static void test_list_contains_all_valid(void **state)
{
    a3list *list = (a3list *)*state;

    size_t size = a3list_size(list);

    a3list *other = a3list_create();

    a3list_add(other, random_list_item());
    a3list_add(other, random_list_item());

    a3list_add_all(list, other);

    assert_int_equal(a3list_size(list), size + a3list_size(other));

    assert_int_not_equal(a3list_contains_all(list, other), 0);

    a3list_destroy(other);
}

static void test_list_contains_all_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    size_t size = a3list_size(list);

    a3list *other = a3list_create();

    a3list_add(other, random_list_item());
    a3list_add(other, random_list_item());

    assert_int_equal(a3list_contains_all(list, other), 0);

    assert_int_equal(a3list_contains_all(list, NULL), 0);

    assert_int_equal(a3list_contains_all(NULL, other), 0);

    assert_int_equal(a3list_contains_all(NULL, NULL), 0);
}

static void test_list_get_valid(void **state)
{
    a3list *list = (a3list *)*state;

    void *data = a3list_get(list, 1);

    assert_non_null(data);
}
static void test_list_get_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    void *data = a3list_get(list, 100);

    assert_null(data);

    data = a3list_get(NULL, 1);

    assert_null(data);
}

static void test_list_get_size_valid(void **state)
{
    a3list *list = (a3list *)*state;

    size_t size = a3list_get_size(list, 1);

    assert_int_not_equal(size, 0);
}

static void test_list_get_size_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    size_t size = a3list_get_size(list, 100);

    assert_int_equal(size, 0);

    size = a3list_get_size(NULL, 1);

    assert_int_equal(size, 0);
}

static void test_list_remove_valid(void **state)
{
    a3list *list = (a3list *)*state;

    void *data = NULL;

    assert_int_equal(a3list_size(list), 3);

    data = a3list_get(list, 1);

    assert_int_not_equal(a3list_remove(list, data), 0);

    assert_int_equal(a3list_size(list), 2);
}
static void test_list_remove_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_item *item = random_list_item();

    assert_int_equal(a3list_remove(list, item->data), 0);

    assert_int_equal(a3list_size(list), 3);

    assert_int_equal(a3list_remove(NULL, item->data), 0);

    a3list_item_destroy(item);
}

static void test_list_remove_index_valid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_equal(a3list_size(list), 3);

    assert_int_not_equal(a3list_remove_index(list, 1), 0);

    assert_int_equal(a3list_size(list), 2);
}

static void test_list_remove_index_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_equal(a3list_remove_index(list, 100), 0);

    assert_int_equal(a3list_remove_index(NULL, 1), 0);
}

static void test_list_remove_all_valid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list *other = a3list_create();

    a3list_add(other, random_list_item());
    a3list_add(other, random_list_item());

    assert_int_equal(a3list_size(other), 2);

    a3list_add_all(list, other);

    assert_int_equal(a3list_size(list), 5);

    assert_int_equal(a3list_remove_all(list, other), 2);

    assert_int_equal(a3list_size(list), 3);

    a3list_destroy(other);
}
static void test_list_remove_all_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list *other = a3list_create();

    assert_int_equal(a3list_remove_all(list, other), 0);

    assert_int_equal(a3list_size(list), 3);

    assert_int_equal(a3list_remove_all(list, NULL), 0);

    assert_int_equal(a3list_remove_all(NULL, other), 0);

    a3list_destroy(other);
}

static void test_list_index_of_valid(void **state)
{
    a3list *list = (a3list *)*state;

    void *data = a3list_get(list, 1);

    assert_non_null(data);

    assert_int_equal(a3list_index_of(list, data), 1);
}
static void test_list_index_of_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_item *item = random_list_item();

    assert_int_equal(a3list_index_of(list, item->data), -1);

    assert_int_equal(a3list_index_of(NULL, item->data), -1);
}

static void test_list_set_valid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_item *item = random_list_item();

    void *data = NULL;

    assert_int_equal(a3list_size(list), 3);

    a3list_set(list, 1, item);

    assert_int_equal(a3list_size(list), 3);

    data = a3list_get(list, 1);

    assert_int_equal(a3list_item_compare(item, data), 0);
}
static void test_list_set_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    a3list_item *item = random_list_item();

    void *data = NULL;

    assert_int_equal(a3list_size(list), 3);

    a3list_set(list, 100, item);

    assert_int_equal(a3list_size(list), 3);

    data = a3list_get(list, 1);

    assert_int_not_equal(a3list_item_compare(item, data), 0);

    a3list_item_destroy(item);
}

static void test_list_size_valid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_equal(a3list_size(list), 3);

    a3list_add(list, random_list_item());

    assert_int_equal(a3list_size(list), 4);
}
static void test_list_size_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_equal(a3list_size(NULL), 0);
}

static void test_list_is_empty_valid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_equal(a3list_is_empty(list), 0);

    a3list_clear(list);

    assert_int_not_equal(a3list_is_empty(list), 0);
}
static void test_list_is_empty_invalid(void **state)
{
    a3list *list = (a3list *)*state;

    assert_int_not_equal(a3list_is_empty(NULL), 0);
}

int main()
{
    const struct CMUnitTest valid_tests[] = {
        cmocka_unit_test_setup_teardown(test_list_add_valid, create_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_add_index_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_add_all_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_add_all_index_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_clear_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_contains_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_contains_all_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_get_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_get_size_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_remove_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_remove_index_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_remove_all_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_index_of_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_set_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_size_valid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_is_empty_valid, create_and_populate_test_list, destroy_test_list)};

    const struct CMUnitTest invalid_tests[] = {
        cmocka_unit_test_setup_teardown(test_list_add_invalid, create_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_add_index_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_add_all_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_add_all_index_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_clear_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_contains_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_contains_all_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_get_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_get_size_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_remove_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_remove_index_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_remove_all_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_index_of_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_set_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_size_invalid, create_and_populate_test_list, destroy_test_list),
        cmocka_unit_test_setup_teardown(test_list_is_empty_invalid, create_and_populate_test_list, destroy_test_list)};

    int rval = cmocka_run_group_tests_name("valid tests", valid_tests, NULL, NULL);

    if (rval) {
        return rval;
    }

    return cmocka_run_group_tests_name("invalid tests", invalid_tests, NULL, NULL);
}
