#include <iostream>
#include <typeinfo>
using namespace std;

#define print(X)                                                               \
    cout << "sizeof(" << #X << "): " << sizeof(X) << ", typeid(" << #X         \
         << ").name(): " << typeid(X).name() << endl;

void print_all() {}

template <class First, class... Others> void print_all(First f, Others... o) {
    print(f);
    print_all(o...);
}

int32_t add(int32_t a, int32_t b) { return a + b; }

struct Struct1 {
    bool b;
    uint64_t u;
};

struct Struct2 {
    Struct1 a;
    Struct1 *ap;
    int32_t i;
};

int main() {
    cout << "begin" << endl;

    int a = 1;
    long b = 2;
    bool c = true;
    int32_t d = 3;
    uint64_t e = 4;
    unsigned long long f = 5;
    string g = "str";
    Struct1 h;
    Struct2 i;

    print_all(a, b, c, d, e, f, g, add, h, i);
    cout << "end" << endl;
    return 0;
}
