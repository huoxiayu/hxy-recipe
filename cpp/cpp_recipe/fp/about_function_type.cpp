#include <iostream>
using namespace std;

typedef string (*str2str)(const string &);

void foo(str2str &func, const string &input) {
    string output = func(input);
    cout << "input: " << input << ", output: " << output << endl;
}

string function_decl(const string &input) { return input + "_function_ret"; }

int main() {
    str2str ptr_2_function = function_decl;
    foo(ptr_2_function, "k");

    str2str ptr_2_lambda = [](const string &input) -> string {
        return input + "_lambda_ret";
    };
    foo(ptr_2_lambda, "k");

    return 0;
}