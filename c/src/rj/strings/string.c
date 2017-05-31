#include <rj/strings/string.h>

inline int rj_str_empty(const char *str)
{
    return !str || !*str;
}
