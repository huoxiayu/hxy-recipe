#include <chrono>
#include <iostream>
#include <thread>
#include <typeinfo>

using namespace std;

using ull = unsigned long long;

ull fib(ull n) { return n < 2 ? n : fib(n - 1) + fib(n - 2); }

template <typename T> void about_chrono_clock() {
    string tname = typeid(T).name();
    cout << tname << " begin" << endl;

    using std::chrono::duration_cast;
    using std::chrono::microseconds;
    using std::chrono::milliseconds;
    using std::chrono::nanoseconds;
    using std::chrono::seconds;

    auto start = T::now();
    cout << "fib(41) is: " << fib(41) << endl;
    auto end = T::now();
    auto cost = end - start;

    // default in ns
    cout << "cost: " << cost.count() << endl;
    cout << "elapsed: " << duration_cast<seconds>(cost).count() << "s" << endl;
    cout << "elapsed: " << duration_cast<milliseconds>(cost).count() << "ms"
         << endl;
    cout << "elapsed: " << duration_cast<microseconds>(cost).count() << "us"
         << endl;
    cout << "elapsed: " << duration_cast<nanoseconds>(cost).count() << "ns"
         << endl;

    cout << tname << " end" << endl;
}

int main() {
    cout << "begin" << endl;

    for (int i = 0; i < 2; i++) {
        about_chrono_clock<std::chrono ::steady_clock>();
        about_chrono_clock<std::chrono ::high_resolution_clock>();
    }

    using namespace std::chrono_literals;

    auto start = std::chrono ::steady_clock::now();
    std::this_thread::sleep_for(std::chrono::seconds(1));
    std::this_thread::sleep_for(std::chrono::milliseconds(1000));

    std::this_thread::sleep_for(1s);
    std::this_thread::sleep_for(1000ms);
    std::this_thread::sleep_for(1000000us);
    std::this_thread::sleep_for(1000000000ns);
    auto end = std::chrono ::steady_clock::now();
    auto millis =
        std::chrono::duration_cast<std::chrono::milliseconds>(end - start)
            .count();
    cout << "cost " << millis << " ms" << endl;

    cout << "end" << endl;
    return 0;
}
