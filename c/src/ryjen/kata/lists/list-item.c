
#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "internal.h"
#include <ryjen/kata/lists/list-item.h>

CodaListItem *coda_list_item_new(void *data, size_t size, CodaListCompareCallback comparer) {
    return coda_list_item_new_transient(data, size, comparer, malloc, free, memmove);
}

CodaListItem *coda_list_item_new_static(void *data, size_t size, CodaListCompareCallback comparer) {
    return coda_list_item_new_transient(data, size, comparer, NULL, NULL, NULL);
}

CodaListItem *coda_list_item_new_transient(void *data, size_t size,
                                           CodaListCompareCallback comparer,
                                           CodaListAllocCallback allocator, CodaListDestroyCallback destructor,
                                           CodaListCopyCallback copier) {
    CodaListItem *item = malloc(sizeof(*item));
    assert(item != NULL);
    item->data = data;
    item->size = size;
    item->allocator = allocator;
    item->destructor = destructor;
    item->copier = copier;
    item->comparer = comparer;
    return item;
}

void coda_list_item_delete(struct __coda_list_item *item) {
    if (item == NULL) {
        return;
    }

    if (item->data && item->destructor) {
        (*item->destructor)(item->data);
    }

    free(item);
}

void *coda_list_item_data(const CodaListItem *item) {
    return item->data;
}

size_t coda_list_item_size(const CodaListItem *item) {
    return item->size;
}

CodaListItem *coda_list_item_copy(const CodaListItem *orig) {
    CodaListItem *item = NULL;

    if (orig == NULL) {
        return NULL;
    }

    item = malloc(sizeof(CodaListItem));
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

int coda_list_item_compare(const CodaListItem *item, const void *data) {
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
