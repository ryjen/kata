
#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "list-item.h"

RJListItem *rj_list_item_create(void *data, size_t size, RJListCompareCallback comparer)
{
    return rj_list_item_create_transient(data, size, comparer, malloc, free, memmove);
}

RJListItem *rj_list_item_create_static(void *data, size_t size, RJListCompareCallback comparer)
{
    return rj_list_item_create_transient(data, size, comparer, NULL, NULL, NULL);
}

RJListItem *rj_list_item_create_transient(void *data, size_t size, RJListCompareCallback comparer,
                                          RJListAllocCallback allocator, RJListDestroyCallback destructor,
                                          RJListCopyCallback copier)
{
    RJListItem *item = malloc(sizeof(*item));
    assert(item != NULL);
    item->data = data;
    item->size = size;
    item->allocator = allocator;
    item->destructor = destructor;
    item->copier = copier;
    item->comparer = comparer;
    return item;
}

void rj_list_item_destroy(RJListItem *item)
{
    if (item == NULL) {
        return;
    }

    if (item->data && item->destructor) {
        (*item->destructor)(item->data);
    }

    free(item);
}

RJListItem *rj_list_item_copy(const RJListItem *orig)
{
    RJListItem *item = NULL;

    if (orig == NULL) {
        return NULL;
    }

    item = malloc(sizeof(RJListItem));
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

int rj_list_item_compare(const RJListItem *item, const void *data)
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
