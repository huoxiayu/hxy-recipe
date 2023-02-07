#include "common.h"
#include "math.h"
#include "utils.h"

double common_add(double a, double b) {
  std::cout << "call common_add(" << a << ", " << b << ")" << std::endl;
  return template_add<double>(a, b);
}

int utils_add_twice(int a, int b) {
  std::cout << "call utils_add_twice(" << a << ", " << b << ")" << std::endl;
  return utils_add(a, b) * 2;
}