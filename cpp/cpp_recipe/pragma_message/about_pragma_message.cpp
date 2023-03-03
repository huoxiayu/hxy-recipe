#include <iostream>

using namespace std;

#ifdef A
#pragma message("A defined")
#elif defined(B)
#pragma message("B defined")
#else
#pragma message("Both of A and B are not defined")
#endif

int main() {
	cout << "hi" << endl;
	return 0;
}
