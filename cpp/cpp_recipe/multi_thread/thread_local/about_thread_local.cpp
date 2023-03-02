#include <chrono>
#include <iostream>
#include <mutex>
#include <string>
#include <thread>

unsigned int v = 0;
thread_local unsigned int thread_local_v = 0;

void go(const std::string &thread_name) {
    int add = 1;
    v += add;
    thread_local_v += add;
    std::cout << thread_name << " -> v: " << v
              << ", thread_local_v: " << thread_local_v << std::endl;
    std::cout << "&add: " << &add << ", &v: " << &v << ", "
              << "&thread_local_v: " << &thread_local_v << std::endl;

    std::this_thread::sleep_for(std::chrono::seconds(1));
}

int main() {
    std::thread a(go, "thread_a");
    a.join();

    std::thread b(go, "thread_b");
    b.join();

    go("thread_main");
}
