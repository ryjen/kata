cmake_minimum_required (VERSION 3.1)
set(CMAKE_CXX_STANDARD 11)

# add options for testing
option(ENABLE_COVERAGE "Enable code coverage testing." OFF)
option(ENABLE_MEMCHECK "Enable testing for memory leaks." OFF)
option(ENABLE_PROFILING "Enable profiling code usage." OFF)

# define project name
project (kata VERSION 0.1)

# set path to custom modules
list(APPEND CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake)

# create the package config install
include(CreatePackage)
create_package("code kata")

set(COLLECTIONS_LIB ${PROJECT_NAME}-collections)
set(ALGORITHMS_LIB ${PROJECT_NAME}-algorithms)

set(KATA_TEST ${PROJECT_NAME}-test)

# add directories
add_subdirectory(src)
add_subdirectory(tests)

# Setup testing
enable_testing()


if(ENABLE_COVERAGE)
	include(CodeCoverage)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${CMAKE_CXX_FLAGS_COVERAGE}")
	setup_target_for_coverage(TARGET ${KATA_TEST} OUTPUT ${PROJECT_BINARY_DIR}/coverage)
endif()

include(ValgrindTest)

add_valgrind_profile_test(MEMCHECK ${ENABLE_MEMCHECK} PROFILING ${ENABLE_PROFILING} TARGET ${KATA_TEST})


