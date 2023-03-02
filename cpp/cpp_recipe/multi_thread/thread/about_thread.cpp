#include <chrono>
#include <iostream>
#include <pthread.h>
#include <thread>

using namespace std;

void foo() {
    std::thread::id this_id = std::this_thread::get_id();
    cout << this_id << " foo begin " << endl;
    std::this_thread::sleep_for(std::chrono::seconds(1));
    cout << this_id << " foo end " << endl;
}

void bar(uint32_t seconds) {
    std::thread::id this_id = std::this_thread::get_id();
    cout << this_id << " bar begin " << endl;
    std::this_thread::sleep_for(std::chrono::seconds(seconds));
    cout << this_id << " bar end " << endl;
}

void thread_property(string th_name) {
    const int len = 32;
    char thread_name[len];

    pthread_getname_np(pthread_self(), thread_name, len);
    cout << "thread name is: " << thread_name << endl;

    pthread_setname_np(pthread_self(), th_name.c_str());

    pthread_getname_np(pthread_self(), thread_name, len);
    cout << "thread name is: " << thread_name << endl;
}

int main() {
    cout << "begin" << endl;

    thread thread1(foo);
    thread thread2(bar, 2);

    thread1.join();
    thread2.join();

    thread thread3(thread_property, "thread_name_x");
    thread3.join();

    thread thread4(thread_property, "thread_name_y");
    thread4.join();

    cout << "end" << endl;
    return 0;
}
