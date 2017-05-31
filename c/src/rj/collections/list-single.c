#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include <rj/collections/list-item.h>
#include <rj/collections/list-vtable.h>
#include "internal.h"

typedef struct __rj_slist RJSList;

typedef struct __rj_slist_node RJSListNode;

struct __rj_slist_node {
    RJSListNode *next;
    RJListItem *item;
};

struct __rj_slist {
    RJSListNode *first;
    size_t size;
};

extern void rj_list_single_clear(RJList *list);

static inline RJSList *__rj_slist_impl(const RJList *arg)
{
    assert(arg->impl != NULL);
    return (RJSList *)arg->impl;
}

static RJSListNode *__rj_slist_node_create(RJListItem *item)
{
    RJSListNode *node = NULL;
    assert(item != NULL);
    node = malloc(sizeof(RJSListNode));
    assert(node != NULL);
    node->next = NULL;
    node->item = item;
    return node;
}

static void __rj_slist_node_destroy(RJSListNode *node)
{
    assert(node != NULL);

    if (node->item != NULL) {
        rj_list_item_destroy(node->item);
    }

    free(node);
}

void *rj_list_single_create()
{
    RJSList *list = malloc(sizeof(RJSList));
    assert(list != NULL);
    list->first = NULL;
    list->size = 0;
    return list;
}

void rj_list_single_destroy(RJList *list)
{
    assert(list != NULL);

    rj_list_single_clear(list);

    free(__rj_slist_impl(list));
}

static void __rj_slist_node_insert_after(RJSListNode *node, RJListItem *item)
{
    RJSListNode *other = NULL;

    assert(node != NULL);
    assert(item != NULL);

    other = __rj_slist_node_create(item);

    other->next = node->next;
    node->next = other;
}

static RJSListNode *__rj_slist_get_node(const RJSList *list, size_t index)
{
    RJSListNode *node = NULL;
    size_t pos = 0;

    assert(list != NULL);

    for (node = list->first; node; node = node->next, pos++) {
        if (index == pos) {
            return node;
        }
    }
    return NULL;
}

static void __rj_slist_add_node(RJSList *list, RJSListNode *node)
{
    assert(list != NULL);
    assert(node != NULL);

    node->next = list->first;
    list->first = node;
    list->size++;
}

void rj_list_single_add(RJList *list, RJListItem *item)
{
    RJSListNode *node = NULL;

    assert(list != NULL);
    assert(item != NULL);

    node = __rj_slist_node_create(item);

    __rj_slist_add_node(__rj_slist_impl(list), node);
}

void rj_list_single_add_index(RJList *list, size_t index, RJListItem *item)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;

    assert(list != NULL);
    assert(item != NULL);

    impl = __rj_slist_impl(list);

    node = __rj_slist_get_node(impl, index);

    if (node != NULL) {
        __rj_slist_node_insert_after(node, item);
        impl->size++;
    }
}

void rj_list_single_add_all(RJList *list, const RJList *other)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;

    assert(list != NULL);
    assert(other != NULL);

    impl = __rj_slist_impl(other);

    for (node = impl->first; node; node = node->next) {
        rj_list_single_add(list, rj_list_item_copy(node->item));
    }
}

void rj_list_single_add_all_index(RJList *list, size_t index, const RJList *other)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL, *other_node = NULL;

    assert(list != NULL);
    assert(other != NULL);

    node = __rj_slist_get_node(__rj_slist_impl(list), index);

    if (node == NULL) {
        return;
    }

    impl = __rj_slist_impl(other);

    for (other_node = impl->first; other_node; other_node = other_node->next) {
        rj_list_single_add_index(list, index, rj_list_item_copy(other_node->item));
    }
}

void rj_list_single_clear(RJList *list)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL, *next_node = NULL;

    assert(list != NULL);

    impl = __rj_slist_impl(list);

    for (node = impl->first; node; node = next_node) {
        next_node = node->next;
        __rj_slist_node_destroy(node);
    }
    impl->first = NULL;
    impl->size = 0;
}

static RJSListNode *__rj_slist_find_node_data(const RJSList *list, const void *data)
{
    RJSListNode *node = NULL;

    assert(list != NULL);

    for (node = list->first; node; node = node->next) {
        RJListItem *item = node->item;

        if (item == NULL) {
            continue;
        }

        if (data == NULL && item->data == NULL) {
            return node;
        }

        if (rj_list_item_compare(item, data) == 0) {
            return node;
        }
    }
    return NULL;
}

int rj_list_single_contains(const RJList *list, const void *data)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;
    RJListItem *item = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __rj_slist_find_node_data(__rj_slist_impl(list), data);

    if (node == NULL) {
        return 0;
    }

    return 1;
}

int rj_list_single_contains_all(const RJList *list, const RJList *other)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;

    if (list == NULL || other == NULL) {
        return 0;
    }

    impl = __rj_slist_impl(other);

    if (impl->first == NULL) {
        return 0;
    }

    for (node = impl->first; node; node = node->next) {
        if (rj_list_contains(list, node->item->data)) {
            return 1;
        }
    }

    return 0;
}

void *rj_list_single_get(const RJList *list, size_t index)
{
    RJSListNode *node = NULL;

    if (list == NULL) {
        return NULL;
    }
    node = __rj_slist_get_node(__rj_slist_impl(list), index);

    if (node == NULL || node->item == NULL) {
        return NULL;
    }

    return node->item->data;
}

