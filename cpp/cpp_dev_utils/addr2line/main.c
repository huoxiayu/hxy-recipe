#include <stdio.h>

int divide(int a, int b) {
	return a / b;
}

int main() {
	int x = 2, y = 0;
	printf("ret -> %d", divide(x, y));
	return 0;
}
