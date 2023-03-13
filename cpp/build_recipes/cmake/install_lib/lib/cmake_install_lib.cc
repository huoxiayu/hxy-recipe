#include "cmake_install_lib.h"
#include <iostream>

int lib_add(int a, int b) {
	int r = a + b;
	std::cout << "lib_add(" << a << ", " << b << ") is: " << r << std::endl;
	return r;
}
