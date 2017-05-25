
file(GLOB_RECURSE ALGORITHMS_SRC ${CMAKE_CURRENT_SOURCE_DIR}/algorithms/*.c)
file(GLOB_RECURSE COLLECTIONS_SRC ${CMAKE_CURRENT_SOURCE_DIR}/collections/*.c)

add_executable(${KATA_TEST}
	kata.c
	challenge.c
	${ALGORITHMS_SRC}
	${COLLECTIONS_SRC}
)

target_include_directories(${KATA_TEST} PUBLIC ${PROJECT_SOURCE_DIR}/src)

target_link_libraries(${KATA_TEST} ${ALGORITHMS_LIB} ${COLLECTIONS_LIB} cmocka)


