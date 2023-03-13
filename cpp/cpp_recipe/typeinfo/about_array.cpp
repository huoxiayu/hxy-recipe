// C++中无法直接返回数组，但可以返回指针或者std::array
#include <array>
#include <iostream>

using namespace std;

struct S {
    S() { cout << "ctor" << endl; }

    S(const S &) { cout << "cp ctor" << endl; }

    S(S &&) { cout << "move ctor" << endl; }

    ~S() { cout << "dtor" << endl; }
};

const int len = 2;

namespace case1 {

// 原生array不管怎么声明都是按照指针传递
void foo(S s_array_param[len]) {}

void go() {
    cout << "begin" << endl;

    S s_array[len];

    cout << "call foo()" << endl;
    foo(s_array);

    cout << "end" << endl;
}

} // namespace case1

namespace case2 {

// std::array通过cp ctor实现按值传递
void foo(array<S, len> s_array_param) {}

void go() {
    cout << "begin" << endl;

    array<S, len> s_array;

    cout << "call foo()" << endl;
    foo(s_array);

    cout << "end" << endl;
}

} // namespace case2

int main() {
    case1::go();
    cout << endl;
    case2::go();

    return 0;
}
