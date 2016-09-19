#ifndef IMPORT_GUARD
#define IMPORT_GUARD
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <vector>

#ifdef _WIN32
#include "win_compat.hpp"
#else
#include <unistd.h>
#endif
#include <fcntl.h>

#ifndef O_LARGEFILE
#define O_LARGEFILE 0
#endif

namespace acl
{
    /**
     * a field in a record
     * has a value and a type
     */
    typedef struct {
        typedef enum { String, Integer, Decimal } Type;
        std::string value;
        Type type;
    } Field;

    /**
     * a file to read or write
     */
    class File
    {
       protected:
        /**
         * a state of reading
         */
        typedef struct rs {
            bool eof;
            bool eor;
            int error;
            rs() : eof(false), eor(false), error(0)
            {
            }
            operator bool()
            {
                return !eof && error == 0;
            }
        } ReadState;

        FILE *fp_;
        std::string mode_;

       public:
        /**
         * default constructor
         * @param mode the mode of the file
         */
        File(const std::string &mode = "r+") : fp_(NULL), mode_(mode)
        {
        }

        virtual ~File()
        {
            if (fp_ != NULL) {
                fclose(fp_);
                fp_ = NULL;
            }
        }

        /**
         * opens a file name
         * @param fileName the file to open
         * @param perm the permision to use if created
         * @return true if successful
         */
        bool open(const std::string &fileName, int perm = 0666)
        {
            fp_ = fopen(fileName.c_str(), mode_.c_str());

            if (fp_ == NULL) {
                perror("unable to open");
                return false;
            }

            return true;
        }

        /**
         * reads a character
         * @param ch a reference to the character to read into
         * @return the read state
         */
        ReadState read(char &ch) const
        {
            ReadState state;

            ch = fgetc(fp_);

            state.error = ferror(fp_);
            // check for error
            if (state.error) {
                perror("read");
                return state;
            }

            // check for end of file
            if (ch == EOF) {
                state.eof = true;
            }

            return state;
        }

        /**
         * resets the file cursor
         */
        bool reset(off_t pos = 0, int type = SEEK_SET) const
        {
            if (fp_ == NULL) {
                return false;
            }
            if (fseek(fp_, pos, type) == -1) {
                perror("seek failure");
                return false;
            }
            return true;
        }

        /**
         * write a string
         * @param value the string to write
         * @return true if successful
         */
        bool write(const std::string &value) const
        {
            if (fp_ == NULL) {
                return false;
            }

            int rv = fputs(value.c_str(), fp_);

            if (rv < 0) {
                perror("write");
            }
            return rv >= 0;
        }

        /**
         * write a character
         * @param value the value to write
         * @return true if successful
         */
        bool write(const char &value) const
        {
            if (fp_ == NULL) {
                return false;
            }

            int rv = fputc(value, fp_);

            if (rv < 0) {
                perror("write");
            }
            return rv >= 0;
        }

        /**
         * tests if the file is open
         * @return true if open
         */
        bool isOpen() const
        {
            return fp_ != NULL;
        }
    };

    /**
     * reads a csv to build records
     */
    class CSVReader : public File
    {
       public:
        CSVReader() : File("r")
        {
        }

        /**
         * get the size of a scanned field
         */
        int fieldSize(size_t i) const
        {
            if (i >= columnSizes_.size()) {
                return 0;
            }
            return static_cast<int>(columnSizes_[i]);
        }

        /**
         * scans the file to determine the size of the fields
         */
        bool scanFieldSizes()
        {
            ReadState state;
            std::vector<Field> record;

            // go to the begining of the file
            if (!reset()) {
                return false;
            }

            // for each record...
            while ((state = read(record))) {
                // and each field...
                for (size_t i = 0; i < record.size(); i++) {
                    Field field = record[i];

                    // set the maximum size
                    if (columnSizes_.size() <= i) {
                        columnSizes_.push_back(field.value.size());
                    } else {
                        if (columnSizes_[i] < field.value.size()) {
                            columnSizes_[i] = field.value.size();
                        }
                    }
                }
            }

            if (state.error) {
                return false;
            }
            return true;
        }

