#include <errno.h>
#include <rj/logging/error.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "log.h"

struct __coda_error {
    int code;
    char *message;
};

CodaError *coda_error_new()
{
    CodaError *e = malloc(sizeof(CodaError));

    if (e == NULL) {
        coda_log_errno(ENOMEM);
        return NULL;
    }

    e->code = 0;
    e->message = NULL;

    return e;
}

CodaError *coda_error_with_message(const char *message)
{
    CodaError *e = coda_error_new();

    if (e == NULL) {
        return NULL;
    }

    e->message = strdup(message);

    return e;
}

CodaError *coda_error_with_code_and_message(int code, const char *message)
{
    CodaError *e = coda_error_new();

    if (e == NULL) {
        return NULL;
    }

    e->code = code;
    e->message = strdup(message);

    return e;
}

void coda_error_free(struct __coda_error *e)
{
    if (!e) {
        return;
    }

    if (e->message) {
        free(e->message);
    }

    free(e);
}

const char *coda_error_message(struct __coda_error *error)
{
    return !error ? NULL : error->message;
}

void coda_error_set_message(struct __coda_error *error, const char *message)
{
    if (error) {
        error->message = strdup(message);
    }
}

int coda_error_code(struct __coda_error *error)
{
    return !error ? -1 : error->code;
}

void coda_error_set_code(struct __coda_error *error, int code)
{
    if (error) {
        error->code = code;
    }
}
