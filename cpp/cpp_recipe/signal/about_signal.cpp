// g++ -pthread about_signal.cpp
#include <chrono>
#include <condition_variable>
#include <csignal>
#include <iostream>
#include <mutex>
#include <signal.h>
#include <unistd.h>

using namespace std;

bool stop = false;
mutex mu;
condition_variable condition_var;

void signal_handler(int signum) {
    mu.lock();

    unique_lock<mutex> lock(mu);
    cout << "signal (" << signum << ") received." << endl;
    if (!stop) {
        cout << "stopping..." << endl;
        stop = true;
    } else {
        cout << "stopping has been triggered" << endl;
    }
    condition_var.notify_all();
}

void register_signal_handler() {
    for (uint32_t i = 0U; i < 20U; i++) {
        signal(i, signal_handler);
    }
}

int main() {
    cout << "begin" << endl;

    register_signal_handler();
    std::chrono::seconds wait_timeout_in_seconds(10);
    while (!stop) {
        unique_lock<std::mutex> lock(mu);
        cout << "pid: " << getpid() << " waitting..." << endl;
        condition_var.wait_for(lock, wait_timeout_in_seconds);
    }

    cout << "end" << endl;
    return 0;
}
