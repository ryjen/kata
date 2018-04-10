
coda_list
======

a linked list implementation

examples:

### creating a list
```c
CodaList *list = coda_list_new_single();
// list = rj_list_create_double();
```

### create a list item
```c
size_t data_size = ...
void *data = ...

/* create an item using stdlib memory function */
CodaListItem *item = coda_list_item_new(data, data_size, memcmp);

/* create an item with no memory functions, data will not be destroyed or copied */
item = coda_list_item_new_static(data, data_size, memcmp);

/* create an item with custom memory functions */
item = coda_list_item_new_transient(data, data_size, memcmp, malloc, free, memmove);
```

### add some data:
```c
coda_list_add(list, item);

coda_list_add_index(list, 1, item);

coda_list_set(list, 1, item);

coda_list_add_all(list, other_list);

coda_list_add_all_index(list, 1, other_list);
```

### remove some data
```c
coda_list_remove(list, data);

coda_list_remove_index(list, 1);

coda_list_remove_all(list, other_list);

coda_list_clear(list);
```

### some data exist?
```c
bool val = coda_list_contains(list, data);

val = coda_list_contains_all(list, other_list);
```

### get some data
```c
void *data = coda_list_get(list, 1);

size_t size = rj_list_get_size(list, 1);
```

### properties
```c
size_t list_size = coda_list_size(list);

int index = coda_list_index_of(list, data);

bool is_empty = coda_list_is_empty(list);
```

### sorting (mutable)
```c
coda_list_sort(list);
```


## TODO

- unit tests (sigh, no TDD)
- doubly linked list implementation

