#ifndef CODA_KATA_LIST_INTERNAL_H
#define CODA_KATA_LIST_INTERNAL_H

#include "list-vtable.h"

struct __coda_list {
    CodaListVtable *vtable;
    void *impl;
};

struct __coda_list_item {
    void *data;
    size_t size;
    CodaListAllocCallback allocator;
    CodaListDestroyCallback destructor;
    CodaListCopyCallback copier;
    CodaListCompareCallback comparer;
};

/**
 * a singly linked list
 */
CodaListVtable *coda_list_single_vtable();

#endif
