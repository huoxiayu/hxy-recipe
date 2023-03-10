#include <iostream>

using namespace std;

struct Foo {
    using type = int;
};

// #1
template <typename T> void f(typename T::type t) {
    cout << "f(typename T::type t): " << t << endl;
}

// #2
template <typename T> void f(T t) { cout << "f(T t): " << t << endl; }

int main() {
    // Call #1
    f<Foo>(10);

    // Call #2(#1 is not match but without error thanks to SFINAE)
    f<int>(10);
}
