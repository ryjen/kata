
set(SOURCES 
	string.c
)

add_library(${STRINGS_LIB} ${SOURCES})

set(HEADERS
	string.h
)

string(REPLACE "_" "/" INSTALL_DIRECTORY ${STRINGS_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

