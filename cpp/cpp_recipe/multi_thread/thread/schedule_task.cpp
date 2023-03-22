#include <chrono>
#include <cstdlib>
#include <iostream>
#include <thread>

void task() {
    char const *thread_name = "task_thread";
#if defined(__APPLE__)
    pthread_setname_np(thread_name);
#else
    pthread_setname_np(pthread_self(), thread_name);
#endif
    int times = 3;
    while (times-- > 0) {
        std::cout << "**********begin**********" << std::endl;
        int ret = system("ls");
        if (ret != 0) {
            std::cout << "ret error: " << ret << std::endl;
        }
        std::cout << "***********end***********" << std::endl << std::endl;
        std::this_thread::sleep_for(std::chrono::milliseconds(1000));
    }
}

int main() {
    auto lambda = []() { task(); };
    std::thread task_thread(lambda);
    task_thread.join();
    return 0;
}