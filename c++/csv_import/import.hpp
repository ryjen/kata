#ifndef IMPORT_GUARD
#define IMPORT_GUARD
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <vector>

#ifdef WIN32
#include "win_compat.hpp"
#else
#include <unistd.h>
#endif
#include <fcntl.h>

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

        int fd_;
        int mode_;

       public:
        File(int mode) : fd_(-1), mode_(mode)
        {
        }

        virtual ~File()
        {
            if (fd_ != -1) {
                ::close(fd_);
                fd_ = -1;
            }
        }

        bool open(const std::string &fileName)
        {
            fd_ = ::open(fileName.c_str(), mode_, 0666);

            if (fd_ == -1) {
                perror("unable to open");
                return false;
            }

            return true;
        }

        ReadState read(char &ch) const
        {
            ReadState state;

            int rv = ::read(fd_, &ch, 1);

            // check for error
            if (rv < 0) {
                if (EINTR != errno && EAGAIN != errno) {
                    perror("read");
                }
                state.error = errno;
                return state;
            }

            // check for end of file
            if (rv == 0) {
                state.eof = true;
            }

            return state;
        }

        bool reset() const
        {
            if (fd_ == -1) {
                return false;
            }
            if (::lseek(fd_, 0, SEEK_SET) == -1) {
                perror("seek failure");
                return false;
            }
            return true;
        }

        bool write(const std::string &value) const
        {
            if (fd_ == -1) {
                return false;
            }

            int rv = ::write(fd_, value.c_str(), value.length());

            if (rv < 0) {
                perror("write");
            }
            return rv >= 0;
        }
        bool write(const char &value) const
        {
            if (fd_ == -1) {
                return false;
            }

            int rv = ::write(fd_, &value, 1);

            if (rv < 0) {
                perror("write");
            }
            return rv >= 0;
        }

        bool isOpen() const
        {
            return fd_ != -1;
        }
    };

    /**
     * reads a csv to build records
     */
    class RecordReader : public File
    {
       public:
        RecordReader() : File(O_RDONLY | O_LARGEFILE)
        {
        }

        /**
         * get the size of a scanned field
         */
        size_t fieldSize(size_t i) const
        {
            if (i >= columnSizes_.size()) {
                return 0;
            }
            return columnSizes_[i];
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
        RecordWriter() : File(O_WRONLY | O_CREAT | O_TRUNC | O_LARGEFILE)
        {
        }

        /**
         * write a reader to file
         * @param reader the reader to read from
         * @return true if successful
         */
        bool write(const RecordReader &reader)
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

            if (fd_ == -1) {
                return false;
            }

            switch (field.type) {
                case Field::String:
                    // left align
                    rv = dprintf(fd_, "%*s ", -size, field.value.c_str());
                    break;
                case Field::Integer:
                case Field::Decimal:
                    // right align
                    rv = dprintf(fd_, "%*s ", size, field.value.c_str());
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
