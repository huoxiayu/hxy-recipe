#include <iostream>
#include <string>
using namespace std;

static int32_t idx = 1;

struct A {
	int64_t a = idx++;
	int32_t b = idx++;
	bool c = (idx++) & 1;
	double d = idx++;
	string e = std::to_string(idx++);
	
	void foo() {
		cout << "a: " << a << endl;
		cout << "b: " << b << endl;
		cout << "c: " << c << endl;
		cout << "d: " << d << endl;
		cout << "e: " << e << endl;
	}
};

int main() {
    cout << "begin" << endl;

    A stack_obj;
    cout << "&stack_obj: " << &stack_obj << endl;
    stack_obj.foo();

    char buf[sizeof(A)];
    A* placement_new_obj = new(buf) A;
    cout << "placement_new_obj point to: " << placement_new_obj << endl;
    placement_new_obj->foo();

    A* heap_obj = new A;
    cout << "heap_obj point to: " << heap_obj << endl;
    heap_obj->foo();
    delete heap_obj;

    cout << "end" << endl;
    return 0;
}

