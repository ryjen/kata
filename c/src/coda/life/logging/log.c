#ifdef LOG_FUNCTIONS
#include <dlfcn.h>
#include <execinfo.h>
#endif
#include <rj/logging/error.h>
#include <rj/logging/log.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define LOG_BUF_SIZE BUFSIZ

const char *RJLogLevelNames[] = {"UNKNOWN", "ERROR", "WARN", "INFO", "DEBUG", "TRACE"};

RJLogLevel rj_current_log_level;

static inline int __rj_log_valid(RJLogLevel level, const char *str)
{
    return level <= rj_current_log_level && str && *str;
}

static void __rj_log_vargs(RJLogLevel level, const char *const format, va_list args)
{
    char buf[LOG_BUF_SIZE + 1] = {0};

    time_t t = 0;

    if (!format || !*format) {
        return;
    }

    t = time(0);

#ifdef LOG_FUNCTIONS

    const char *last_func = "unk";

    void *callstack[4];

    int frames = backtrace(callstack, 4);

    Dl_info info;

    if (frames > 0) {
        if (dladdr(callstack[2], &info) && info.dli_sname) {
            last_func = info.dli_sname;
        }
    }
#endif

    strftime(buf, BUFSIZ, "%Y-%m-%d %H:%M:%S", localtime(&t));

#ifdef LOG_FUNCTIONS
    fprintf(stdout, "%s %s: [%s] ", buf, RJLogLevelNames[level], last_func);
#else
    fprintf(stdout, "%s %s: ", buf, RJLogLevelNames[level]);
#endif

    vfprintf(stdout, format, args);
    fputs("\n", stdout);
    fflush(stdout);
}

void coda_log_error(const char *const format, ...)
{
    va_list args;

    if (!__rj_log_valid(RJLogError, format)) {
        return;
    }

    va_start(args, format);
    __rj_log_vargs(RJLogError, format, args);
    va_end(args);
}

void coda_log_warn(const char *const format, ...)
{
    va_list args;

    if (!__rj_log_valid(RJLogWarn, format)) {
        return;
    }

    va_start(args, format);
    __rj_log_vargs(RJLogWarn, format, args);
    va_end(args);
}

void coda_log_info(const char *const format, ...)
{
    va_list args;

    if (!__rj_log_valid(RJLogInfo, format)) {
        return;
    }

    va_start(args, format);
    __rj_log_vargs(RJLogInfo, format, args);
    va_end(args);
}

void coda_log_debug(const char *const format, ...)
{
    va_list args;

    if (!__rj_log_valid(RJLogDebug, format)) {
        return;
    }

    va_start(args, format);
    __rj_log_vargs(RJLogDebug, format, args);
    va_end(args);
}

void coda_log_trace(const char *const format, ...)
{
    va_list args;

    if (!__rj_log_valid(RJLogTrace, format)) {
        return;
    }

    va_start(args, format);
    __rj_log_vargs(RJLogTrace, format, args);
    va_end(args);
}

void coda_log_set_error(RJError **error, const char *const format, ...)
{
    va_list args;

    if (!format || !*format) {
        return;
    }

    va_start(args, format);

    if (error) {
        char buf[LOG_BUF_SIZE + 1] = {0};
        vsnprintf(buf, LOG_BUF_SIZE, format, args);
        *error = rj_error_with_message(buf);
    }

    if (RJLogError > rj_current_log_level) {
        va_end(args);
        return;
    }

    __rj_log_vargs(RJLogError, format, args);
    va_end(args);
}

void coda_log_set_errno(RJError **error, int errnum)
{
    if (error) {
        *error = rj_error_with_code_and_message(errnum, strerror(errnum));
    }

    coda_log_error("%d: %s", errnum, strerror(errnum));
}
