
#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "list-item.h"

rj_list_item *rj_list_item_create(void *data, size_t size, rj_list_compare_fn comparer)
{
    return rj_list_item_create_transient(data, size, comparer, malloc, free, memmove);
}

rj_list_item *rj_list_item_create_static(void *data, size_t size, rj_list_compare_fn comparer)
{
    return rj_list_item_create_transient(data, size, comparer, NULL, NULL, NULL);
}

rj_list_item *rj_list_item_create_transient(void *data, size_t size, rj_list_compare_fn comparer, rj_list_alloc_fn allocator,
                                          rj_list_destroy_fn destructor, rj_list_copy_fn copier)
{
    rj_list_item *item = malloc(sizeof(*item));
    assert(item != NULL);
    item->data = data;
    item->size = size;
    item->allocator = allocator;
    item->destructor = destructor;
    item->copier = copier;
    item->comparer = comparer;
    return item;
}

void rj_list_item_destroy(rj_list_item *item)
{
    if (item == NULL) {
        return;
    }

    if (item->data && item->destructor) {
        (*item->destructor)(item->data);
    }

    free(item);
}

rj_list_item *rj_list_item_copy(const rj_list_item *orig)
{
    rj_list_item *item = NULL;

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

int rj_list_item_compare(const rj_list_item *item, const void *data)
{
    assert(item != NULL);

    if (item->data == NULL && data == NULL) {
        return 0;
    }

    if (item->data == NULL || data == NULL) {
        return -1;
    }

    if (item->comparer) {
        return (*item->comparer)(item->data, data, item->size);
    } else {
        return memcmp(item->data, data, item->size);
    }
}
