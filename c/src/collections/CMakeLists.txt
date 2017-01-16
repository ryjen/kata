
add_library(${PROJECT_NAME}-collections
	list-item.c
	list-single.c
)

set(COLLECTION_HEADERS
	list-item.h
	list-single.h
	list.h
)

install(FILES ${COLLECTION_HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${PROJECT_NAME}/collections")

