#include <cstdlib>
#include <cxxabi.h>
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

template <typename T> string get_type_name() {
    string tname = typeid(T).name();
    int status;
    char *demangled_name =
        abi::__cxa_demangle(tname.c_str(), NULL, NULL, &status);
    if (status == 0) {
        tname = demangled_name;
        std::free(demangled_name);
    }
    return tname;
}

int main() {
    cout << "begin" << endl;

    cout << "get_type_name<int>(): " << get_type_name<int>() << endl;
    cout << "get_type_name<int32_t>(): " << get_type_name<int32_t>() << endl;
    cout << "get_type_name<int64_t>(): " << get_type_name<int64_t>() << endl;
    cout << "get_type_name<long>(): " << get_type_name<long>() << endl;
    cout << "get_type_name<bool>(): " << get_type_name<bool>() << endl;
    cout << "get_type_name<string>(): " << get_type_name<string>() << endl;
    cout << "get_type_name<char *>(): " << get_type_name<char *>() << endl;
    cout << "get_type_name<Struct1>(): " << get_type_name<Struct1>() << endl;
    cout << "get_type_name<Struct2>(): " << get_type_name<Struct2>() << endl;
    cout << "get_type_name<decltype(add)>(): " << get_type_name<decltype(add)>()
         << endl;

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
