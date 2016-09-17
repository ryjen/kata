#include <sys/time.h>
#import <iomanip>
#include <sstream>
#include "import.hpp"

#define TEST_FILE "test.csv"

#define TEST_OUTPUT "test.record"

#define NUMBER_OF_RECORDS 100000

namespace acl
{
    bool generate_random_string(const acl::File &file)
    {
        static const char alphanum[] =
            "0123456789"
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ,"
            "abcdefghijklmnopqrstuvwxyz,";

        int length = rand() % 25 + 5;

        if (!file.write('"')) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!file.write(alphanum[rand() % (sizeof(alphanum) - 1)])) {
                return false;
            }
            // add random new line
            if ((rand() % 100000) < 100) {
                if (!file.write("\r\n")) {
                    return false;
                }
            }
        }
        if (!file.write('"')) {
            return false;
        }
        return true;
    }

    bool generate_random_number(const acl::File &file)
    {
        double num = 100.0 + (rand() / (RAND_MAX / (100000.0 - 100.0)));
        std::ostringstream out;
        out << std::fixed << std::setprecision(2) << num;
        file.write(out.str());
        return true;
    }

    bool generate_test_record(const acl::File &file)
    {
        for (int i = 0; i < 5; i++) {
            if (!generate_random_string(file)) {
                return false;
            }
            if (!file.write(',')) {
                return false;
            }
        }
        if (!generate_random_number(file)) {
            return false;
        }

        return file.write("\r\n");
    }

    bool generate_test_csv(const std::string &fileName)
    {
        acl::File file(O_WRONLY | O_CREAT | O_TRUNC | O_LARGEFILE);

        if (!file.open(fileName)) {
            return false;
        }

        for (long i = 0; i < NUMBER_OF_RECORDS; i++) {
            if (!generate_test_record(file)) {
                return false;
            }
        }
        return true;
    }

    typedef struct {
        struct timeval begin, end;
        void start()
        {
            gettimeofday(&begin, NULL);
        }
        void stop()
        {
            gettimeofday(&end, NULL);
        }

        long usec() const
        {
            return ((end.tv_sec - begin.tv_sec) * 1000000 + end.tv_usec - begin.tv_usec);
        }
    } Timer;

    std::ostream &operator<<(std::ostream &out, const Timer &timer)
    {
        long usec = timer.usec();
        long sec = usec / 1000000;
        long rem = usec % 1000000;
        out << sec << "." << std::setfill('0') << std::setw(6) << rem;
        return out;
    }
}

int main(int argc, char *argv[])
{
    acl::RecordReader reader;
    acl::RecordWriter writer;
    acl::Timer timer;
    acl::Timer task;

    srand(time(0));

    std::cout << "Generating test file for " << NUMBER_OF_RECORDS << " records... " << std::flush;
    task.start();

    if (!acl::generate_test_csv(TEST_FILE)) {
        return EXIT_FAILURE;
    }

    task.stop();
    std::cout << task << std::endl << std::endl;

    if (!reader.open(TEST_FILE)) {
        std::cerr << "Unable to open test file" << std::endl;
        return EXIT_FAILURE;
    }

    std::cout << "Getting record sizes... " << std::flush;
    timer.start();
    task.start();

    if (!reader.scanFieldSizes()) {
        std::cerr << "Unable to get size of records" << std::endl;
        return EXIT_FAILURE;
    }

    task.stop();
    std::cout << task << std::endl;

    std::cout << "Converting records... " << std::flush;
    task.start();

    if (!writer.open(TEST_OUTPUT)) {
        std::cerr << "Unable to open test file" << std::endl;
        return EXIT_FAILURE;
    }

    if (!writer.write(reader)) {
        std::cerr << "Unable to write test file" << std::endl;
        return EXIT_FAILURE;
    }

    task.stop();
    std::cout << task << std::endl;

    timer.stop();
    std::cout << "Total " << timer << std::endl;

    return EXIT_SUCCESS;
}