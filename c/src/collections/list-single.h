#ifndef ARG3_KATA_LIST_SINGLE_H_
#define ARG3_KATA_LIST_SINGLE_H_

/**
 * a singly linked list
 */
typedef struct a3list a3list;

/**
 * creates a new list
 * @return an allocated list object
 */
a3list *a3list_create();

/**
 * destroys a created list
 * @param list the list instance
 */
void a3list_destroy(a3list *list);

/**
 * prepends a list item to the list
 * @param list the list instance
 * @param item the item to add to the list
 * @see a3list_item_create
 */
void a3list_add(a3list *list, a3list_item *item);

/**
 * adds an item to the list after the specific index
 * @param list  the list instance
 * @param index the index to add after
 * @param item  the item to add
 * @see a3list_item_create
 */
void a3list_add_index(a3list *list, size_t index, a3list_item *item);

/**
 * adds one list to another
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param other the list to add from
 */
void a3list_add_all(a3list *list, const a3list *other);

/**
 * adds one list to another after the specified index
 * if the items in the other list have an allocator and a copier,
 * then a copy will be made.
 * @param list  the list instance
 * @param index the index to add after
 * @param other the list to add from
 */
void a3list_add_all_index(a3list *list, size_t index, const a3list *other);

/**
 * removes all items in the list
 * if the items have a destructor set, it will be called
 * @param list the list instance
 */
void a3list_clear(a3list *list);

/**
 * tests if a list contains an item
 * items in the list must have a compare function set
 * @param  list the list instance
 * @param  item the item (memory) to check for
 * @return      zero if not found, positive if found
 */
int a3list_contains(const a3list *list, const void *item);

/**
 * tests if a list contains all items in another list
 * items in the list must have a compare function set
 * @param  list  the list instance
 * @param  other the list to check
 * @return       zero if nothing found, otherwise the number of items found
 */
int a3list_contains_all(const a3list *list, const a3list *other);

/**
 * gets the data (memory) from a list
 * @param  list  the list instance
 * @param  index the index of the data
 * @return       bytes of the item
 * @see a3list_get_size to get the size of the data
 */
void *a3list_get(const a3list *list, size_t index);

/**
 * gets the size of the data from a list
 * @param  list  the list instance
 * @param  index the index of the data
 * @return       the size of the data at the given index
 */
size_t a3list_get_size(const a3list *list, size_t index);

/**
 * removes an item from a list
 * items must have a compare function set
 * @param  list the list instance
 * @param  item the item to remove
 * @return      zero if nothing was removed, otherwise a positive value
 */
int a3list_remove(a3list *list, const void *item);

/**
 * removes an index from a list
 * @param  list  the list instance
 * @param  index the index to remove
 * @return       positive value if the index was removed
 */
int a3list_remove_index(a3list *list, size_t index);

/**
 * removes a list from a list
 * items must have a compare function set
 * @param  list  the list instance
 * @param  other the list to remove
 * @return       zero if nothing removed, otherwise the number of items removed
 */
int a3list_remove_all(a3list *list, const a3list *other);

/**
 * gets the index of an item in a list
 * @param  list the list instance
 * @param  item the item to find
 * @return      the index of the item in the list
 */
int a3list_index_of(const a3list *list, const void *item);

/**
 * sets (replaces) an item in a list.  the existing item will be destroyed.
 * @param list  the list instance
 * @param index the index to set
 * @param item  the item to set
 */
void a3list_set(a3list *list, size_t index, a3list_item *item);

/**
 * gets the size of a list
 * @param  list the list instance
 * @return      the size (number of items)
 */
size_t a3list_size(const a3list *list);

/**
 * tests if a list is empty
 * @param  list the list instance
 * @return      a positive value if the list is empty, otherwise zero
 */
int a3list_is_empty(const a3list *list);

#endif
