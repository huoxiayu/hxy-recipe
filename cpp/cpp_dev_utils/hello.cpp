#include <initializer_list>
#include <iostream>
#include <tuple>
#include <vector>
using namespace std;

#define N 500

#define plus(a, b) (a + b)

class A {
public:
  virtual void foo() = 0;
  virtual ~A() = default;
};

class B : public A {
public:
  void foo() { cout << "b.foo" << endl; }
};

class C : public A {
public:
  void foo() { cout << "c.foo" << endl; }
};

int main() {
  cout << "hello clang-format" << endl;
  cout << plus(1, 2) << endl;
  vector<A *> v{new B, new C};
  for (auto a : v) {
    a->foo();
  }
  tuple t2dd{1.2, 2.3};
  cout << "(" << get<0>(t2dd) << ", " << get<1>(t2dd) << ")" << endl;
  return 0;
}