// 1. static_cast
// static_cast不执行运行时类型检查，有编译期类型检查，比如下面p3的转换编译期就会报错
// 编译器会帮助完成截断、补齐、指针偏移等操作
// static_cast可能不安全，比如通过static_cast执行不合理的downcast操作
// 2. dynamic_cast
// dynamic_cast执行运行时类型检查
// dynamic_cast更加安全，同时存在运行时检查开销，但只适用于指针和引用
// dynamic_cast转换失败会返回空指针
// 3. const_cast
// const_cast用来修改类型的const/volatile属性，除了const/volatile以外转换前后类型需相同
// 4. reintepreter_cast
// reinterpreter_cast执行bitwise copy，即强制的（不管3721）的重新解释转换
//
// reinterpreter和static_cast的区别：
//     static_cast完成相关类型之间的转换
//     reinterpret_cast处理互不相关的类型之间的转换：比如整型到指针/一种指针到另一种完全不相关的指针

#include <iostream>

using namespace std;

// no virtual table
struct Base {
    void base() { cout << "base" << endl; }
    int m;
};

// has virtual table
struct Derived : public Base {
    Derived() = default;
    virtual ~Derived() = default;
    virtual void derived() { cout << "derived" << endl; }
};

void about_const_cast() {
    const string &const_str = string("const_string_payload");
    cout << "const_str: " << const_str << endl;

    string &non_const_str = const_cast<string &>(const_str);
    cout << "non_const_str: " << non_const_str << endl;

    cout << "after update" << endl;
    non_const_str = "non_const_str_payload";

    cout << "const_str: " << const_str << endl;
    cout << "non_const_str: " << non_const_str << endl;
}

void about_static_cast() {
    float f = 1.11F;
    cout << "f: " << f << endl;

    // enable g++ -Wconversion

    // warning: implicit conversion turns floating-point number into integer:
    // 'double' to 'int32_t' (aka 'int')
    int32_t i = f;
    cout << "i: " << i << endl;

    // no warning
    int32_t j = static_cast<int32_t>(f);
    cout << "j: " << j << endl;

    uint64_t u = 0XFFFFFFFFFFFFFFFF;
    void *vp = &u;
    cout << "*static_cast<uint32_t *>(vp) -> " << *static_cast<uint32_t *>(vp)
         << endl;
    cout << "*static_cast<uint64_t *>(vp) -> " << *static_cast<uint64_t *>(vp)
         << endl;

    int *p = new int(1);
    cout << "*p: " << *p << endl;

    void *p1 = static_cast<void *>(p);
    cout << "*p1: " << *(int *)p1 << endl;

    char *p2 = static_cast<char *>(p1);
    cout << "*p2: " << *p2 << endl;

    // 类型转换无效
    // error: static_cast from 'int *' to 'char *' is not allowed
    // char *p3 = static_cast<char *>(p);

    delete p;

    Derived *d = new Derived();
    cout << "d pointer to -> " << d << endl; // 0x7ff086705e70(vtable)
    cout << "&d->m -> " << &d->m << endl;    // 0x7ff086705e78

    d->derived();

    Base *b = static_cast<Base *>(d);
    cout << "b pointer to -> " << b << endl; // 0x7ff086705e78
    cout << "&b->m -> " << &b->m << endl;    // 0x7ff086705e78
    b->base();

    delete d;
}

void about_reinterpret_cast() {
    Derived *d = new Derived();
    cout << "d pointer to -> " << d << endl; // 0x7ff086705e70(vtable)
    cout << "&d->m -> " << &d->m << endl;    // 0x7ff086705e78

    d->derived();

    // warning: 'reinterpret_cast' from class 'Derived *' to its base at
    // non-zero offset 'Base *' behaves differently from 'static_cast'
    // note: use 'static_cast' to adjust the pointer correctly while upcasting
    Base *b = reinterpret_cast<Base *>(d);
    cout << "b pointer to -> " << b << endl; // 0x7fe34c705e70
    cout << "&b->m -> " << &b->m << endl;    // 0x7fe34c705e70
    b->base();

    delete d;
}

void about_dynamic_cast() {}

int main() {
    cout << "begin" << endl;

    cout << "about_const_cast begin" << endl;
    about_const_cast();

    cout << "about_static_cast begin" << endl;
    about_static_cast();

    cout << "about_reinterpret_cast begin" << endl;
    about_reinterpret_cast();

    cout << "about_dynamic_cast begin" << endl;
    about_dynamic_cast();

    cout << "end" << endl;
    return 0;
}
