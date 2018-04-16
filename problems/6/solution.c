#include <stdlib.h>

typedef struct xor_list_node XListNode;
typedef struct xor_list XList;

struct xor_list_node {
    void *xptr;
    void *value;
}

struct xor_list {
    XListNode *first;
    XListNode *last;
}

XListNode *xlist_node_new(void *value) {
    XListNode *node = malloc(sizeof(XListNode));
    node->xptr = 0;
    node->value = value;
    return node;
}

static void __xlist_node_xor(XListNode *a, XListNode *b) {
    return (((uintptr)a) ^ ((uintptr)b));
}

static void __xlist_add_node_before(XList *list, XListNode *node, XListNode *value) {
    if (list == NULL || value == NULL) {
        return;
    }


}

static void __xlist_add_node_start(XList *list, XListNode *value) {
    if (list == NULL || value == NULL) {
        return;
    }

    if (list->first == NULL) {
        list->first = value;
        list->last = value;
        value->xptr = NULL;
    } else {
        __xlist_add_node_before(list, list->first, value);
    }
}

void xlist_add_node(XList *list, XListNode *value) {
    if (list->last == NULL) {
        __xlist_add_node_start(list, value);
    } else {
        __xlist_add_node_end(list, list->last, value);
    }
}
