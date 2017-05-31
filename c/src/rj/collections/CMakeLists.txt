
set(SOURCES 
	list-item.c 
	list-single.c
	list.c
)

add_library(${COLLECTIONS_LIB} ${SOURCES})

set(HEADERS
	list-item.h
	list-vtable.h
	list.h
)

string(REPLACE "_" "/" INSTALL_DIRECTORY ${COLLECTIONS_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

