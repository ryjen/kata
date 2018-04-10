#ifndef CODA_KATA_LIST_ITEM_H
#define CODA_KATA_LIST_ITEM_H

#include <stdlib.h>

typedef void (*CodaListDestroyCallback)(void *);

typedef void *(*CodaListAllocCallback)(size_t);

typedef int (*CodaListCompareCallback)(const void *, const void *, size_t);

typedef void *(*CodaListCopyCallback)(void *, const void *, size_t);

typedef struct __coda_list_item CodaListItem;

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
CodaListItem *coda_list_item_new(void *data, size_t size, CodaListCompareCallback comparator);

/**
 * creates a list item suitable for adding to a list
 * this implementation will not destroy, copy or compare memory
 * @param  data the memory to use as a list item
 * @param  size the size of the data in bytes
 * @param  comparator the compare function, can be NULL
 * @return      a list item
 */
CodaListItem *coda_list_item_new_static(void *data, size_t size, CodaListCompareCallback comparator);

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
CodaListItem *coda_list_item_new_transient(void *data, size_t size, CodaListCompareCallback comparator,
                                           CodaListAllocCallback allocator, CodaListDestroyCallback destructor,
                                           CodaListCopyCallback copier);

/**
 * destroy a list item
 * will call the destructor function on the data if set
 * @param item the item instance
 */
void coda_list_item_delete(CodaListItem *item);


/**
 * gets the data associated with a list item
 * @param item the list item with data
 * @return the list item data
 */
void *coda_list_item_data(const CodaListItem *item);


/**
 * gets the size of the data associated with a list item
 * @param item the list item with data
 * @return the size of the item data
 */
size_t coda_list_item_size(const CodaListItem *item);

/**
 * copies and item.  the allocator and copy functions will be called if set, otherwise the pointer will be assigned
 * @param  item the item instance
 * @return      a copy of the item
 */
CodaListItem *coda_list_item_copy(const CodaListItem *item);

/**
 * compares two items.  the compare function must be set on the item, otherwise memcmp will be used
 * @param  item the item instance
 * @param  data the data to compare
 * @return      zero if equal
 */
int coda_list_item_compare(const CodaListItem *item, const void *data);

#endif
