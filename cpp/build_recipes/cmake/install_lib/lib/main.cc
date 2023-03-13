#include <iostream>
#include "cmake_install_namespace/cmake_install_lib.h"

int main() {

	int x = 100, y = 200;
	int ret = lib_add(x, y);
	std::cout << "ret: " << ret << std::endl;
	
	return 0;
}
