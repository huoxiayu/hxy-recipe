#include <algorithm>
#include <iomanip>
#include <iostream>
#include <sys/time.h>
#include <sys/types.h>
#include <time.h>
#include <unistd.h>

int main() {
    struct timeval tv;
    gettimeofday(&tv, NULL);
    long time_diff_in_secs = 28800;
    tv.tv_sec += time_diff_in_secs;
    struct tm tm_time;
    gmtime_r(((time_t *)&(tv.tv_sec)), &tm_time);
    std::cout << std::setfill('0') << std::setw(2) << (tm_time.tm_mon + 1)
              << std::setw(2) << tm_time.tm_mday << ' ' << std::setw(2)
              << tm_time.tm_hour << ':' << std::setw(2) << tm_time.tm_min << ':'
              << std::setw(2) << tm_time.tm_sec << '.' << std::setw(6)
              << tv.tv_usec << ' ' << static_cast<uint32_t>(getpid());

    return 0;
}