static void __rj_slist_node_unlink(RJSList *list, RJSListNode *node, RJSListNode *prev)
{
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

int rj_list_single_remove(RJList *list, const void *item)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;

    if (list == NULL) {
        return 0;
    }

    impl = __rj_slist_impl(list);

    node = __rj_slist_find_node_data(impl, item);

    if (node == NULL) {
        return 0;
    }

    __rj_slist_node_unlink(impl, node, NULL);

    __rj_slist_node_destroy(node);

    return 1;
}

int rj_list_single_remove_index(RJList *list, size_t index)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;

    if (list == NULL) {
        return 0;
    }

    impl = __rj_slist_impl(list);

    node = __rj_slist_get_node(impl, index);

    if (node == NULL) {
        return 0;
    }

    __rj_slist_node_unlink(impl, node, NULL);

    __rj_slist_node_destroy(node);

    return 1;
}

int rj_list_single_remove_all(RJList *list, const RJList *other)
{
    RJSList *impl = NULL, *oimpl = NULL;
    RJSListNode *node = NULL, *found = NULL, *prev = NULL;
    int result = 0;

    if (list == NULL || other == NULL) {
        return 0;
    }

    impl = __rj_slist_impl(list);
    oimpl = __rj_slist_impl(other);

    for (node = oimpl->first; node; prev = node, node = node->next) {
        if (node->item == NULL) {
            continue;
        }

        found = __rj_slist_find_node_data(impl, node->item->data);

        if (found) {
            __rj_slist_node_unlink(impl, found, prev);

            __rj_slist_node_destroy(found);

            result++;
        }
    }

    return result;
}

int rj_list_single_index_of(const RJList *list, const void *data)
{
    RJSList *impl = NULL;
    RJSListNode *node = NULL;
    int pos = 0;

    if (list == NULL) {
        return -1;
    }

    impl = __rj_slist_impl(list);

    for (node = impl->first; node; node = node->next, pos++) {
        RJListItem *item = node->item;

        if (item == NULL) {
            continue;
        }

        if (rj_list_item_compare(item, data) == 0) {
            return pos;
        }
    }

    return -1;
}

void rj_list_single_set(RJList *list, size_t index, RJListItem *item)
{
    RJSListNode *node = NULL;

    if (list == NULL) {
        return;
    }

    node = __rj_slist_get_node(__rj_slist_impl(list), index);

    if (node == NULL) {
        return;
    }

    rj_list_item_destroy(node->item);

    node->item = item;
}

size_t rj_list_single_size(const RJList *list)
{
    RJSList *impl = NULL;

    if (list == NULL) {
        return 0;
    }

    impl = __rj_slist_impl(list);

    return impl->size;
}

int rj_list_single_is_empty(const RJList *list)
{
    RJSList *impl = NULL;
    assert(list != NULL);
    impl = __rj_slist_impl(list);
    return impl->size == 0 || impl->first == NULL;
}

static RJSListNode *__rj_slist_merge_nodes(RJSListNode *left, RJSListNode *right)
{
    RJSListNode *result = NULL, *last = NULL;
    RJSListNode *next = NULL;

    while (left && right) {
        // compare the two nodes
        if (rj_list_item_compare(left->item, right->item->data) <= 0) {
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
static RJSListNode *__rj_slist_merge_sort(RJSListNode *first)
{
    RJSListNode *left = NULL;
    RJSListNode *right = NULL;
    RJSListNode *node = NULL, *node_next = NULL;
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

    left = __rj_slist_merge_sort(left);
    right = __rj_slist_merge_sort(right);

    return __rj_slist_merge_nodes(left, right);
}

void rj_list_single_sort(RJList *list)
{
    RJSList *impl = NULL;

    // Base case. A list of zero or one elements is sorted, by definition.
    if (rj_list_size(list) <= 1) {
        return;
    }

    impl = __rj_slist_impl(list);

    impl->first = __rj_slist_merge_sort(impl->first);
}

void rj_list_single_for_each(RJList *list, RJListCallback callback)
{
    RJSListNode *node = NULL;
    RJSListNode *prev = NULL;
    RJSList *impl = NULL;
    size_t index = 0;

    assert(list != NULL);
    assert(callback != NULL);

    impl = __rj_slist_impl(list);

    for (node = impl->first; node; prev = node, node = node->next) {
        if (callback(list, index++, node->item) == RJListIteratorDelete) {
            __rj_slist_node_unlink(impl, node, prev);

            __rj_slist_node_destroy(node);
        }
    }
}

static RJListVtable __rj_slist_vtable = {.create = rj_list_single_create,
                                         .destroy = rj_list_single_destroy,
                                         .add = rj_list_single_add,
                                         .add_all = rj_list_single_add_all,
                                         .add_index = rj_list_single_add_index,
                                         .add_all_index = rj_list_single_add_all_index,
                                         .clear = rj_list_single_clear,
                                         .contains = rj_list_single_contains,
                                         .contains_all = rj_list_single_contains_all,
                                         .get = rj_list_single_get,
                                         .remove = rj_list_single_remove,
                                         .remove_index = rj_list_single_remove_index,
                                         .remove_all = rj_list_single_remove_all,
                                         .index_of = rj_list_single_index_of,
                                         .set = rj_list_single_set,
                                         .size = rj_list_single_size,
                                         .is_empty = rj_list_single_is_empty,
                                         .sort = rj_list_single_sort,
                                         .for_each = rj_list_single_for_each};

RJListVtable *rj_list_single_vtable()
{
    return &__rj_slist_vtable;
}
