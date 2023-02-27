#include "a.h"
#include "b.h"
#include "cd.h"
#include <iostream>
using namespace std;

int main() {
    cout << "begin" << endl;

    A a;
    B b;
    a.b = &b;
    b.a = &a;

    C c;
    D d;
    c.d = &d;
    d.c = &c;

    cout << "end" << endl;
    return 0;
}