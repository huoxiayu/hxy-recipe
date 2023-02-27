#include "a_interface.h"
#include <iostream>

struct A_IMPL {
    A_IMPL(int32_t a, int64_t b, bool c, std::string d)
        : a(a), b(b), c(c), d(d) {
        std::cout << "A_IMPL ctor" << std::endl;
    }

    ~A_IMPL() { std::cout << "A_IMPL dtor" << std::endl; }

    int32_t a;
    int64_t b;
    bool c;
    std::string d;
};

A_INTERFACE *new_A_INTERFACE(int32_t a, int64_t b, bool c, std::string d) {
    A_IMPL *a_impl = new A_IMPL(a, b, c, d);
    return reinterpret_cast<A_INTERFACE *>(a_impl);
}

void print_A_INTERFACE(A_INTERFACE *a_interface) {
    A_IMPL *a_impl = reinterpret_cast<A_IMPL *>(a_interface);
    std::cout << "a: " << a_impl->a << ", b: " << a_impl->b
              << ", c: " << a_impl->c << ", d: " << a_impl->d << std::endl;
}

void delete_A_INTERFACE(A_INTERFACE *a_interface) {
    A_IMPL *a_impl = reinterpret_cast<A_IMPL *>(a_interface);
    delete a_impl;
}
