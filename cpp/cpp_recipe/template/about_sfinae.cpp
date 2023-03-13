#include <iostream>

using namespace std;

namespace case1 {

struct Foo {
    using type = int;
};

// #1
template <typename T> void f(typename T::type t) {
    cout << "f(typename T::type t): " << t << endl;
}

// #2
template <typename T> void f(T t) { cout << "f(T t): " << t << endl; }

void go() {
    // Call #1
    f<Foo>(10);

    // Call #2(#1 is not match but without error thanks to SFINAE)
    f<int>(10);
}

} // namespace case1

namespace case2 {

struct Bar {
    Bar() : v(1) {}

    Bar(int x) : v(x) {}

    int v;
};

void go() {
    cout << is_default_constructible<Bar>::value << endl;
    cout << is_constructible<Bar, int>::value << endl;
    cout << is_constructible<Bar, int, int>::value << endl;
    cout << is_constructible<Bar, long>::value << endl;
}

} // namespace case2

int main() {
    case1::go();
    case2::go();

    return 0;
}