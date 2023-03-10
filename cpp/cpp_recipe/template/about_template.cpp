// time g++ about_template.cpp -std=c++17 -ftemplate-depth=10000 && time ./a.out
// 没有-ftemplate-depth会出现：
// fatal error: template instantiation depth exceeds maximum of 1000
// (use '-ftemplate-depth=' to increase the maximum)
//
// jump to: https://cppinsights.io/
// understand the process of template unroll
#include <iostream>

using namespace std;

namespace case1 {

// const here is redundant
template <const uint32_t N> struct fib {
    static_assert(N >= 0, "N must >= 0"); // 编译器在编译阶段使用，不会生成代码
    static constexpr uint64_t v = fib<N - 1>::v + fib<N - 2>::v;
};

template <> struct fib<1> { static constexpr uint64_t v = 1; };

template <> struct fib<0> { static constexpr uint64_t v = 1; };

void go() {
    // 编译完直接就是：movl $165580141, %esi
    cout << "fib<40U>: " << fib<40U>::v << endl;
    // cout << "fib<10000U>: " << fib<10000U>::v << endl;
}

} // namespace case1

namespace case2 {

uint32_t idx = 1;

template <typename T, typename T::inner_type = true> struct match {
    int id = idx++;
    match() {
        cout << "ctor id: " << id << endl;
        // 没用到的模板参数可以不起名
        // 如果需要下面这行：
        // cout << "t_inner_type: " << t_inner_type << endl;
        // 则：模板声明需要用下面这种方式（也就是增加模板参数命名：t_inner_type）
        // template <typename T, typename T::inner_type t_inner_type = true>
        cout << endl;
    }
};

struct T_with_inner_type {
    using inner_type = bool;
};

struct T_with_unsuitable_inner_type {
    using inner_type = string;
};

struct T_without_inner_type {};

void go() {
    match<T_with_inner_type> m1;

    // can't compile
    // match<T_with_unsuitable_inner_type> m2;

    //  can't compile
    // match<T_without_inner_type> m3;
}

} // namespace case2

int main() {
    case1::go();
    case2::go();
    return 0;
}
