#ifndef RJ_KATA_LIST_ITEM_H
#define RJ_KATA_LIST_ITEM_H

#include <stdlib.h>

typedef void (*RJListDestroyCallback)(void *);
typedef void *(*RJListAllocCallback)(size_t);
typedef int (*RJListCompareCallback)(const void *, const void *, size_t);
typedef void *(*RJListCopyCallback)(void *, const void *, size_t);

typedef struct __rj_list_item RJListItem;

struct __rj_list_item {
    void *data;
    size_t size;
    RJListAllocCallback allocator;
    RJListDestroyCallback destructor;
    RJListCopyCallback copier;
    RJListCompareCallback comparer;
};

/**
 * creates a list item suitable for adding to a list
 * this implementation will use the stdlib methods for dealing with memory:
 *    destroy = free
 *    copy = memmove
 *    compare = memcmp
 * @param  data the memory to use as a list item
 * @param  size the size of the data in bytes
 * @param  comparator the compare function, can be null
 * @return      a list item
 */
RJListItem *rj_list_item_create(void *data, size_t size, RJListCompareCallback comparator);

/**
 * creates a list item suitable for adding to a list
 * this implementation will not destroy, copy or compare memory
 * @param  data the memory to use as a list item
 * @param  size the size of the data in bytes
 * @param  comparator the compare function, can be NULL
 * @return      a list item
 */
RJListItem *rj_list_item_create_static(void *data, size_t size, RJListCompareCallback comparator);

/**
 * creates a list item suitable for adding to a list
 * this implementation you can pass functions to deal with the memory
 * @param  data       the memory to use as a list item
 * @param  size       the size of the data in bytes
 * @param  allocator  the function to allocate new memory (malloc)
 * @param  destructor the function to destroy the memory (free)
 * @param  copier     the function to copy the memory (memmove)
 * @param  comparator the function to compare the memory (memcpy)
 * @return            a list item
 */
RJListItem *rj_list_item_create_transient(void *data, size_t size, RJListCompareCallback comparator,
                                          RJListAllocCallback allocator, RJListDestroyCallback destructor,
                                          RJListCopyCallback copier);

/**
 * destroy a list item
 * will call the destructor function on the data if set
 * @param item the item instance
 */
void rj_list_item_destroy(RJListItem *item);

/**
 * copies and item.  the allocator and copy functions will be called if set, otherwise the pointer will be assigned
 * @param  item the item instance
 * @return      a copy of the item
 */
RJListItem *rj_list_item_copy(const RJListItem *item);

/**
 * compares two items.  the compare function must be set on the item, otherwise memcmp will be used
 * @param  item the item instance
 * @param  data the data to compare
 * @return      zero if equal
 */
int rj_list_item_compare(const RJListItem *item, const void *data);

#endif
