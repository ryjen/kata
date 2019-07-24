/*!
 * @file
 * @header Error Information
 * Functions for dealing with errors
 */
#ifndef CODA_KATA_ERROR_H

/*! @parseOnly */
#define CODA_KATA_ERROR_H

typedef struct __coda_error CodaError;

/*!
 * allocates a new empty error
 * @return an allocated error or NULL
 */
CodaError *coda_error_new();

/*!
 * allocates an error with a message
 * @param message the message for the error
 * @return the allocated error or NULL
 */
CodaError *coda_error_with_message(const char *message);

/*!
 * allocates an error with a code and message
 * @param code the error code
 * @param message the error message
 * @return the allocated error
 */
CodaError *coda_error_with_code_and_message(int code, const char *message);

/*!
 * deallocates an error
 * @param error the error instance to deallocate
 */
void coda_error_free(CodaError *error);

/*!
 * gets the message for an error
 * @param error the error instance
 * @return the error message or NULL
 */
const char *coda_error_message(CodaError *error);

/*!
 * sets the message for an error
 * @param error the error instance
 * @param message the message to set
 */
void coda_error_set_message(CodaError *error, const char *message);

/*!
 * gets the code for an error
 * @param error the error instance
 * @return the error code or zero
 */
int coda_error_code(CodaError *error);

/*!
 * sets the code for an error
 * @param error the error instance
 * @param code the code to set
 */
void coda_error_set_code(CodaError *error, int code);

#endif
