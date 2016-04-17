
add_library(${PROJECT_NAME}-collections
	collections/list-item.c
	collections/list-single.c
)

set(COLLECTION_HEADERS
	collections/list-item.h
	collections/list-single.h
	collections/list.h
)

install(FILES ${COLLECTION_HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${PROJECT_NAME}/collections")
