#include <iostream>
#include <memory>

using namespace std;

struct S {
    S() { cout << "S ctor" << endl; }

    S(const S &s) {
        this->v = s.v;
        cout << "S cptor" << endl;
    }

    ~S() { cout << "S dtor" << endl; }

    int32_t v = 999;

    friend ostream &operator<<(ostream &os, const S &s) {
        os << s.v;
        return os;
    }
};

int main() {
    cout << "make_shared<int32_t> begin" << endl;
    shared_ptr<int32_t> int_ptr = make_shared<int32_t>(111);
    cout << "*int_ptr: " << *int_ptr << endl;
    cout << "make_shared<int32_t> end" << endl;

    cout << "make_shared<S> begin" << endl;
    S s;
    shared_ptr<S> s_ptr = make_shared<S>(s);
    cout << "*s_ptr is: " << *s_ptr << endl;
    cout << "s_ptr->v is: " << s_ptr->v << endl;
    cout << "make_shared<S> end" << endl;

    return 0;
}
