#include <iostream>
using namespace std;

// some log
#define print(X) cout << #X << ":" << X++ << endl << "&" << #X << ":" << &X << endl;

static int global_static_var = 1;
int global_non_static_var = 2;

void process() {
	static int local_static_var = 3;
	int local_non_static_var = 4;
	print(global_static_var);
	print(global_non_static_var);
	print(local_static_var);
	print(local_non_static_var);
}

int main() {
	for (int i = 0; i < 3; i++) {
		process();
		cout << endl;
	}
	return 0;
}

