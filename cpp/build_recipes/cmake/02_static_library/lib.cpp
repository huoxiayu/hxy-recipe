#include <iostream>
#include <string>
#include "lib.h"

Foo::Foo(std::string s): str(s) {
    std::cout << "construct Foo(" << str << ")" << std::endl;
}

void Foo::foo() {
    std::cout << "foo -> " << str << std::endl;
}
