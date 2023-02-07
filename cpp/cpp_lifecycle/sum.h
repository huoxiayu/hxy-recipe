#pragma once
#include <iostream>
using namespace std;

struct Sum {
public:
	Sum() {
		cout << "Construct Sum" << endl;
	}

	int sum(int a, int b) {
		cout << "call sum" << endl;
		return a + b;
	}

	template<class T>
	T sum(T a, T b) {
		cout << "call template sum" << endl;
		return a + b;
	}
};

// "Construct Sum"输出在main函数执行之前
Sum sum;

