// time g++ about_template.cpp -std=c++17 -ftemplate-depth=10000 && time ./a.out
// 没有-ftemplate-depth会出现：
// fatal error: template instantiation depth exceeds maximum of 1000
// (use '-ftemplate-depth=' to increase the maximum)
//
// jump to: https://cppinsights.io/
// understand the process of template unroll
#include <iostream>

using namespace std;

// const here is redundant
template <const uint32_t N> struct fib {
    static_assert(N >= 0, "N must >= 0"); // 编译器在编译阶段使用，不会生成代码
    static constexpr uint64_t v = fib<N - 1>::v + fib<N - 2>::v;
};

template <> struct fib<1> { static constexpr uint64_t v = 1; };

template <> struct fib<0> { static constexpr uint64_t v = 1; };

int main() {
    // 编译完直接就是：movl $165580141, %esi
    cout << "fib<40U>: " << fib<40U>::v << endl;
    cout << "fib<10000U>: " << fib<10000U>::v << endl;
    return 0;
}
