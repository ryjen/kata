#ifndef CODA_KATA_LOG_H
#define CODA_KATA_LOG_H

#include <string.h>

#ifndef __attribute__
#define __attribute__(x)
#endif

/*! levels of logging */
typedef enum {
    /*! logging is disabled */
    CodaLogNone = 0,
    /*! only error messages will be logged */
    CodaLogError = 1,
    /*! warnings and errors will be logged */
    CodaLogWarn = 2,
    /*! info, warning, and error messages will be logged */
    CodaLogInfo = 3,
    /*! debug, info, warning and error messages will be logged */
    CodaLogDebug = 4,
    /*! trace, debug, info, warning and error messages will be logged */
    CodaLogTrace = 5
} CodaLogLevel;

void coda_log_error(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void coda_log_warn(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void coda_log_info(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void coda_log_debug(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void coda_log_trace(const char *const format, ...) __attribute__((format(printf, 1, 2)));

#define coda_log_errno(errnum) rj_log_error("%s:%d %s (%d)", __FILE__, __LINE__, strerror(errnum), errnum)

void coda_log_set_error(RJError **error, const char *const format, ...) __attribute__((format(printf, 2, 3)));

void coda_log_set_errno(RJError **error, int errnum);

#endif
