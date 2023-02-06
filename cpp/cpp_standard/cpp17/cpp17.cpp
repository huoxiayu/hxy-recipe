// g++ -std=c++17 cpp17.cpp && ./a.out
#include <tuple>
#include <iostream>
using namespace std;

int main() {
	tuple t2ii{1.2, 2.3};
	cout << get<0>(t2ii) << ", " << get<1>(t2ii) << endl;
	return 0;
}