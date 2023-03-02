#include <cstdlib>
#include <iostream>
using namespace std;

int main() {
    int x = 0;
    cout << "in main, x is: " << x << endl;

    // error: cannot assign to a variable captured by copy in non-mutable lambda
    // auto f0 = [=]() { x = 1; };

    auto f1 = [&]() {
        x = 1;
        cout << "in lambda f1, x is: " << x << endl;
    };

    f1();

    cout << "in main, x is: " << x << endl;

    auto f2 = [=]() mutable {
        x = rand();
        cout << "in lambda f2, x is: " << x << endl;
    };

    f2();

    cout << "in main, x is: " << x << endl;

    f2();

    cout << "in main, x is: " << x << endl;

    return 0;
}
