#include <iostream>
#include <type_traits>
#include <typeinfo>

using namespace std;

namespace case1 {
#define PRINT(X)                                                               \
    cout << #X << ", type: " << typeid((X)).name() << ", value: " << (X)       \
         << endl;

void go() {
    cout << "typeid(int).name(): " << typeid(int).name() << endl;
    cout << "typeid(uint64_t).name(): " << typeid(uint64_t).name() << endl;

    enable_if_t<true, int> v1 = 1;
    PRINT(v1);

    enable_if_t<true, uint64_t> v2 = 2;
    PRINT(v2);
}

} // namespace case1

namespace case2 {

template <bool B, typename T = void> struct custom_enable_if {};

template <typename T> struct custom_enable_if<true, T> { using type = T; };

enum Type { int_t, float_t };

const string type_name[]{"int_t", "float_t"};

struct T {
    Type type;

    // #1
    // enable_if is tedious
    // must write: 'typename xxx::type'
    template <typename Integer, typename enable_if<is_integral<Integer>::value,
                                                   bool>::type = true>
    T(Integer) : type(int_t) {
        cout << "Integer Template" << endl;
    }

    // #2
    // enable_if_t is more simple and more clear
    template <typename Floating,
              enable_if_t<is_floating_point<Floating>::value, bool> = true>
    T(Floating) : type(float_t) {
        cout << "Floating Template" << endl;
    }
};

void go() {
    T t1(1);
    cout << type_name[t1.type] << endl;

    T t2(2.0);
    cout << type_name[t2.type] << endl;
}

} // namespace case2

int main() {
    case1::go();
    case2::go();
    return 0;
}
