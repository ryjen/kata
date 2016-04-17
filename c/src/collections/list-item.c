
#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "list-item.h"

a3list_item *a3list_item_create(void *data, size_t size)
{
    return a3list_item_create_transient(data, size, malloc, free, memmove, memcmp);
}

a3list_item *a3list_item_create_static(void *data, size_t size)
{
    return a3list_item_create_transient(data, size, NULL, NULL, NULL, NULL);
}

a3list_item *a3list_item_create_transient(void *data, size_t size, a3list_alloc_fn allocator, a3list_destroy_fn destructor, a3list_copy_fn copier,
                                          a3list_compare_fn comparer)
{
    a3list_item *item = malloc(sizeof(*item));
    assert(item != NULL);
    item->data = data;
    item->size = size;
    item->allocator = allocator;
    item->destructor = destructor;
    item->copier = copier;
    item->comparer = comparer;
    return item;
}

void a3list_item_destroy(a3list_item *item)
{
    if (item == NULL) {
        return;
    }

    if (item->data && item->destructor) {
        (*item->destructor)(item->data);
    }

    free(item);
}

a3list_item *a3list_item_copy(const a3list_item *orig)
{
    a3list_item *item = NULL;

    if (orig == NULL) {
        return NULL;
    }

    item = malloc(sizeof(*item));
    assert(item != NULL);
    if (orig->copier && orig->allocator) {
        item->data = (*orig->allocator)(orig->size);
        assert(item->data != NULL);
        (*orig->copier)(item->data, orig->data, orig->size);
    } else {
        item->data = orig->data;
    }
    item->size = orig->size;
    item->allocator = orig->allocator;
    item->destructor = orig->destructor;
    item->copier = orig->copier;
    item->comparer = orig->comparer;

    return item;
}

int a3list_item_compare(const a3list_item *item, const void *data)
{
    if (item == NULL) {
        return -1;
    }

    if (item->data == NULL && data == NULL) {
        return 0;
    }

    if (item->comparer == NULL || data == NULL) {
        return -1;
    }

    return (*item->comparer)(item->data, data, item->size);
}
