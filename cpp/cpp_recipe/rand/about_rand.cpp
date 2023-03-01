#include <ctime>
#include <iostream>
using namespace std;

int main() {
    cout << "begin" << endl;

    auto unixTimestampInSeconds = time(nullptr);
    cout << "unixTimestampInSeconds: " << unixTimestampInSeconds << endl;

    srand(unixTimestampInSeconds);

    for (int i = 0; i < 10; i++) {
        cout << "rand(): " << rand() << endl;
    }

    cout << "end" << endl;
    return 0;
}
