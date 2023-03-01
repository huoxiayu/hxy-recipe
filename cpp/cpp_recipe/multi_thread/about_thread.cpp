#include <chrono>
#include <iostream>
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

int main() {
    cout << "begin" << endl;

    thread thread1(foo);
    thread thread2(bar, 2);

    thread1.join();
    thread2.join();

    cout << "end" << endl;
    return 0;
}
