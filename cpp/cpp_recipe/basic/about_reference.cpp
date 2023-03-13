#include <iostream>

using namespace std;

struct S {
    int v = 0;
};

S &foo(S &s) {
    cout << "in foo, s.v: " << s.v << endl;
    return s;
}

int main() {
    S s;
    for (int i = 1; i <= 10; i++) {
        foo(s).v = i;
    }
    return 0;
}