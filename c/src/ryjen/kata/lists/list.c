#include <assert.h>
#include <ryjen/kata/lists/list.h>
#include "internal.h"

#define coda_assert_vtable(list, fun) assert((list)->vtable->fun != NULL)

#define coda_list_vtable0(list, fun) ((list)->vtable->fun)((list))

#define coda_list_vtable1(list, fun, arg) ((list)->vtable->fun)((list), (arg))

#define coda_list_vtable2(list, fun, arg1, arg2) ((list)->vtable->fun)((list), (arg1), (arg2))

/**
 * creates a new list
 * @return an allocated list object
 */
CodaList *coda_list_new_single() {
    CodaList *list = malloc(sizeof(CodaList));

    list->vtable = coda_list_single_vtable();

    assert(list->vtable != NULL);

    coda_assert_vtable(list, create);

    list->impl = coda_list_vtable0(list, create);

    return list;
}

/**
 * destroys a created list
 * @param list the list instance
 */
void coda_list_delete(CodaList *list) {
    assert(list != NULL);

    coda_assert_vtable(list, destroy);

    coda_list_vtable0(list, destroy);

    free(list);
}

/**
 * prepends a list item to the list
 * @param list the list instance
 * @param item the item to add to the list
 * @see rj_list_item_create
 */
void coda_list_add(CodaList *list, CodaListItem *item) {
    assert(list != NULL);

    coda_assert_vtable(list, add);

    coda_list_vtable1(list, add, item);
}

/**
 * adds an item to the list after the specific index
 * @param list  the list instance
 * @param index the index to add after
 * @param item  the item to add
 * @see rj_list_item_create
 */
void coda_list_add_index(CodaList *list, size_t index, CodaListItem *item) {
    assert(list != NULL);

    coda_assert_vtable(list, add_index);

    coda_list_vtable2(list, add_index, index, item);
}

/**
 * adds one list to another
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param other the list to add from
 */
void coda_list_add_all(CodaList *list, const CodaList *other) {
    assert(list != NULL);

    coda_assert_vtable(list, add_all);

    coda_list_vtable1(list, add_all, other);
}

/**
 * adds one list to another after the specified index
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param index the index to add after
 * @param other the list to add from
 */
void coda_list_add_all_index(CodaList *list, size_t index, const CodaList *other) {
    assert(list != NULL);

    coda_assert_vtable(list, add_all_index);

    coda_list_vtable2(list, add_all_index, index, other);
}

/**
 * removes all items in the list
 * if the items have a destructor set, it will be called
 * @param list the list instance
 */
void coda_list_clear(CodaList *list) {
    assert(list != NULL);

    coda_assert_vtable(list, clear);

    coda_list_vtable0(list, clear);
}

/**
 * tests if a list contains an item
 * items in the list must have a compare function set
 * @param  list the list instance
 * @param  item the item (memory) to check for
 * @return      zero if not found, positive if found
 */
int coda_list_contains(const CodaList *list, const void *item) {
    assert(list != NULL);

    coda_assert_vtable(list, contains);

    return coda_list_vtable1(list, contains, item);
}

/**
 * tests if a list contains all items in another list
 * items in the list must have a compare function set
 * @param  list  the list instance
 * @param  other the list to check
 * @return       zero if nothing found, otherwise the number of items found
 */
int coda_list_contains_all(const CodaList *list, const CodaList *other) {
    assert(list != NULL);

    coda_assert_vtable(list, contains_all);

    return coda_list_vtable1(list, contains_all, other);
}

/**
 * gets the data (memory) from a list
 * @param  list  the list instance
 * @param  index the index of the data
 * @return       bytes of the item
 * @see rj_list_get_size to get the size of the data
 */
void *coda_list_get(const CodaList *list, size_t index) {
    assert(list != NULL);

    coda_assert_vtable(list, get);

    return coda_list_vtable1(list, get, index);
}

/**
 * removes an item from a list
 * items must have a compare function set
 * @param  list the list instance
 * @param  item the item to remove
 * @return      zero if nothing was removed, otherwise a positive value
 */
int coda_list_remove(CodaList *list, const void *item) {
    assert(list != NULL);

    coda_assert_vtable(list, remove);

    return coda_list_vtable1(list, remove, item);
}

/**
 * removes an index from a list
 * @param  list  the list instance
 * @param  index the index to remove
 * @return       positive value if the index was removed
 */
int coda_list_remove_index(CodaList *list, size_t index) {
    assert(list != NULL);

    coda_assert_vtable(list, remove_index);

    return coda_list_vtable1(list, remove_index, index);
}

/**
 * removes a list from a list
 * items must have a compare function set
 * @param  list  the list instance
 * @param  other the list to remove
 * @return       zero if nothing removed, otherwise the number of items removed
 */
int coda_list_remove_all(CodaList *list, const CodaList *other) {
    assert(list != NULL);

    coda_assert_vtable(list, remove_all);

    return coda_list_vtable1(list, remove_all, other);
}

/**
 * gets the index of an item in a list
 * @param  list the list instance
 * @param  item the item to find
 * @return      the index of the item in the list
 */
int coda_list_index_of(const CodaList *list, const void *item) {
    assert(list != NULL);

    coda_assert_vtable(list, index_of);

    return coda_list_vtable1(list, index_of, item);
}

/**
 * sets (replaces) an item in a list.  the existing item will be destroyed.
 * @param list  the list instance
 * @param index the index to set
 * @param item  the item to set
 */
void coda_list_set(CodaList *list, size_t index, CodaListItem *item) {
    assert(list != NULL);

    coda_assert_vtable(list, set);

    coda_list_vtable2(list, set, index, item);
}

/**
 * gets the size of a list
 * @param  list the list instance
 * @return      the size (number of items)
 */
size_t coda_list_size(const CodaList *list) {
    assert(list != NULL);

    coda_assert_vtable(list, size);

    return coda_list_vtable0(list, size);
}

/**
 * tests if a list is empty
 * @param  list the list instance
 * @return      a positive value if the list is empty, otherwise zero
 */
int coda_list_is_empty(const CodaList *list) {
    assert(list != NULL);

    coda_assert_vtable(list, is_empty);

    return coda_list_vtable0(list, is_empty);
}

/**
 * sorts the list based on the comparator
 * NOTE: the implementation is subject to change
 * @param  list the list instance
 */
void coda_list_sort(CodaList *list) {
    assert(list != NULL);

    coda_assert_vtable(list, sort);

    coda_list_vtable0(list, sort);
}
