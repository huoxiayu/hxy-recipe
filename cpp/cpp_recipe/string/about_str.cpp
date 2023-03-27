#include <algorithm>
#include <cassert>
#include <chrono>
#include <cstdlib>
#include <iostream>
#include <regex>
#include <string>
#include <vector>

using namespace std;

int main() {
    string s = "1 2 3   4    5";
    cout << s << endl;
    s.erase(remove(s.begin(), s.end(), ' '), s.end());
    cout << s << endl;

    string r = "aaa_bbb_aaa_bbb";
    cout << "r: " << r << endl;

    string max_shape = regex_replace(r, regex("bbb"), "aaa");
    cout << "max_shape: " << max_shape << endl;
    cout << "r: " << r << endl;

    return 0;
}