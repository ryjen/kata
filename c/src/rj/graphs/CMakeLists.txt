
set(SOURCES 
	adjacency-matrix.c
)

add_library(${GRAPHS_LIB} ${SOURCES})

set(HEADERS
	adjacency-matrix.h
	common.h
)

string(REPLACE "_" "/" INSTALL_DIRECTORY ${GRAPHS_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

