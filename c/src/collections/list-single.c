#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include "list-item.h"

#include "list-single.h"

typedef struct __rj_list_node RJListNode;

struct __rj_list_node {
    RJListNode *next;
    RJListItem *item;
};

struct __rj_list {
    RJListNode *first;
    size_t size;
};

static RJListNode *__rj_list_node_create(RJListItem *item)
{
    RJListNode *node = malloc(sizeof(*node));
    assert(node != NULL);
    node->next = NULL;
    node->item = item;
    return node;
}

static void __rj_list_node_destroy(RJListNode *node)
{
    if (node == NULL) {
        return;
    }

    if (node->item != NULL) {
        rj_list_item_destroy(node->item);
    }

    free(node);
}

RJList *rj_list_create()
{
    RJList *list = malloc(sizeof(RJList));
    assert(list != NULL);
    list->first = NULL;
    list->size = 0;
    return list;
}

void rj_list_destroy(RJList *list)
{
    if (list == NULL) {
        return;
    }

    rj_list_clear(list);

    free(list);
}

static void __rj_list_node_insert_after(RJListNode *node, RJListItem *item)
{
    RJListNode *other = NULL;

    if (node == NULL || item == NULL) {
        return;
    }

    other = __rj_list_node_create(item);

    other->next = node->next;
    node->next = other;
}

static RJListNode *__rj_list_get_node(const RJList *list, size_t index)
{
    RJListNode *node = NULL;
    size_t pos = 0;

    if (list == NULL) {
        return NULL;
    }

    for (node = list->first; node; node = node->next, pos++) {
        if (index == pos) {
            return node;
        }
    }
    return NULL;
}

static void __rj_list_add_node(RJList *list, RJListNode *node)
{
    if (list == NULL || node == NULL) {
        return;
    }

    node->next = list->first;
    list->first = node;
    list->size++;
}

void rj_list_add(RJList *list, RJListItem *item)
{
    RJListNode *node = NULL;

    if (list == NULL) {
        return;
    }

    node = __rj_list_node_create(item);

    __rj_list_add_node(list, node);
}

void rj_list_add_index(RJList *list, size_t index, RJListItem *item)
{
    RJListNode *node = NULL;
    if (list == NULL) {
        return;
    }

    node = __rj_list_get_node(list, index);

    if (node != NULL) {
        __rj_list_node_insert_after(node, item);
        list->size++;
    }
}

void rj_list_add_all(RJList *list, const RJList *other)
{
    RJListNode *node = NULL;

    if (list == NULL || other == NULL) {
        return;
    }

    for (node = other->first; node; node = node->next) {
        rj_list_add(list, rj_list_item_copy(node->item));
    }
}


void rj_list_add_all_index(RJList *list, size_t index, const RJList *other)
{
    RJListNode *node = NULL, *other_node = NULL;

    if (list == NULL || other == NULL) {
        return;
    }

    node = __rj_list_get_node(list, index);

    if (node == NULL) {
        return;
    }

    for (other_node = other->first; other_node; other_node = other_node->next) {
        rj_list_add_index(list, index, rj_list_item_copy(other_node->item));
    }
}

void rj_list_clear(RJList *list)
{
    RJListNode *node = NULL, *next_node = NULL;

    if (list == NULL) {
        return;
    }

    for (node = list->first; node; node = next_node) {
        next_node = node->next;
        __rj_list_node_destroy(node);
    }
    list->first = NULL;
    list->size = 0;
}

