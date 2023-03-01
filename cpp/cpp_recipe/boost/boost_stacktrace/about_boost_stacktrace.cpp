// TODO: how to link boost correctly
#include <iostream>

#define BOOST_STACKTRACE_USE_ADDR2LINE
#include <boost/stacktrace.hpp>
using namespace std;

int main() {
    std::cerr << boost::stacktrace::stacktrace() << std::endl;

    return 0;
}
