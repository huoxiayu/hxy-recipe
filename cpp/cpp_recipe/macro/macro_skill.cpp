/*
 * C++具有强大的模板元编程的能力
 * 因此宏在C++中不如在C中使用那么广泛
 * 但C++中宏的学习和使用仍然是必要的
 * 比如模板元编程无法做到：
 * 通过模板展开生成新的标识符，例如：生成新的函数名/类名/namespace名等
 *
 * TODO:
 * https://bot-man-jl.github.io/articles/?post=2020/Macro-Programming-Art
 */
#include <iostream>

using namespace std;

template <typename... Args>
std::string format(const char *format, Args... args) {
    size_t length = std::snprintf(nullptr, 0, format, args...);
    if (length <= 0) {
        return "";
    }

    char *buf = new char[length + 1];
    std::snprintf(buf, length + 1, format, args...);

    return std::string(buf);
}

void built_in_macro() {
    // cpp standard version
    cout << "__cplusplus: " << __cplusplus << endl;
    // log
    cout << format("xxx error at file: %s, line: %d", __FILE__, __LINE__)
         << endl;
    // time(compiled time)
    cout << "current time is: " __DATE__ << " " << __TIME__ << endl;
}

// #
#define STR(X) #X

void str() {
    int a = 1;
    cout << STR(a) << endl;
    cout << a << endl;
}

#define CONCAT(X, Y) X##Y
void concat() {
    string ab = "ab";
    cout << CONCAT(a, b) << endl;
}

// ##
#define PRINT_A(IDX) cout << "a" << STR(IDX) << ": " << a##IDX << endl;
void print_a() {
    int a1 = 1, a2 = 2, a3 = 3;
    PRINT_A(1);
    PRINT_A(2);
    PRINT_A(3);
}

int main() {
    cout << "begin" << endl;

    built_in_macro();
    str();
    concat();
    print_a();

    cout << "end" << endl;
    return 0;
}
