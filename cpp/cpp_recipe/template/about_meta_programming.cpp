#include <iostream>
#include <type_traits>

// __VA_ARGS__
// 可变宏参数
#define CALL(...)                                                              \
    std::cout << #__VA_ARGS__ << ": " << (__VA_ARGS__) << std::endl;

// 模板参数是input
// using type = T;
// 定义的type是输出
template <typename T> struct type_identity { using type = T; };

// 模板参数是input
// constexpr static T value = t;
// 定义的value是输出
template <typename T, T t> struct value_identity : type_identity<T> {
    constexpr static T value = t;
};

// 特化true_type & false_type
using true_type = value_identity<bool, true>;
using false_type = value_identity<bool, false>;

// 默认T1和T2不相同，因此继承false_type
template <typename T1, typename T2> struct is_same : false_type {};

// 特化T和T继承自true_type
template <typename T> struct is_same<T, T> : true_type {};

// 运用类似的技巧
// 这样写法比较繁琐，因此是：bad_is_void
template <typename T> struct bad_is_void : false_type {};
template <> struct bad_is_void<void> : true_type {};
template <> struct bad_is_void<const void> : true_type {};
template <> struct bad_is_void<volatile void> : true_type {};
template <> struct bad_is_void<const volatile void> : true_type {};

// 定义remove_const/remove_const_t
template <typename T> struct remove_const : type_identity<T> {};
template <typename T> struct remove_const<const T> : type_identity<T> {};
template <typename T> using remove_const_t = typename remove_const<T>::type;

// 定义remove_volatile/remove_volatile_t
template <typename T> struct remove_volatile : type_identity<T> {};
template <typename T> struct remove_volatile<volatile T> : type_identity<T> {};
template <typename T>
using remove_volatile_t = typename remove_volatile<T>::type;

// remove_cv_t(combine remove_const_t and remove_volatile_t)
template <typename T> using remove_cv_t = remove_volatile_t<remove_const_t<T>>;

template <typename T> struct is_void : is_same<void, remove_cv_t<T>> {};

template <typename T> struct is_float : is_same<float, remove_cv_t<T>> {};
template <typename T> constexpr bool is_float_v = is_float<T>::value;

template <typename T> struct is_double : is_same<double, remove_cv_t<T>> {};
template <typename T> constexpr bool is_double_v = is_double<T>::value;

template <typename T>
constexpr bool is_float_or_double_v = is_float_v<T> || is_double_v<T>;

template <typename T1, typename T2>
struct is_same_remove_cv : is_same<remove_cv_t<T1>, remove_cv_t<T2>> {};

void example0() {
    CALL(is_same<void, void>::value);
    CALL(is_same<void, const void>::value);
    CALL(is_same<void, volatile void>::value);
    CALL(is_same<void, const volatile void>::value);

    CALL(is_same_remove_cv<void, void>::value);
    CALL(is_same_remove_cv<void, const void>::value);
    CALL(is_same_remove_cv<void, volatile void>::value);
    CALL(is_same_remove_cv<void, const volatile void>::value);

    CALL(is_float_or_double_v<float>);
    CALL(is_float_or_double_v<double>);
    CALL(is_float_or_double_v<int>);
    CALL(is_float_or_double_v<void>);
    CALL(is_float_or_double_v<char *>);
}

template <typename T, typename... US> struct is_any_of : false_type {};

template <typename T, typename... US>
struct is_any_of<T, T, US...> : true_type {};

template <typename T, typename U, typename... US>
struct is_any_of<T, U, US...> : is_any_of<T, US...> {};

void example1() {
    CALL(is_any_of<void>::value);
    CALL(is_any_of<void, void>::value);
    CALL(is_any_of<void, void, int>::value);
    CALL(is_any_of<void, int, void>::value);
    CALL(is_any_of<void, int, long, char *, void>::value);
}

template <bool cond, typename T1, typename T2> struct condition {
    using type = T1;
};

template <typename T1, typename T2> struct condition<false, T1, T2> {
    using type = T2;
};

void example2() {
    typedef condition<true, int, double>::type Type1;
    typedef condition<false, int, double>::type Type2;
    typedef condition<sizeof(int) >= sizeof(double), int, double>::type Type3;

    std::cout << typeid(Type1).name() << '\n';
    std::cout << typeid(Type2).name() << '\n';
    std::cout << typeid(Type3).name() << '\n';
}

template <bool cond, typename TRUE_TYPE, typename FALSE_TYPE> struct _if {};

template <typename TRUE_TYPE, typename FALSE_TYPE>
struct _if<true, TRUE_TYPE, FALSE_TYPE> : type_identity<TRUE_TYPE> {};

template <typename TRUE_TYPE, typename FALSE_TYPE>
struct _if<false, TRUE_TYPE, FALSE_TYPE> : type_identity<FALSE_TYPE> {};

void example3() {}

void example10() {
    using uptr_int = std::unique_ptr<int>;

    constexpr bool copyable = std::is_copy_constructible<uptr_int>::value;
    constexpr bool movable = std::is_move_constructible<uptr_int>::value;

    static_assert(!copyable);
    static_assert(movable);
}

int main() {
    std::cout << "begin" << std::endl;

    example2();

    example10();

    std::cout << "end" << std::endl;
    return 0;
}