       private:
        static const char FIELD_SEPARATOR = ',';
        static const char RECORD_SEPARATOR = '\r';
        static const char STRING_DELIM = '"';

        /**
         * reads a record
         * @param record the record to add fields to
         * @return the read state
         */
        ReadState read(std::vector<Field> &record) const
        {
            ReadState state;
            Field field;

            record.clear();

            // for each field
            while ((state = read(field))) {
                // add to the record
                record.push_back(field);

                // check for end of record
                if (state.eor) {
                    break;
                }
            }

            // if end of file or other error, return the state
            if (state.eof || state.error) {
                return state;
            }

            // return valid state
            return ReadState();
        }

        /**
         * reads a field
         * @param field the field to read into
         * @return the read state
         */
        ReadState read(Field &field) const
        {
            ReadState state;
            char c = 0;
            char next = ',';

            // reset the field
            field.value.clear();
            field.type = Field::Integer;

            while ((state = File::read(c))) {
                if (state.error) {
                    if (EINTR == state.error || EAGAIN == state.error) {
                        continue;
                    }
                    return state;
                }

                // check for end of file
                if (state.eof) {
                    break;
                }

                // could get a newline in a string
                // just skip
                if (c == '\n') {
                    continue;
                }

                // end of a record?
                if (c == RECORD_SEPARATOR) {
                    // if we're in the middle of a string
                    if (next == STRING_DELIM) {
                        // append a space instead of a newline
                        field.value += ' ';
                        continue;
                    }
                    // otherwise we're at the end of a record
                    state.eor = true;
                    break;
                }
                // found a character we're looking for?
                if (c == next) {
                    switch (next) {
                        case STRING_DELIM:
                            // done reading a string, look for end of field
                            next = FIELD_SEPARATOR;
                            continue;
                        case FIELD_SEPARATOR:
                            // we've read a field successfully, return the state
                            return state;
                    }
                }
                // if we're not currently reading a string
                if (next != STRING_DELIM) {
                    switch (c) {
                        case STRING_DELIM:
                            // start reading a string
                            next = STRING_DELIM;
                            field.type = Field::String;
                            continue;
                        case '.':
                            // set for decimal type
                            field.type = Field::Decimal;
                            break;
                    }
                }

                // default action, append to field value
                field.value += c;
            }

            return state;
        }

        friend class RecordWriter;
        std::vector<size_t> columnSizes_;
    };

    /**
     * writes to a file
     */
    class RecordWriter : public File
    {
       public:
        RecordWriter() : File("w")
        {
        }

        /**
         * write a reader to file
         * @param reader the reader to read from
         * @return true if successful
         */
        bool write(const CSVReader &reader)
        {
            ReadState state;
            std::vector<Field> record;

            // make sure reading from beginging
            if (!reader.reset()) {
                return false;
            }

            // for each record...
            while ((state = reader.read(record))) {
                // and each field....
                for (size_t i = 0; i < record.size(); i++) {
                    // write to file
                    if (!write(record[i], reader.fieldSize(i))) {
                        return false;
                    }
                }
                // add record separator
                if (!File::write("\r\n")) {
                    return false;
                }
            }

            // error check
            if (state.error) {
                return false;
            }

            return true;
        }

        /**
         * writes a field to file
         * @param field the field to write
         * @param size the size of the field
         * @return true if successful
         */
        bool write(const Field &field, int size) const
        {
            int rv = 0;

            if (!isOpen()) {
                return false;
            }

            switch (field.type) {
                case Field::String:
                    // left align
                    rv = fprintf(fp_, "%*s ", -size, field.value.c_str());
                    break;
                case Field::Integer:
                case Field::Decimal:
                    // right align
                    rv = fprintf(fp_, "%*s ", size, field.value.c_str());
                    break;
            }

            if (rv < 0) {
                perror("write");
            }
            return rv >= 0;
        }
    };
}

#endif
