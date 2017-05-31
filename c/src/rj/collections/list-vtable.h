#ifndef RJ_KATA_LIST_SPEC_H
#define RJ_KATA_LIST_SPEC_H

#include <rj/collections/list.h>

typedef struct __rj_list_vtable RJListVtable;
typedef struct __rj_list RJList;

struct __rj_list_vtable {
    /**
     * creates a new list
     * @return an allocated list object
     */
    void *(*create)();

    /**
     * destroys a created list
     * @param list the list instance
     */
    void (*destroy)(RJList *list);

    /**
     * prepends a list item to the list
     * @param list the list instance
     * @param item the item to add to the list
     * @see rj_list_item_create
     */
    void (*add)(RJList *list, RJListItem *item);

    /**
     * adds all items in a list to the list
     * @param list the list instance
     * @param other the list items to add
     */
    void (*add_all)(RJList *list, const RJList *other);

    /**
     * adds an item to the list after the specific index
     * @param list  the list instance
     * @param index the index to add after
     * @param item  the item to add
     * @see rj_list_item_create
     */
    void (*add_index)(RJList *list, size_t index, RJListItem *item);

    /**
     * adds one list to another after the specified index
     * if the items in the other list have an allocator and a copier,
     * then a copy will be made.
     * @param list  the list instance
     * @param index the index to add after
     * @param other the list to add from
     */
    void (*add_all_index)(RJList *list, size_t index, const RJList *other);

    /**
     * removes all items in the list
     * if the items have a destructor set, it will be called
     * @param list the list instance
     */
    void (*clear)(RJList *list);

    /**
     * tests if a list contains an item
     * items in the list must have a compare function set
     * @param  list the list instance
     * @param  item the item (memory) to check for
     * @return      zero if not found, positive if found
     */
    int (*contains)(const RJList *list, const void *item);

    /**
     * tests if a list contains all items in another list
     * items in the list must have a compare function set
     * @param  list  the list instance
     * @param  other the list to check
     * @return       zero if nothing found, otherwise the number of items found
     */
    int (*contains_all)(const RJList *list, const RJList *other);

    /**
     * gets the data (memory) from a list
     * @param  list  the list instance
     * @param  index the index of the data
     * @return       bytes of the item
     * @see rj_list_get_size to get the size of the data
     */
    void *(*get)(const RJList *list, size_t index);

    /**
     * removes an item from a list
     * items must have a compare function set
     * @param  list the list instance
     * @param  item the item to remove
     * @return      zero if nothing was removed, otherwise a positive value
     */
    int (*remove)(RJList *list, const void *item);

    /**
     * removes an index from a list
     * @param  list  the list instance
     * @param  index the index to remove
     * @return       positive value if the index was removed
     */
    int (*remove_index)(RJList *list, size_t index);

    /**
     * removes a list from a list
     * items must have a compare function set
     * @param  list  the list instance
     * @param  other the list to remove
     * @return       zero if nothing removed, otherwise the number of items removed
     */
    int (*remove_all)(RJList *list, const RJList *other);

    /**
     * gets the index of an item in a list
     * @param  list the list instance
     * @param  item the item to find
     * @return      the index of the item in the list
     */
    int (*index_of)(const RJList *list, const void *item);

    /**
     * sets (replaces) an item in a list.  the existing item will be destroyed.
     * @param list  the list instance
     * @param index the index to set
     * @param item  the item to set
     */
    void (*set)(RJList *list, size_t index, RJListItem *item);

    /**
     * gets the size of a list
     * @param  list the list instance
     * @return      the size (number of items)
     */
    size_t (*size)(const RJList *list);

    /**
     * tests if a list is empty
     * @param  list the list instance
     * @return      a positive value if the list is empty, otherwise zero
     */
    int (*is_empty)(const RJList *list);

    /**
     * sorts the list based on the comparator
     * NOTE: the implementation is subject to change
     * @param  list the list instance
     */
    void (*sort)(RJList *list);

    /**
     * iterates the list
     * @param list the list instance
     * @param callback the iterator callback
     */
    void (*for_each)(RJList *list, RJListCallback callback);
};

#endif
