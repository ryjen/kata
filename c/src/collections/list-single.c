#include <stdlib.h>

#include <assert.h>

#include "list-item.h"

#include "list-single.h"

typedef struct a3list_node a3list_node;

struct a3list_node {
    a3list_node *next;
    a3list_item *item;
};

struct a3list {
    a3list_node *first;
    size_t size;
};

static a3list_node *__a3list_node_create(a3list_item *item)
{
    a3list_node *node = malloc(sizeof(*node));
    assert(node != NULL);
    node->next = NULL;
    node->item = item;
    return node;
}

static void __a3list_node_destroy(a3list_node *node)
{
    if (node == NULL) {
        return;
    }

    if (node->item != NULL) {
        a3list_item_destroy(node->item);
    }

    free(node);
}

a3list *a3list_create()
{
    a3list *list = malloc(sizeof(*list));
    assert(list != NULL);
    list->first = NULL;
    list->size = 0;
    return list;
}

void a3list_destroy(a3list *list)
{
    if (list == NULL) {
        return;
    }

    a3list_clear(list);

    free(list);
}

static void __a3list_node_insert_after(a3list_node *node, a3list_item *item)
{
    a3list_node *other = NULL;

    if (node == NULL || item == NULL) {
        return;
    }

    other = __a3list_node_create(item);

    other->next = node->next;
    node->next = other;
}

static a3list_node *__a3list_get_node(const a3list *list, size_t index)
{
    a3list_node *node = NULL;
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

void a3list_add(a3list *list, a3list_item *item)
{
    a3list_node *node = NULL;

    if (list == NULL) {
        return;
    }

    node = __a3list_node_create(item);
    node->next = list->first;
    list->first = node;
    list->size++;
}

void a3list_add_index(a3list *list, size_t index, a3list_item *item)
{
    a3list_node *node = NULL;
    if (list == NULL) {
        return;
    }

    node = __a3list_get_node(list, index);

    if (node != NULL) {
        __a3list_node_insert_after(node, item);
        list->size++;
    }
}

void a3list_add_all(a3list *list, const a3list *other)
{
    a3list_node *node = NULL;

    if (list == NULL || other == NULL) {
        return;
    }

    for (node = other->first; node; node = node->next) {
        a3list_add(list, a3list_item_copy(node->item));
    }
}


void a3list_add_all_index(a3list *list, size_t index, const a3list *other)
{
    a3list_node *node = NULL, *other_node = NULL;

    if (list == NULL || other == NULL) {
        return;
    }

    node = __a3list_get_node(list, index);

    if (node == NULL) {
        return;
    }

    for (other_node = other->first; other_node; other_node = other_node->next) {
        a3list_add_index(list, index, a3list_item_copy(other_node->item));
    }
}

void a3list_clear(a3list *list)
{
    a3list_node *node = NULL, *next_node = NULL;

    if (list == NULL) {
        return;
    }

    for (node = list->first; node; node = next_node) {
        next_node = node->next;
        __a3list_node_destroy(node);
    }
    list->first = NULL;
    list->size = 0;
}

static a3list_node *__a3list_find_node_data(const a3list *list, const void *data)
{
    a3list_node *node = NULL;

    if (list == NULL) {
        return NULL;
    }

    for (node = list->first; node; node = node->next) {
        a3list_item *item = node->item;

        if (item == NULL) {
            continue;
        }

        if (data == NULL && item->data == NULL) {
            return node;
        }

        if (a3list_item_compare(item, data) == 0) {
            return node;
        }
    }
    return NULL;
}

int a3list_contains(const a3list *list, const void *data)
{
    a3list_node *node = NULL;
    a3list_item *item = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __a3list_find_node_data(list, data);

    if (node == NULL) {
        return 0;
    }

    return 1;
}

int a3list_contains_all(const a3list *list, const a3list *other)
{
    a3list_node *node = NULL;

    if (list == NULL || other == NULL) {
        return 0;
    }

    if (other->first == NULL) {
        return 0;
    }

    for (node = other->first; node; node = node->next) {
        if (a3list_contains(list, node->item->data)) {
            return 1;
        }
    }

    return 0;
}

void *a3list_get(const a3list *list, size_t index)
{
    a3list_node *node = NULL;
    if (list == NULL) {
        return NULL;
    }
    node = __a3list_get_node(list, index);

    if (node == NULL || node->item == NULL) {
        return NULL;
    }

    return node->item->data;
}

size_t a3list_get_size(const a3list *list, size_t index)
{
    a3list_node *node = NULL;
    if (list == NULL) {
        return 0;
    }
    node = __a3list_get_node(list, index);

    if (node == NULL || node->item == NULL) {
        return 0;
    }

    return node->item->size;
}

static void __a3list_node_unlink(a3list *list, a3list_node *node)
{
    a3list_node *prev = NULL;

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

int a3list_remove(a3list *list, const void *item)
{
    a3list_node *node = NULL, *prev = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __a3list_find_node_data(list, item);

    if (node == NULL) {
        return 0;
    }

    __a3list_node_unlink(list, node);

    __a3list_node_destroy(node);

    return 1;
}

int a3list_remove_index(a3list *list, size_t index)
{
    a3list_node *node = NULL;

    if (list == NULL) {
        return 0;
    }

    node = __a3list_get_node(list, index);

    if (node == NULL) {
        return 0;
    }

    __a3list_node_unlink(list, node);

    __a3list_node_destroy(node);

    return 1;
}

int a3list_remove_all(a3list *list, const a3list *other)
{
    a3list_node *node = NULL, *found = NULL;
    int result = 0;

    if (list == NULL || other == NULL) {
        return 0;
    }

    for (node = other->first; node; node = node->next) {
        if (node->item == NULL) {
            continue;
        }

        found = __a3list_find_node_data(list, node->item->data);

        if (found) {
            __a3list_node_unlink(list, found);

            __a3list_node_destroy(found);

            result++;
        }
    }

    return result;
}

int a3list_index_of(const a3list *list, const void *data)
{
    a3list_node *node = NULL;
    int pos = 0;

    if (list == NULL) {
        return -1;
    }

    for (node = list->first; node; node = node->next, pos++) {
        a3list_item *item = node->item;

        if (item == NULL) {
            continue;
        }

        if (a3list_item_compare(item, data) == 0) {
            return pos;
        }
    }

    return -1;
}

void a3list_set(a3list *list, size_t index, a3list_item *item)
{
    a3list_node *node = NULL;

    if (list == NULL) {
        return;
    }

    node = __a3list_get_node(list, index);

    if (node == NULL) {
        return;
    }

    a3list_item_destroy(node->item);

    node->item = item;
}

size_t a3list_size(const a3list *list)
{
    if (list == NULL) {
        return 0;
    }
    return list->size;
}

int a3list_is_empty(const a3list *list)
{
    return list == NULL || list->size == 0 || list->first == NULL;
}