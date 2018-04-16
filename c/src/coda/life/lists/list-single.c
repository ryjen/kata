#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include <coda/life/lists/list-item.h>
#include <coda/life/lists/list-vtable.h>
#include "internal.h"

typedef struct __coda_slist CodaSList;

typedef struct __coda_slist_node CodaSListNode;

struct __coda_slist_node {
    CodaSListNode *next;
    CodaListItem *item;
};

struct __coda_slist {
    CodaSListNode *first;
    size_t size;
};

extern void coda_list_single_clear(CodaList *list);

static inline CodaSList *__coda_slist_impl(const CodaList *arg) {
    assert(arg->impl != NULL);
    return (CodaSList *) arg->impl;
}

static CodaSListNode *__coda_slist_node_create(CodaListItem *item) {
    CodaSListNode *node = NULL;
    assert(item != NULL);
    node = malloc(sizeof(CodaSListNode));
    assert(node != NULL);
    node->next = NULL;
    node->item = item;
    return node;
}

static void __coda_slist_node_destroy(CodaSListNode *node) {
    assert(node != NULL);

    if (node->item != NULL) {
        coda_list_item_delete(node->item);
    }

    free(node);
}

void *coda_list_single_new() {
    CodaSList *list = malloc(sizeof(CodaSList));
    assert(list != NULL);
    list->first = NULL;
    list->size = 0;
    return list;
}

void coda_list_single_delete(CodaList *list) {
    assert(list != NULL);

    coda_list_single_clear(list);

    free(__coda_slist_impl(list));
}

static void __coda_slist_node_insert_after(CodaSListNode *node, CodaListItem *item) {
    CodaSListNode *other = NULL;

    assert(node != NULL);
    assert(item != NULL);

    other = __coda_slist_node_create(item);

    other->next = node->next;
    node->next = other;
}

static CodaSListNode *__coda_slist_get_node(const CodaSList *list, size_t index) {
    CodaSListNode *node = NULL;
    size_t pos = 0;

    assert(list != NULL);

    for (node = list->first; node; node = node->next, pos++) {
        if (index == pos) {
            return node;
        }
    }
    return NULL;
}

static void __coda_slist_add_node(CodaSList *list, CodaSListNode *node) {
    assert(list != NULL);
    assert(node != NULL);

    node->next = list->first;
    list->first = node;
    list->size++;
}

void coda_list_single_add(CodaList *list, CodaListItem *item) {
    CodaSListNode *node = NULL;

    assert(list != NULL);
    assert(item != NULL);

    node = __coda_slist_node_create(item);

    __coda_slist_add_node(__coda_slist_impl(list), node);
}

void coda_list_single_add_index(CodaList *list, size_t index, CodaListItem *item) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;

    assert(list != NULL);
    assert(item != NULL);

    impl = __coda_slist_impl(list);

    node = __coda_slist_get_node(impl, index);

    if (node != NULL) {
        __coda_slist_node_insert_after(node, item);
        impl->size++;
    }
}

void coda_list_single_add_all(CodaList *list, const CodaList *other) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;

    assert(list != NULL);
    assert(other != NULL);

    impl = __coda_slist_impl(other);

    for (node = impl->first; node; node = node->next) {
        coda_list_single_add(list, coda_list_item_copy(node->item));
    }
}

void coda_list_single_add_all_index(CodaList *list, size_t index, const CodaList *other) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL, *other_node = NULL;

    assert(list != NULL);
    assert(other != NULL);

    node = __coda_slist_get_node(__coda_slist_impl(list), index);

    if (node == NULL) {
        return;
    }

    impl = __coda_slist_impl(other);

    for (other_node = impl->first; other_node; other_node = other_node->next) {
        coda_list_single_add_index(list, index, coda_list_item_copy(other_node->item));
    }
}

void coda_list_single_clear(CodaList *list) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL, *next_node = NULL;

    assert(list != NULL);

    impl = __coda_slist_impl(list);

    for (node = impl->first; node; node = next_node) {
        next_node = node->next;
        __coda_slist_node_destroy(node);
    }
    impl->first = NULL;
    impl->size = 0;
}

