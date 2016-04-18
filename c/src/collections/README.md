
a3list
======

a singly linked list implementation

examples:

### creating a list
```c
	a3list *list = a3list_create();
```

### create a list item
```c
  size_t data_size = ...
  void *data = ...

  /* create an item using stdlib memory function */
	a3list_item *item = a3list_item_create(data, data_size, memcmp);

  /* create an item with no memory functions, data will not be destroyed or copied */
	item = a3list_item_create_static(data, data_size, memcmp);

  /* create an item with custom memory functions */
	item = a3list_item_create_transient(data, data_size, memcmp, malloc, free, memmove);
```

### add some data:
```c
	a3list_add(list, item);

	a3list_add_index(list, 1, item);

  a3list_set(list, 1, item);
```

### remove some data
```c
	a3list_remove(list, data);

	a3list_remove_index(list, 1);

	a3list_remove_all(list, other_list);

  a3list_clear(list);
```

### some data exist?
```c
  bool val = a3list_contains(list, data);

  val = a3list_contains_all(list, other_list);
```

### get some data
```c
  void *data = a3list_get(list, 1);

  size_t size = a3list_get_size(list, 1);
```

### properties
```c
  size_t list_size = a3list_size(list);

  int index = a3list_index_of(list, data);

  bool is_empty = a3list_is_empty(list);
```

### sorting
```c
  a3list_sort(list);
```

