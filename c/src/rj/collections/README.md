
rj_list
======

a sanitized singly linked list implementation

examples:

### creating a list
```c
	rj_list *list = rj_list_create();
```

### create a list item
```c
  size_t data_size = ...
  void *data = ...

  /* create an item using stdlib memory function */
	rj_list_item *item = rj_list_item_create(data, data_size, memcmp);

  /* create an item with no memory functions, data will not be destroyed or copied */
	item = rj_list_item_create_static(data, data_size, memcmp);

  /* create an item with custom memory functions */
	item = rj_list_item_create_transient(data, data_size, memcmp, malloc, free, memmove);
```

### add some data:
```c
	rj_list_add(list, item);

	rj_list_add_index(list, 1, item);

  rj_list_set(list, 1, item);

  rj_list_add_all(list, other_list);

  rj_list_add_all_index(list, 1, other_list);
```

### remove some data
```c
	rj_list_remove(list, data);

	rj_list_remove_index(list, 1);

	rj_list_remove_all(list, other_list);

  rj_list_clear(list);
```

### some data exist?
```c
  bool val = rj_list_contains(list, data);

  val = rj_list_contains_all(list, other_list);
```

### get some data
```c
  void *data = rj_list_get(list, 1);

  size_t size = rj_list_get_size(list, 1);
```

### properties
```c
  size_t list_size = rj_list_size(list);

  int index = rj_list_index_of(list, data);

  bool is_empty = rj_list_is_empty(list);
```

### sorting (mutable)
```c
  rj_list_sort(list);
```

