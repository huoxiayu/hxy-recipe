#include <iostream>
using namespace std;

struct Foo {
    Foo() { cout << "Foo ctor" << endl; }

    ~Foo() { cout << "Foo dtor" << endl; }

    void foo() { cout << "call foo()" << endl; }
};

struct Bar {
    Bar() { cout << "Bar ctor" << endl; }

    ~Bar() { cout << "Bar dtor" << endl; }

    void bar() { cout << "call bar()" << endl; }
};

Foo foo;

Bar bar;

int main() {
    cout << "main begin" << endl;

    foo.foo();
    bar.bar();

    cout << "&foo: " << &foo << endl;
    cout << "&bar: " << &bar << endl;

    cout << "main end" << endl;
    return 0;
}