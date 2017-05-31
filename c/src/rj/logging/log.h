#ifndef RJ_KATA_LOG_H
#define RJ_KATA_LOG_H

#include <string.h>

#ifndef __attribute__
#define __attribute__(x)
#endif

/*! levels of logging */
typedef enum {
    /*! logging is disabled */
    RJLogNone = 0,
    /*! only error messages will be logged */
    RJLogError = 1,
    /*! warnings and errors will be logged */
    RJLogWarn = 2,
    /*! info, warning, and error messages will be logged */
    RJLogInfo = 3,
    /*! debug, info, warning and error messages will be logged */
    RJLogDebug = 4,
    /*! trace, debug, info, warning and error messages will be logged */
    RJLogTrace = 5
} RJLogLevel;

void rj_log_error(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void rj_log_warn(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void rj_log_info(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void rj_log_debug(const char *const format, ...) __attribute__((format(printf, 1, 2)));

void rj_log_trace(const char *const format, ...) __attribute__((format(printf, 1, 2)));

#define rj_log_errno(errnum) rj_log_error("%s:%d %s (%d)", __FILE__, __LINE__, strerror(errnum), errnum)

void rj_log_set_error(RJError **error, const char *const format, ...) __attribute__((format(printf, 2, 3)));

void rj_log_set_errno(RJError **error, int errnum);

#endif
