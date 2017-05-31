#include <assert.h>
#include <rj/collections/list.h>
#include "internal.h"

#define assert_vtable(list, fun) assert((list)->vtable->fun != NULL)

#define list_vtable0(list, fun) ((list)->vtable->fun)((list))

#define list_vtable1(list, fun, arg) ((list)->vtable->fun)((list), (arg))

#define list_vtable2(list, fun, arg1, arg2) ((list)->vtable->fun)((list), (arg1), (arg2))

/**
 * creates a new list
 * @return an allocated list object
 */
RJList *rj_list_create_single()
{
    RJList *list = malloc(sizeof(RJList));

    list->vtable = rj_list_single_vtable();

    assert(list->vtable != NULL);

    assert_vtable(list, create);

    list->impl = list_vtable0(list, create);

    return list;
}

/**
 * destroys a created list
 * @param list the list instance
 */
void rj_list_destroy(RJList *list)
{
    assert(list != NULL);

    assert_vtable(list, destroy);

    list_vtable0(list, destroy);

    free(list);
}

/**
 * prepends a list item to the list
 * @param list the list instance
 * @param item the item to add to the list
 * @see rj_list_item_create
 */
void rj_list_add(RJList *list, RJListItem *item)
{
    assert(list != NULL);

    assert_vtable(list, add);

    list_vtable1(list, add, item);
}

/**
 * adds an item to the list after the specific index
 * @param list  the list instance
 * @param index the index to add after
 * @param item  the item to add
 * @see rj_list_item_create
 */
void rj_list_add_index(RJList *list, size_t index, RJListItem *item)
{
    assert(list != NULL);

    assert_vtable(list, add_index);

    list_vtable2(list, add_index, index, item);
}

/**
 * adds one list to another
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param other the list to add from
 */
void rj_list_add_all(RJList *list, const RJList *other)
{
    assert(list != NULL);

    assert_vtable(list, add_all);

    list_vtable1(list, add_all, other);
}

/**
 * adds one list to another after the specified index
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param index the index to add after
 * @param other the list to add from
 */
void rj_list_add_all_index(RJList *list, size_t index, const RJList *other)
{
    assert(list != NULL);

    assert_vtable(list, add_all_index);

    list_vtable2(list, add_all_index, index, other);
}

/**
 * removes all items in the list
 * if the items have a destructor set, it will be called
 * @param list the list instance
 */
void rj_list_clear(RJList *list)
{
    assert(list != NULL);

    assert_vtable(list, clear);

    list_vtable0(list, clear);
}

/**
 * tests if a list contains an item
 * items in the list must have a compare function set
 * @param  list the list instance
 * @param  item the item (memory) to check for
 * @return      zero if not found, positive if found
 */
int rj_list_contains(const RJList *list, const void *item)
{
    assert(list != NULL);

    assert_vtable(list, contains);

    return list_vtable1(list, contains, item);
}

/**
 * tests if a list contains all items in another list
 * items in the list must have a compare function set
 * @param  list  the list instance
 * @param  other the list to check
 * @return       zero if nothing found, otherwise the number of items found
 */
int rj_list_contains_all(const RJList *list, const RJList *other)
{
    assert(list != NULL);

    assert_vtable(list, contains_all);

    return list_vtable1(list, contains_all, other);
}

/**
 * gets the data (memory) from a list
 * @param  list  the list instance
 * @param  index the index of the data
 * @return       bytes of the item
 * @see rj_list_get_size to get the size of the data
 */
void *rj_list_get(const RJList *list, size_t index)
{
    assert(list != NULL);

    assert_vtable(list, get);

    return list_vtable1(list, get, index);
}

/**
 * removes an item from a list
 * items must have a compare function set
 * @param  list the list instance
 * @param  item the item to remove
 * @return      zero if nothing was removed, otherwise a positive value
 */
int rj_list_remove(RJList *list, const void *item)
{
    assert(list != NULL);

    assert_vtable(list, remove);

    return list_vtable1(list, remove, item);
}

/**
 * removes an index from a list
 * @param  list  the list instance
 * @param  index the index to remove
 * @return       positive value if the index was removed
 */
int rj_list_remove_index(RJList *list, size_t index)
{
    assert(list != NULL);

    assert_vtable(list, remove_index);

    return list_vtable1(list, remove_index, index);
}

/**
 * removes a list from a list
 * items must have a compare function set
 * @param  list  the list instance
 * @param  other the list to remove
 * @return       zero if nothing removed, otherwise the number of items removed
 */
int rj_list_remove_all(RJList *list, const RJList *other)
{
    assert(list != NULL);

    assert_vtable(list, remove_all);

    return list_vtable1(list, remove_all, other);
}

/**
 * gets the index of an item in a list
 * @param  list the list instance
 * @param  item the item to find
 * @return      the index of the item in the list
 */
int rj_list_index_of(const RJList *list, const void *item)
{
    assert(list != NULL);

    assert_vtable(list, index_of);

    return list_vtable1(list, index_of, item);
}

/**
 * sets (replaces) an item in a list.  the existing item will be destroyed.
 * @param list  the list instance
 * @param index the index to set
 * @param item  the item to set
 */
void rj_list_set(RJList *list, size_t index, RJListItem *item)
{
    assert(list != NULL);

    assert_vtable(list, set);

    list_vtable2(list, set, index, item);
}

/**
 * gets the size of a list
 * @param  list the list instance
 * @return      the size (number of items)
 */
size_t rj_list_size(const RJList *list)
{
    assert(list != NULL);

    assert_vtable(list, size);

    return list_vtable0(list, size);
}

/**
 * tests if a list is empty
 * @param  list the list instance
 * @return      a positive value if the list is empty, otherwise zero
 */
int rj_list_is_empty(const RJList *list)
{
    assert(list != NULL);

    assert_vtable(list, is_empty);

    return list_vtable0(list, is_empty);
}

/**
 * sorts the list based on the comparator
 * NOTE: the implementation is subject to change
 * @param  list the list instance
 */
void rj_list_sort(RJList *list)
{
    assert(list != NULL);

    assert_vtable(list, sort);

    list_vtable0(list, sort);
}
