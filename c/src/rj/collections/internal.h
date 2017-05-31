#ifndef RJ_KATA_LIST_INTERNAL_H
#define RJ_KATA_LIST_INTERNAL_H

#include "list-vtable.h"

struct __rj_list {
    RJListVtable *vtable;
    void *impl;
};

/**
 * a singly linked list
 */
RJListVtable *rj_list_single_vtable();

#endif