static RJListNode *__rj_list_find_node_data(const RJList *list, const void *data)
{
    RJListNode *node = NULL;

    if (list == NULL) {
        return NULL;
    }

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

int rj_list_contains(const RJList *list, const void *data)
{
    RJListNode *node = NULL;
    RJListItem *item = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __rj_list_find_node_data(list, data);

    if (node == NULL) {
        return 0;
    }

    return 1;
}

int rj_list_contains_all(const RJList *list, const RJList *other)
{
    RJListNode *node = NULL;

    if (list == NULL || other == NULL) {
        return 0;
    }

    if (other->first == NULL) {
        return 0;
    }

    for (node = other->first; node; node = node->next) {
        if (rj_list_contains(list, node->item->data)) {
            return 1;
        }
    }

    return 0;
}

void *rj_list_get(const RJList *list, size_t index)
{
    RJListNode *node = NULL;
    if (list == NULL) {
        return NULL;
    }
    node = __rj_list_get_node(list, index);

    if (node == NULL || node->item == NULL) {
        return NULL;
    }

    return node->item->data;
}

size_t rj_list_get_size(const RJList *list, size_t index)
{
    RJListNode *node = NULL;
    if (list == NULL) {
        return 0;
    }
    node = __rj_list_get_node(list, index);

    if (node == NULL || node->item == NULL) {
        return 0;
    }

    return node->item->size;
}

static void __rj_list_node_unlink(RJList *list, RJListNode *node)
{
    RJListNode *prev = NULL;

    if (list == NULL || node == NULL) {
        return;
    }

    if (list->first == node) {
        list->first = node->next;
        node->next = NULL;
        list->size--;
        return;
    }

    for (prev = list->first; prev; prev = prev->next) {
        if (prev->next == node) {
            prev->next = node->next;
            node->next = NULL;
            list->size--;
            return;
        }
    }
}

int rj_list_remove(RJList *list, const void *item)
{
    RJListNode *node = NULL, *prev = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __rj_list_find_node_data(list, item);

    if (node == NULL) {
        return 0;
    }

    __rj_list_node_unlink(list, node);

    __rj_list_node_destroy(node);

    return 1;
}

int rj_list_remove_index(RJList *list, size_t index)
{
    RJListNode *node = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __rj_list_get_node(list, index);

    if (node == NULL) {
        return 0;
    }

    __rj_list_node_unlink(list, node);

    __rj_list_node_destroy(node);

    return 1;
}

int rj_list_remove_all(RJList *list, const RJList *other)
{
    RJListNode *node = NULL, *found = NULL;
    int result = 0;

    if (list == NULL || other == NULL) {
        return 0;
    }

    for (node = other->first; node; node = node->next) {
        if (node->item == NULL) {
            continue;
        }

        found = __rj_list_find_node_data(list, node->item->data);

        if (found) {
            __rj_list_node_unlink(list, found);

            __rj_list_node_destroy(found);

            result++;
        }
    }

    return result;
}

int rj_list_index_of(const RJList *list, const void *data)
{
    RJListNode *node = NULL;
    int pos = 0;

    if (list == NULL) {
        return -1;
    }

    for (node = list->first; node; node = node->next, pos++) {
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

void rj_list_set(RJList *list, size_t index, RJListItem *item)
{
    RJListNode *node = NULL;

    if (list == NULL) {
        return;
    }

    node = __rj_list_get_node(list, index);

    if (node == NULL) {
        return;
    }

    rj_list_item_destroy(node->item);

    node->item = item;
}

size_t rj_list_size(const RJList *list)
{
    if (list == NULL) {
        return 0;
    }
    return list->size;
}

int rj_list_is_empty(const RJList *list)
{
    return list == NULL || list->size == 0 || list->first == NULL;
}

static RJListNode *__rj_list_merge_nodes(RJListNode *left, RJListNode *right)
{
    RJListNode *result = NULL, *last = NULL;
    RJListNode *next = NULL;

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
static RJListNode *__rj_list_merge_sort(RJListNode *first)
{
    RJListNode *left = NULL;
    RJListNode *right = NULL;
    RJListNode *node = NULL, *node_next = NULL;
    size_t pos = 0;

    /* make sure we have 2 items at least */
    if (first == NULL || first->next == NULL) {
        return first;
    }

    /* this divides the list into two seperate lists.
     * doesn't split down the middle as calculating the size is
     * extra overhead, instead just alternating each consecutive item
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

    left = __rj_list_merge_sort(left);
    right = __rj_list_merge_sort(right);

    return __rj_list_merge_nodes(left, right);
}

void rj_list_sort(RJList *list)
{
    // Base case. A list of zero or one elements is sorted, by definition.
    if (rj_list_size(list) <= 1) {
        return;
    }

    list->first = __rj_list_merge_sort(list->first);
}
