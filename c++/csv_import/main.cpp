#include "import.hpp"

int main(int argc, char *argv[])
{
    acl::RecordReader reader;
    acl::RecordWriter writer;

    if (argc < 3) {
        std::cout << "Syntax: " << argv[0] << " <input csv> <output file>" << std::endl;
        return EXIT_FAILURE;
    }

    if (!reader.open(argv[1])) {
        std::cerr << "Unable to open " << argv[1] << std::endl;
        return EXIT_FAILURE;
    }

    if (!reader.scanFieldSizes()) {
        std::cerr << "Unable to get size of records in " << argv[1] << std::endl;
        return EXIT_FAILURE;
    }

    if (!writer.open(argv[2])) {
        std::cerr << "Unable to open " << argv[2] << std::endl;
        return EXIT_FAILURE;
    }

    if (!writer.write(reader)) {
        std::cerr << "Unable to output " << argv[2] << std::endl;
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}