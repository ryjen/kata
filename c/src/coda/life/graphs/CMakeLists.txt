
set(HEADERS
		adjacency-matrix.h
		edge.h
		vertex.h
)

set(SOURCES 
	adjacency-matrix.c
	${HEADERS}
)

add_library(${GRAPHS_LIB} ${SOURCES})


string(REPLACE "_" "/" INSTALL_DIRECTORY ${GRAPHS_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

