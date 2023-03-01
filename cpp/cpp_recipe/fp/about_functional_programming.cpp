#include <chrono>
#include <iostream>
#include <string>
#include <thread>
using namespace std;

template <typename FUNC> void wrap(FUNC func, string prompt) {
    cout << prompt << " begin" << endl;
    auto start = std::chrono::high_resolution_clock::now();
    func();
    auto end = std::chrono::high_resolution_clock::now();
    auto cost = end - start;
    auto millis = std::chrono::duration_cast<std::chrono::milliseconds>(cost);
    cout << prompt << " end, cost " << millis.count() << " ms" << endl;
}

void go() { std::this_thread::sleep_for(std::chrono::seconds(1)); }

struct Functor {
    void operator()() { std::this_thread::sleep_for(std::chrono::seconds(1)); }
};

void about_wrap() {
    wrap([]() { std::this_thread::sleep_for(std::chrono::seconds(1)); },
         "lambda");

    wrap(go, "function");

    wrap(Functor(), "functor");
}

auto curried_add(int a) {
    return [a](int b) { return a + b; };
}

void about_currying() {
    cout << "curried_add(1)(2): " << curried_add(1)(2) << endl;
    auto adder_one = curried_add(1);
    for (int i = 0; i < 3; i++) {
        cout << "adder_one(" << i << "): " << adder_one(i) << endl;
    }
}

int main() {
    cout << "begin" << endl;

    about_wrap();
    about_currying();

    cout << "end" << endl;
    return 0;
}
