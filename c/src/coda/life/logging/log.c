#ifdef LOG_FUNCTIONS
#include <dlfcn.h>
#include <execinfo.h>
#endif

#include <coda/life/logging/error.h>
#include <coda/life/logging/log.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define LOG_BUF_SIZE BUFSIZ

const char *CodaLogLevelNames[] = {"UNKNOWN", "ERROR", "WARN", "INFO", "DEBUG", "TRACE"};

CodaLogLevel coda_current_log_level;

static inline int __coda_log_valid(CodaLogLevel level, const char *str) {
    return level <= coda_current_log_level && str && *str;
}

static void __coda_log_vargs(CodaLogLevel level, const char *const format, va_list args) {
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
    fprintf(stdout, "%s %s: [%s] ", buf, CodaLogLevelNames[level], last_func);
#else
    fprintf(stdout, "%s %s: ", buf, CodaLogLevelNames[level]);
#endif

    vfprintf(stdout, format, args);
    fputs("\n", stdout);
    fflush(stdout);
}

void coda_log_error(const char *const format, ...) {
    va_list args;

    if (!__coda_log_valid(CodaLogError, format)) {
        return;
    }

    va_start(args, format);
    __coda_log_vargs(CodaLogError, format, args);
    va_end(args);
}

void coda_log_warn(const char *const format, ...) {
    va_list args;

    if (!__coda_log_valid(CodaLogWarn, format)) {
        return;
    }

    va_start(args, format);
    __coda_log_vargs(CodaLogWarn, format, args);
    va_end(args);
}

void coda_log_info(const char *const format, ...) {
    va_list args;

    if (!__coda_log_valid(CodaLogInfo, format)) {
        return;
    }

    va_start(args, format);
    __coda_log_vargs(CodaLogInfo, format, args);
    va_end(args);
}

void coda_log_debug(const char *const format, ...) {
    va_list args;

    if (!__coda_log_valid(CodaLogDebug, format)) {
        return;
    }

    va_start(args, format);
    __coda_log_vargs(CodaLogDebug, format, args);
    va_end(args);
}

void coda_log_trace(const char *const format, ...) {
    va_list args;

    if (!__coda_log_valid(CodaLogTrace, format)) {
        return;
    }

    va_start(args, format);
    __coda_log_vargs(CodaLogTrace, format, args);
    va_end(args);
}

void coda_log_set_error(CodaError **error, const char *const format, ...) {
    va_list args;

    if (!format || !*format) {
        return;
    }

    va_start(args, format);

    if (error) {
        char buf[LOG_BUF_SIZE + 1] = {0};
        vsnprintf(buf, LOG_BUF_SIZE, format, args);
        *error = coda_error_with_message(buf);
    }

    if (CodaLogError > coda_current_log_level) {
        va_end(args);
        return;
    }

    __coda_log_vargs(CodaLogError, format, args);
    va_end(args);
}

void coda_log_set_errno(CodaError **error, int errnum) {
    if (error) {
        *error = coda_error_with_code_and_message(errnum, strerror(errnum));
    }

    coda_log_error("%d: %s", errnum, strerror(errnum));
}
