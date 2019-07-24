#ifndef CODA_KATA_LIST_VTABLE_H
#define CODA_KATA_LIST_VTABLE_H

#include <ryjen/kata/lists/list.h>

typedef struct coda_list_vtable CodaListVtable;

struct coda_list_vtable {
    /**
     * creates a new list
     * @return an allocated list object
     */
    void *(*create)();

    /**
     * destroys a created list
     * @param list the list instance
     */
    void (*destroy)(CodaList *list);

    /**
     * prepends a list item to the list
     * @param list the list instance
     * @param item the item to add to the list
     * @see rj_list_item_create
     */
    void (*add)(CodaList *list, CodaListItem *item);

    /**
     * adds all items in a list to the list
     * @param list the list instance
     * @param other the list items to add
     */
    void (*add_all)(CodaList *list, const CodaList *other);

    /**
     * adds an item to the list after the specific index
     * @param list  the list instance
     * @param index the index to add after
     * @param item  the item to add
     * @see rj_list_item_create
     */
    void (*add_index)(CodaList *list, size_t index, CodaListItem *item);

    /**
     * adds one list to another after the specified index
     * if the items in the other list have an allocator and a copier,
     * then a copy will be made.
     * @param list  the list instance
     * @param index the index to add after
     * @param other the list to add from
     */
    void (*add_all_index)(CodaList *list, size_t index, const CodaList *other);

    /**
     * removes all items in the list
     * if the items have a destructor set, it will be called
     * @param list the list instance
     */
    void (*clear)(CodaList *list);

    /**
     * tests if a list contains an item
     * items in the list must have a compare function set
     * @param  list the list instance
     * @param  item the item (memory) to check for
     * @return      zero if not found, positive if found
     */
    int (*contains)(const CodaList *list, const void *item);

    /**
     * tests if a list contains all items in another list
     * items in the list must have a compare function set
     * @param  list  the list instance
     * @param  other the list to check
     * @return       zero if nothing found, otherwise the number of items found
     */
    int (*contains_all)(const CodaList *list, const CodaList *other);

    /**
     * gets the data (memory) from a list
     * @param  list  the list instance
     * @param  index the index of the data
     * @return       bytes of the item
     * @see rj_list_get_size to get the size of the data
     */
    void *(*get)(const CodaList *list, size_t index);

    /**
     * removes an item from a list
     * items must have a compare function set
     * @param  list the list instance
     * @param  item the item to remove
     * @return      zero if nothing was removed, otherwise a positive value
     */
    int (*remove)(CodaList *list, const void *item);

    /**
     * removes an index from a list
     * @param  list  the list instance
     * @param  index the index to remove
     * @return       positive value if the index was removed
     */
    int (*remove_index)(CodaList *list, size_t index);

    /**
     * removes a list from a list
     * items must have a compare function set
     * @param  list  the list instance
     * @param  other the list to remove
     * @return       zero if nothing removed, otherwise the number of items removed
     */
    int (*remove_all)(CodaList *list, const CodaList *other);

    /**
     * gets the index of an item in a list
     * @param  list the list instance
     * @param  item the item to find
     * @return      the index of the item in the list
     */
    int (*index_of)(const CodaList *list, const void *item);

    /**
     * sets (replaces) an item in a list.  the existing item will be destroyed.
     * @param list  the list instance
     * @param index the index to set
     * @param item  the item to set
     */
    void (*set)(CodaList *list, size_t index, CodaListItem *item);

    /**
     * gets the size of a list
     * @param  list the list instance
     * @return      the size (number of items)
     */
    size_t (*size)(const CodaList *list);

    /**
     * tests if a list is empty
     * @param  list the list instance
     * @return      a positive value if the list is empty, otherwise zero
     */
    int (*is_empty)(const CodaList *list);

    /**
     * sorts the list based on the comparator
     * NOTE: the implementation is subject to change
     * @param  list the list instance
     */
    void (*sort)(CodaList *list);

    /**
     * iterates the list
     * @param list the list instance
     * @param callback the iterator callback
     */
    void (*for_each)(CodaList *list, CodaListCallback callback);
};

#endif
