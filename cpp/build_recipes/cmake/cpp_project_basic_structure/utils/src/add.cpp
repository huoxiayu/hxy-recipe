#include "add.h"
#include <iostream>

int add(int a, int b) {
  std::cout << "call add(" << a << ", " << b << ")" << std::endl;
  return a + b;
}