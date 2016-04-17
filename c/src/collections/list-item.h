#ifndef ARG3_KATA_LIST_ITEM_H_
#define ARG3_KATA_LIST_ITEM_H_

typedef void (*a3list_destroy_fn)(void *);
typedef void *(*a3list_alloc_fn)(size_t);
typedef int (*a3list_compare_fn)(const void *, const void *, size_t);
typedef void *(*a3list_copy_fn)(void *, const void *, size_t);

typedef struct a3list_item a3list_item;

struct a3list_item {
    void *data;
    size_t size;
    a3list_alloc_fn allocator;
    a3list_destroy_fn destructor;
    a3list_copy_fn copier;
    a3list_compare_fn comparer;
};

/**
 * creates a list item suitable for adding to a list
 * this implementation will use the stdlib methods for dealing with memory:
 *    destroy = free
 *    copy = memmove
 *    compare = memcmp
 * @param  data the memory to use as a list item
 * @param  size the size of the data in bytes
 * @return      a list item
 */
a3list_item *a3list_item_create(void *data, size_t size);

/**
 * creates a list item suitable for adding to a list
 * this implementation will not destroy, copy or compare memory
 * @param  data the memory to use as a list item
 * @param  size the size of the data in bytes
 * @return      a list item
 */
a3list_item *a3list_item_create_static(void *data, size_t size);

/**
 * creates a list item suitable for adding to a list
 * this implementation you can pass functions to deal with the memory
 * @param  data       the memory to use as a list item
 * @param  size       the size of the data in bytes
 * @param  allocator  the function to allocate new memory (malloc)
 * @param  destructor the function to destroy the memory (free)
 * @param  copier     the function to copy the memory (memmove)
 * @param  comparer   the function to compare the memory (memcpy)
 * @return            a list item
 */
a3list_item *a3list_item_create_transient(void *data, size_t size, a3list_alloc_fn allocator, a3list_destroy_fn destructor, a3list_copy_fn copier,
                                          a3list_compare_fn comparer);

/**
 * destroy a list item
 * will call the destructor function on the data if set
 * @param item the item instance
 */
void a3list_item_destroy(a3list_item *item);

/**
 * copies and item.  the allocator and copy functions will be called if set, otherwise the pointer will be assigned
 * @param  item the item instance
 * @return      a copy of the item
 */
a3list_item *a3list_item_copy(const a3list_item *item);

/**
 * compares two items.  the compare function must be set on the item
 * @param  item the item instance
 * @param  data the data to compare
 * @return      zero if equal
 */
int a3list_item_compare(const a3list_item *item, const void *data);

#endif