static CodaSListNode *__coda_slist_find_node_data(const CodaSList *list, const void *data) {
    CodaSListNode *node = NULL;

    assert(list != NULL);

    for (node = list->first; node; node = node->next) {
        CodaListItem *item = node->item;

        if (item == NULL) {
            continue;
        }

        if (data == NULL && item->data == NULL) {
            return node;
        }

        if (coda_list_item_compare(item, data) == 0) {
            return node;
        }
    }
    return NULL;
}

int coda_list_single_contains(const CodaList *list, const void *data) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;
    CodaListItem *item = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __coda_slist_find_node_data(__coda_slist_impl(list), data);

    if (node == NULL) {
        return 0;
    }

    return 1;
}

int coda_list_single_contains_all(const CodaList *list, const CodaList *other) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;

    if (list == NULL || other == NULL) {
        return 0;
    }

    impl = __coda_slist_impl(other);

    if (impl->first == NULL) {
        return 0;
    }

    for (node = impl->first; node; node = node->next) {
        if (coda_list_contains(list, node->item->data)) {
            return 1;
        }
    }

    return 0;
}

void *coda_list_single_get(const CodaList *list, size_t index) {
    CodaSListNode *node = NULL;

    if (list == NULL) {
        return NULL;
    }
    node = __coda_slist_get_node(__coda_slist_impl(list), index);

    if (node == NULL || node->item == NULL) {
        return NULL;
    }

    return node->item->data;
}

static void __coda_slist_node_unlink(CodaSList *list, CodaSListNode *node, CodaSListNode *prev) {
    if (list == NULL || node == NULL) {
        return;
    }

    if (list->first == node) {
        list->first = node->next;
        node->next = NULL;
        list->size--;
        return;
    }

    if (prev == NULL) {
        for (prev = list->first; prev; prev = prev->next) {
            if (prev->next == node) {
                break;
            }
        }
    }

    if (prev != NULL) {
        prev->next = node->next;
        node->next = NULL;
        list->size--;
    }
}

int coda_list_single_remove(CodaList *list, const void *item) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;

    if (list == NULL) {
        return 0;
    }

    impl = __coda_slist_impl(list);

    node = __coda_slist_find_node_data(impl, item);

    if (node == NULL) {
        return 0;
    }

    __coda_slist_node_unlink(impl, node, NULL);

    __coda_slist_node_destroy(node);

    return 1;
}

int coda_list_single_remove_index(CodaList *list, size_t index) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;

    if (list == NULL) {
        return 0;
    }

    impl = __coda_slist_impl(list);

    node = __coda_slist_get_node(impl, index);

    if (node == NULL) {
        return 0;
    }

    __coda_slist_node_unlink(impl, node, NULL);

    __coda_slist_node_destroy(node);

    return 1;
}

int coda_list_single_remove_all(CodaList *list, const CodaList *other) {
    CodaSList *impl = NULL, *oimpl = NULL;
    CodaSListNode *node = NULL, *found = NULL, *prev = NULL;
    int result = 0;

    if (list == NULL || other == NULL) {
        return 0;
    }

    impl = __coda_slist_impl(list);
    oimpl = __coda_slist_impl(other);

    for (node = oimpl->first; node; prev = node, node = node->next) {
        if (node->item == NULL) {
            continue;
        }

        found = __coda_slist_find_node_data(impl, node->item->data);

        if (found) {
            __coda_slist_node_unlink(impl, found, prev);

            __coda_slist_node_destroy(found);

            result++;
        }
    }

    return result;
}

int coda_list_single_index_of(const CodaList *list, const void *data) {
    CodaSList *impl = NULL;
    CodaSListNode *node = NULL;
    int pos = 0;

    if (list == NULL) {
        return -1;
    }

    impl = __coda_slist_impl(list);

    for (node = impl->first; node; node = node->next, pos++) {
        CodaListItem *item = node->item;

        if (item == NULL) {
            continue;
        }

        if (coda_list_item_compare(item, data) == 0) {
            return pos;
        }
    }

    return -1;
}

void coda_list_single_set(CodaList *list, size_t index, CodaListItem *item) {
    CodaSListNode *node = NULL;

    if (list == NULL) {
        return;
    }

    node = __coda_slist_get_node(__coda_slist_impl(list), index);

    if (node == NULL) {
        return;
    }

    coda_list_item_delete(node->item);

    node->item = item;
}

