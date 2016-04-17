

add_executable (${PROJECT_NAME}-collections-test 
	collections/list-test.c
)

include_directories(${PROJECT_SOURCE_DIR}/src)

target_link_libraries(${PROJECT_NAME}-collections-test ${PROJECT_NAME}-collections cmocka)

