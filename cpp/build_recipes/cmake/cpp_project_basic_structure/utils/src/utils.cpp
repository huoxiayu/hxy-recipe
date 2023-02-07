#include "utils.h"
#include "add.h"
#include <iostream>

int utils_add(int a, int b) {
  std::cout << "call utils_add(" << a << ", " << b << ")" << std::endl;
  return add(a, b);
}