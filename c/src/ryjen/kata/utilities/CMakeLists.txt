
set(HEADERS
		string.h
)

set(SOURCES 
	string.c
	${HEADERS}
)

add_library(${STRINGS_LIB} ${SOURCES})

string(REPLACE "_" "/" INSTALL_DIRECTORY ${STRINGS_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

