#include <iostream>
#include <memory>

using namespace std;

static int32_t idx = 1;

struct S {
    S() { cout << "S ctor, id: " << id << endl; }

    S(const S &s) {
        this->v = s.v;
        cout << "S cptor, id: " << id << endl;
    }

    ~S() { cout << "S dtor, id: " << id << endl; }

    int32_t id = idx++;
    int32_t v = 999;

    friend ostream &operator<<(ostream &os, const S &s) {
        os << "id: " << s.id << ", v: " << s.v;
        return os;
    }
};

int main() {
    cout << "make_shared<S>()->v: " << make_shared<S>()->v << endl;

    cout << "make_shared<int32_t> begin" << endl;
    shared_ptr<int32_t> int_ptr = make_shared<int32_t>(111);
    cout << "*int_ptr: " << *int_ptr << endl;
    cout << "make_shared<int32_t> end" << endl;

    cout << "make_shared<S> begin" << endl;
    S s;
    // 栈地址
    cout << "&s: " << &s << endl;

    shared_ptr<S> s_ptr = make_shared<S>(s);
    // 堆地址
    cout << "s_ptr is: " << s_ptr << endl;

    cout << "*s_ptr is: " << *s_ptr << endl;
    cout << "s_ptr->v is: " << s_ptr->v << endl;
    cout << "make_shared<S> end" << endl;

    int *new_int = new int(3);
    cout << "new_int addr: " << new_int << endl;
    delete new_int;

    return 0;
}
