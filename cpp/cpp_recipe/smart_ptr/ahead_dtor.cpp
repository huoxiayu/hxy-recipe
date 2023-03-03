#include <iostream>
#include <memory>

using namespace std;

struct A {

    A(uint32_t len) : p(new int[len]) {
        cout << "A ctor, len: " << len << ", p pointer to: " << p << endl;
    }

    ~A() {
        cout << "A dtor, p pointer to: " << p << endl;
        delete[] p;
    }

    int *p;
};

template <typename T> void drop(unique_ptr<T> t) {}

void drop2(unique_ptr<A> ptr) {}

int main() {
    cout << "main begin" << endl;

    unique_ptr<A> sp1 = make_unique<A>(1024);
    cout << "sp1 pointer to: " << sp1 << endl;

    cout << "drop sp1 begin" << endl;
    drop<A>(move(sp1));
    cout << "drop sp1 end" << endl;

    cout << "sp1 pointer to: " << sp1 << endl;

    unique_ptr<A> sp2 = make_unique<A>(1024);
    cout << "sp2 pointer to: " << sp2 << endl;

    cout << "drop sp2 begin" << endl;
    drop2(move(sp2));
    cout << "drop sp2 end" << endl;

    cout << "sp2 pointer to: " << sp2 << endl;

    cout << "main end" << endl;
    return 0;
}