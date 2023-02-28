#include <iostream>
#include <memory>
using namespace std;

int32_t idx = -1;

struct A {
    A() : a(idx--), b(idx--) {
        cout << "A default ctor, a: " << a << ", b: " << b << endl;
    }

    A(int32_t a, int32_t b) : a(a), b(b) {
        cout << "A ctor, a: " << a << ", b: " << b << endl;
    }

    ~A() { cout << "A dtor, a: " << a << ", b: " << b << endl; }

    int32_t a;
    int32_t b;
};

template <typename T> void del_func(T *obj) { delete obj; }

template <typename T> void del_array_func(T *obj) { delete[] obj; }

using A_P = A *;
struct RawPointerDeleter {
    template <typename T> void operator()(T *obj) { del_func(obj); }
};

struct RawPointerToArrayDeleter {
    template <typename T> void operator()(T *obj) { del_array_func(obj); }
};

void about_unique_ptr0() {
    unique_ptr<A> p = make_unique<A>();
    cout << "p->a: " << p->a << endl;
    cout << "(*p).b: " << (*p).b << endl;
}

void about_unique_ptr1() {
    unique_ptr<A> p = make_unique<A>(1, 2);
    cout << "p->a: " << p->a << endl;
    cout << "(*p).b: " << (*p).b << endl;
}

void about_unique_ptr2() {
    A *raw_pointer = new A(3, 4);
    cout << "raw_pointer->a: " << raw_pointer->a << endl;
    cout << "(*raw_pointer).b: " << (*raw_pointer).b << endl;
    delete raw_pointer;
}

void about_unique_ptr3() {
    using uni_ptr_with_deleter_type = unique_ptr<A, RawPointerDeleter>;
    auto uni_ptr_with_deleter = uni_ptr_with_deleter_type(new A(5, 6));
    cout << "uni_ptr_with_deleter->a: " << uni_ptr_with_deleter->a << endl;
    cout << "(*uni_ptr_with_deleter).b: " << (*uni_ptr_with_deleter).b << endl;
}

void about_unique_ptr4() {
    auto uni_ptr_without_deleter = unique_ptr<A>(new A(7, 8));
    cout << "uni_ptr_without_deleter->a: " << uni_ptr_without_deleter->a
         << endl;
    cout << "(*uni_ptr_without_deleter).b: " << (*uni_ptr_without_deleter).b
         << endl;
}

void about_unique_ptr5() {
    uint32_t len = 3;
    A *p = new A[len];

    // method 1
    auto del = [](A *i) { delete[] i; };
    auto uni_ptr_with_deleter = unique_ptr<A, decltype(del)>(p, del);
    // method 2
    auto uni_ptr_with_deleter1 = unique_ptr<A, RawPointerToArrayDeleter>(p);
    using fn_type = decltype(del_array_func<A>) *;
    // method 3
    auto uni_ptr_with_deleter2 = unique_ptr<A, fn_type>(p, del_array_func<A>);

    for (uint32_t i = 0; i < len; i++) {
        cout << "uni_ptr_with_deleter->a: " << p->a << endl;
        cout << "(*uni_ptr_with_deleter).b: " << (*p).b << endl;
        p++;
    }
}

void about_unique_ptr6() {
    uint32_t len = 3;
    A *p = new A[len];
    auto uni_ptr_with_deleter = unique_ptr<A>(p);
    for (uint32_t i = 0; i < len; i++) {
        cout << "uni_ptr_with_deleter->a: " << p->a << endl;
        cout << "(*uni_ptr_with_deleter).b: " << (*p).b << endl;
        p++;
    }
}

int main() {
    cout << "begin" << endl;

    cout << "about_unique_ptr0" << endl;
    about_unique_ptr0();
    cout << "about_unique_ptr1" << endl;
    about_unique_ptr1();
    cout << "about_unique_ptr2" << endl;
    about_unique_ptr2();
    cout << "about_unique_ptr3" << endl;
    about_unique_ptr3();
    cout << "about_unique_ptr4" << endl;
    about_unique_ptr4();
    cout << "about_unique_ptr5" << endl;
    about_unique_ptr5();
    cout << "about_unique_ptr6" << endl;
    about_unique_ptr6();

    cout << "end" << endl;
    return 0;
}