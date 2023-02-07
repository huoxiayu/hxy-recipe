#include <iostream>
using namespace std;

extern int sum(int a, int b);

int main() {
  int x = 1, y = 2;
  cout << "sum(x, y) is: " << sum(x, y) << endl;
  return 0;
}
