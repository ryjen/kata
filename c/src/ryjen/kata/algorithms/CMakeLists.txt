
set(SOURCES
        math.c
)

add_library(${ALGORITHMS_LIB} ${SOURCES})

set(HEADERS
		math.h
)

string(REPLACE "_" "/" INSTALL_DIRECTORY ${ALGORITHMS_LIB})

install(FILES ${HEADERS} DESTINATION "${CMAKE_INSTALL_PREFIX}/include/${INSTALL_DIRECTORY}")

add_executable(dvr dvr.c)


