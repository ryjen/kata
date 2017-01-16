add_library(${PROJECT_NAME}-algorithms
	euclid.c
)

set(ALGORITHM_HEADERS
	euclid.h
)

install(FILES ${ALGORITHM_HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${PROJECT_NAME}/algorithms")

