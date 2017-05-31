/*!
 * @file
 * @header Error Information
 * Functions for dealing with errors
 */
#ifndef RJ_KATA_ERROR_H

/*! @parseOnly */
#define RJ_KATA_ERROR_H

typedef struct __rj_error RJError;

/*!
 * allocates a new empty error
 * @return an allocated error or NULL
 */
RJError *rj_error_new();

/*!
 * allocates an error with a message
 * @param message the message for the error
 * @return the allocated error or NULL
 */
RJError *rj_error_with_message(const char *message);

/*!
 * allocates an error with a code and message
 * @param code the error code
 * @param message the error message
 * @return the allocated error
 */
RJError *rj_error_with_code_and_message(int code, const char *message);

/*!
 * deallocates an error
 * @param error the error instance to deallocate
 */
void rj_error_free(RJError *error);

/*!
 * gets the message for an error
 * @param error the error instance
 * @return the error message or NULL
 */
const char *rj_error_message(RJError *error);

/*!
 * sets the message for an error
 * @param error the error instance
 * @param message the message to set
 */
void rj_error_set_message(RJError *error, const char *message);

/*!
 * gets the code for an error
 * @param error the error instance
 * @return the error code or zero
 */
int rj_error_code(RJError *error);

/*!
 * sets the code for an error
 * @param error the error instance
 * @param code the code to set
 */
void rj_error_set_code(RJError *error, int code);

#endif