size_t coda_list_single_size(const CodaList *list) {
    CodaSList *impl = NULL;

    if (list == NULL) {
        return 0;
    }

    impl = __coda_slist_impl(list);

    return impl->size;
}

int coda_list_single_is_empty(const CodaList *list) {
    CodaSList *impl = NULL;
    assert(list != NULL);
    impl = __coda_slist_impl(list);
    return impl->size == 0 || impl->first == NULL;
}

static CodaSListNode *__coda_slist_merge_nodes(CodaSListNode *left, CodaSListNode *right) {
    CodaSListNode *result = NULL, *last = NULL;
    CodaSListNode *next = NULL;

    while (left && right) {
        // compare the two nodes
        if (coda_list_item_compare(left->item, right->item->data) <= 0) {
            // move the left up
            next = left->next;
            if (last == NULL) {
                left->next = result;
                result = left;
            } else {
                left->next = NULL;
                last->next = left;
            }
            last = left;
            left = next;
        } else {
            // move the right up
            next = right->next;
            if (last == NULL) {
                right->next = result;
                result = right;
            } else {
                right->next = NULL;
                last->next = right;
            }
            last = right;
            right = next;
        }
    }

    if (left) {
        // ensure left ends meet
        if (last == NULL) {
            result = left;
        } else {
            last->next = left;
        }
        // last might be needed in the next comparison
        last = left;
    }

    if (right) {
        // ensure right ends meet
        if (last == NULL) {
            result = left;
        } else {
            last->next = right;
        }
        last = right;
    }

    return result;
}

/**
 * standard merge sort, O(n log n)
 * TODO: more efficient algorithm
 */
static CodaSListNode *__coda_slist_merge_sort(CodaSListNode *first) {
    CodaSListNode *left = NULL;
    CodaSListNode *right = NULL;
    CodaSListNode *node = NULL, *node_next = NULL;
    size_t pos = 0;

    /* make sure we have 2 items at least */
    if (first == NULL || first->next == NULL) {
        return first;
    }

    /* this divides the list into two seperate lists.
     * just alternating each consecutive item
     */
    for (node = first; node; node = node_next, pos++) {
        node_next = node->next;
        if (pos % 2 != 0) {
            node->next = left;
            left = node;
        } else {
            node->next = right;
            right = node;
        }
    }

    left = __coda_slist_merge_sort(left);
    right = __coda_slist_merge_sort(right);

    return __coda_slist_merge_nodes(left, right);
}

void coda_list_single_sort(CodaList *list) {
    CodaSList *impl = NULL;

    // Base case. A list of zero or one elements is sorted, by definition.
    if (coda_list_size(list) <= 1) {
        return;
    }

    impl = __coda_slist_impl(list);

    impl->first = __coda_slist_merge_sort(impl->first);
}

void coda_list_single_for_each(CodaList *list, CodaListCallback callback) {
    CodaSListNode *node = NULL;
    CodaSListNode *prev = NULL;
    CodaSList *impl = NULL;
    size_t index = 0;

    assert(list != NULL);
    assert(callback != NULL);

    impl = __coda_slist_impl(list);

    for (node = impl->first; node; prev = node, node = node->next) {
        if (callback(list, index++, node->item) == CodaListIteratorDelete) {
            __coda_slist_node_unlink(impl, node, prev);

            __coda_slist_node_destroy(node);
        }
    }
}

static CodaListVtable __coda_slist_vtable = {.create = coda_list_single_new,
        .destroy = coda_list_single_delete,
        .add = coda_list_single_add,
        .add_all = coda_list_single_add_all,
        .add_index = coda_list_single_add_index,
        .add_all_index = coda_list_single_add_all_index,
        .clear = coda_list_single_clear,
        .contains = coda_list_single_contains,
        .contains_all = coda_list_single_contains_all,
        .get = coda_list_single_get,
        .remove = coda_list_single_remove,
        .remove_index = coda_list_single_remove_index,
        .remove_all = coda_list_single_remove_all,
        .index_of = coda_list_single_index_of,
        .set = coda_list_single_set,
        .size = coda_list_single_size,
        .is_empty = coda_list_single_is_empty,
        .sort = coda_list_single_sort,
        .for_each = coda_list_single_for_each};

CodaListVtable *coda_list_single_vtable() {
    return &__coda_slist_vtable;
}
