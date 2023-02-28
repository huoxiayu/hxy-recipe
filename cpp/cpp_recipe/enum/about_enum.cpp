#include <iostream>
#include <typeinfo>

using namespace std;

enum Status : uint64_t {
    OK = 1,
    NOT_OK, // 2
    OK_OR_NOT_OK = 4,

};

int main() {
    cout << "begin" << endl;

    cout << "OK: " << OK << endl;
    cout << "NOT_OK: " << NOT_OK << endl;
    cout << "OK_OR_NOT_OK: " << OK_OR_NOT_OK << endl;
    cout << "OK < 3 ? " << (OK == 1) << endl;
    cout << "typeid(OK).name(): " << typeid(OK).name() << endl;

    cout << "end" << endl;
    return 0;
}
