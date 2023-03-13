#include <iostream>
#include <utility>

using namespace std;

int add(int a, int b) {
    int c = a + b;
    cout << "add ret -> " << c << endl;
    return c;
}

template <typename Fn, typename... Args>
decltype(declval<Fn>()(declval<Args>()...))
add_decltype1(Fn f, Args... args) {
    auto res = f(args...);
    cout << "add_decltype1 ret -> " << res << endl;
    return res;
}

template <typename Fn, typename... Args>
auto add_decltype2(Fn f, Args... args) -> decltype(f(args...)) {
    auto res = f(args...);
    cout << "add_decltype2 ret -> " << res << endl;
    return res;
}

template<typename Fn, typename... Args>
decltype(Fn(Args...)) add_decltype3(Fn f, Args... args) {
    auto res = f(args...);
    cout << "add_decltype3 ret -> " << res << endl;
    return res;
}

int main() {
    auto res0 = add_decltype1(add, 1, 1);
    cout << "Main Res0: " << res0 << endl;

    auto res1 = add_decltype2(add, 1, 1);
    cout << "Main Res1: " << res1 << endl;

    return 0;
}