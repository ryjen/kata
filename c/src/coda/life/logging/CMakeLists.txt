
set(HEADERS
		error.h
		log.h
)

set(SOURCES 
	log.c
	error.c
	${HEADERS}
)

include(CheckIncludeFile)
include(CheckFunctionExists)

check_include_file(dlfcn.h FOUND_DLFCN_H)
check_include_file(execinfo.h FOUND_EXECINFO_H)
check_function_exists(dladdr FOUND_DLADDR)
check_function_exists(backtrace FOUND_BACKTRACE)

if(FOUND_DLFCN_H AND FOUND_EXECINFO_H AND FOUND_DLADDR AND FOUND_BACKTRACE)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DLOG_FUNCTIONS")
endif()

add_library(${LOGGING_LIB} ${SOURCES})

string(REPLACE "_" "/" INSTALL_DIRECTORY ${LOGGING_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

