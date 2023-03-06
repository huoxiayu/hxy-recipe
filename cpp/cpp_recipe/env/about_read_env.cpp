#include <cstdio>
#include <iostream>
using namespace std;

string lang = getenv("LANG");
const char *xx = getenv("XX");

int main() {
    cout << "begin" << endl;

    cout << "lang: " << lang << endl;
    cout << "xx == nullptr?: " << (xx == nullptr ? "true" : "false") << endl;

    for (const char *k : {"USER", "HOME"}) {
        cout << "getenv(" << k << "): " << getenv(k) << endl;
    }

    cout << "end" << endl;
    return 0;
}
