#include <coda/life/utilities/string.h>

inline int coda_str_empty(const char *str)
{
    return !str || !*str;
}
