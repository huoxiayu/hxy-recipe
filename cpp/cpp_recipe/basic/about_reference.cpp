#include <iostream>
#include <type_traits>

using namespace std;

struct S {
    int v = 0;
};

S &foo(S &s) {
    cout << "in foo, s.v: " << s.v << endl;
    return s;
}

struct C {
    C(S s) {}
};

int main() {
    S s;
    for (int i = 1; i <= 10; i++) {
        foo(s).v = i;
    }

    S &s_ref = s;
    S *s_ptr = &s;
    cout << typeid(s).name() << endl;
    cout << typeid(s_ref).name() << endl;
    cout << typeid(s_ptr).name() << endl;

    C c1(s);
    C c2(s_ref);
    C c3(*s_ptr);

    return 0;
}