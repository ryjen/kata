#include <errno.h>
#include <rj/logging/error.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "log.h"

struct __rj_error {
    int code;
    char *message;
};

RJError *rj_error_new()
{
    RJError *e = malloc(sizeof(RJError));

    if (e == NULL) {
        rj_log_errno(ENOMEM);
        return NULL;
    }

    e->code = 0;
    e->message = NULL;

    return e;
}

RJError *rj_error_with_message(const char *message)
{
    RJError *e = rj_error_new();

    if (e == NULL) {
        return NULL;
    }

    e->message = strdup(message);

    return e;
}

RJError *rj_error_with_code_and_message(int code, const char *message)
{
    RJError *e = rj_error_new();

    if (e == NULL) {
        return NULL;
    }

    e->code = code;
    e->message = strdup(message);

    return e;
}

void rj_error_free(RJError *e)
{
    if (!e) {
        return;
    }

    if (e->message) {
        free(e->message);
    }

    free(e);
}

const char *rj_error_message(RJError *error)
{
    return !error ? NULL : error->message;
}

void rj_error_set_message(RJError *error, const char *message)
{
    if (error) {
        error->message = strdup(message);
    }
}

int rj_error_code(RJError *error)
{
    return !error ? -1 : error->code;
}

void rj_error_set_code(RJError *error, int code)
{
    if (error) {
        error->code = code;
    }
}
