#include "common.h"
#include "utils.h"
#include <iostream>

int main() {
  std::cout << utils_add(1, 2) << std::endl;
  std::cout << common_add(1, 2) << std::endl;
  std::cout << utils_add_twice(1, 2) << std::endl;
  return 0;
}