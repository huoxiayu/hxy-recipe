#pragma once
#include <iostream>

template <class T> T template_add(T a, T b) {
  std::cout << "call template_add(" << a << ", " << b << ")" << std::endl;
  return a + b;
}