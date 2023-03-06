// TODO:
// https://lhprojects.github.io/blog/src/StdFunctionCall.html
// https://blog.csdn.net/weixin_43530460/article/details/100053626
// https://zhuanlan.zhihu.com/p/370563773
#include <functional>
#include <iostream>

using namespace std;

struct Foo {
    Foo(int v) : add(v) { cout << "Foo ctor with add: " << add << endl; }

    Foo(const Foo &foo) : add(foo.add) {
        cout << "Foo cp-ctor with add: " << add << endl;
    }

    void print_add(int i) const {
        cout << "Foo::add: " << add << ", Foo::print_add(" << i
             << "): " << add + i << endl;
    }
    int add;
};

void print_num(int i) { cout << "Function::print_num(" << i << ")" << endl; }

struct PrintNum {
    void operator()(int i) const {
        cout << "PrintNum::operator()(" << i << ")" << endl;
    }
};

int main() {
    // free function
    function<void(int)> f_print_num = print_num;
    f_print_num(1);

    // lambda
    function<void()> f_print_num_lambda = []() { print_num(2); };
    f_print_num_lambda();

    // bind
    function<void()> f_print_num_bind_int = bind(print_num, 3);
    f_print_num_bind_int();

    // member function
    function<void(const Foo &, int)> f_print_add_member_func = &Foo::print_add;
    const Foo foo(100);
    f_print_add_member_func(foo, 4);
    f_print_add_member_func(1000, 5);

    // data member accessor
    function<int(Foo const &)> f_get_add = &Foo::add;
    cout << "Foo::add: " << f_get_add(foo) << endl;

    // bind to member function with copy semantics
    using std::placeholders::_1;
    function<void(int)> f_print_add_bind_with_foo_value =
        bind(&Foo::print_add, foo, _1);

    f_print_add_bind_with_foo_value(6);

    // bind to member function with ptr semantics
    function<void(int)> f_print_add_bing_with_foo_reference =
        bind(&Foo::print_add, &foo, _1);

    f_print_add_bing_with_foo_reference(7);

    // function object
    function<void(int)> f_print_num_function_object = PrintNum();
    f_print_num_function_object(8);

    function<int(int)> fac_function = [&](int m) {
        return (m < 2) ? 1 : m * fac_function(m - 1);
    };

    auto fac_lambda = [&](int n) { return fac_function(n); };

    for (int i{5}; i != 8; ++i) {
        cout << i << "! = " << fac_lambda(i) << ";  ";
    }
}
