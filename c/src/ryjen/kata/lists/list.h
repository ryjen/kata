#ifndef RJ_KATA_LIST_H
#define RJ_KATA_LIST_H

#include <ryjen/kata/lists/list-item.h>

typedef struct __coda_list CodaList;

/**
 * creates a new list
 * @return an allocated list object
 */
CodaList *coda_list_new_single();

/**
 * destroys a created list
 * @param list the list instance
 */
void coda_list_delete(CodaList *list);

/**
 * prepends a list item to the list
 * @param list the list instance
 * @param item the item to add to the list
 * @see rj_list_item_create
 */
void coda_list_add(CodaList *list, CodaListItem *item);

/**
 * adds an item to the list after the specific index
 * @param list  the list instance
 * @param index the index to add after
 * @param item  the item to add
 * @see rj_list_item_create
 */
void coda_list_add_index(CodaList *list, size_t index, CodaListItem *item);

/**
 * adds one list to another
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param other the list to add from
 */
void coda_list_add_all(CodaList *list, const CodaList *other);

/**
 * adds one list to another after the specified index
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param index the index to add after
 * @param other the list to add from
 */
void coda_list_add_all_index(CodaList *list, size_t index, const CodaList *other);

/**
 * removes all items in the list
 * if the items have a destructor set, it will be called
 * @param list the list instance
 */
void coda_list_clear(CodaList *list);

/**
 * tests if a list contains an item
 * items in the list must have a compare function set
 * @param  list the list instance
 * @param  item the item (memory) to check for
 * @return      zero if not found, positive if found
 */
int coda_list_contains(const CodaList *list, const void *item);

/**
 * tests if a list contains all items in another list
 * items in the list must have a compare function set
 * @param  list  the list instance
 * @param  other the list to check
 * @return       zero if nothing found, otherwise the number of items found
 */
int coda_list_contains_all(const CodaList *list, const CodaList *other);

/**
 * gets the data (memory) from a list
 * @param  list  the list instance
 * @param  index the index of the data
 * @return       bytes of the item
 * @see rj_list_get_size to get the size of the data
 */
void *coda_list_get(const CodaList *list, size_t index);

/**
 * removes an item from a list
 * items must have a compare function set
 * @param  list the list instance
 * @param  item the item to remove
 * @return      zero if nothing was removed, otherwise a positive value
 */
int coda_list_remove(CodaList *list, const void *item);

/**
 * removes an index from a list
 * @param  list  the list instance
 * @param  index the index to remove
 * @return       positive value if the index was removed
 */
int coda_list_remove_index(CodaList *list, size_t index);

/**
 * removes a list from a list
 * items must have a compare function set
 * @param  list  the list instance
 * @param  other the list to remove
 * @return       zero if nothing removed, otherwise the number of items removed
 */
int coda_list_remove_all(CodaList *list, const CodaList *other);

/**
 * gets the index of an item in a list
 * @param  list the list instance
 * @param  item the item to find
 * @return      the index of the item in the list
 */
int coda_list_index_of(const CodaList *list, const void *item);

/**
 * sets (replaces) an item in a list.  the existing item will be destroyed.
 * @param list  the list instance
 * @param index the index to set
 * @param item  the item to set
 */
void coda_list_set(CodaList *list, size_t index, CodaListItem *item);

/**
 * gets the size of a list
 * @param  list the list instance
 * @return      the size (number of items)
 */
size_t coda_list_size(const CodaList *list);

/**
 * tests if a list is empty
 * @param  list the list instance
 * @return      a positive value if the list is empty, otherwise zero
 */
int coda_list_is_empty(const CodaList *list);

/**
 * sorts the list based on the comparator
 * NOTE: the implementation is subject to change
 * @param  list the list instance
 */
void coda_list_sort(CodaList *list);

typedef enum { CodaListIterateNext, CodaListIteratorBreak, CodaListIteratorDelete } CodaListCallbackReturn;

typedef CodaListCallbackReturn (*CodaListCallback)(CodaList *list, size_t index, CodaListItem *node);

/**
 * iterates a list for each item
 * @param list the list to iterator
 * @param callback the callback for each item
 */
void coda_list_for_each(CodaList *list, CodaListCallback callback);

#endif
