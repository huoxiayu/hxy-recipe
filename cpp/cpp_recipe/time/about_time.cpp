#include <chrono>
#include <iostream>
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

    auto start = T::now();
    cout << "fib(42) is: " << fib(42) << endl;
    auto end = T::now();
    auto seconds = end - start;
    cout << "elapsed: " << seconds.count() << "s" << endl;
    cout << "elapsed: " << duration_cast<milliseconds>(seconds).count() << "ms"
         << endl;
    cout << "elapsed: " << duration_cast<microseconds>(seconds).count() << "us"
         << endl;
    cout << "elapsed: " << duration_cast<nanoseconds>(seconds).count() << "ns"
         << endl;

    cout << tname << " end" << endl;
}

int main() {
    cout << "begin" << endl;

    for (int i = 0; i < 3; i++) {
        about_chrono_clock<std::chrono ::steady_clock>();
        about_chrono_clock<std::chrono ::high_resolution_clock>();
    }

    cout << "end" << endl;
    return 0;
}
