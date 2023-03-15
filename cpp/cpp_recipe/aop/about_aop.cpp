#include <functional>
#include <iostream>
#include <type_traits>

using namespace std;

// NonCopyable Base Class
class NonCopyable {
  public:
    NonCopyable(const NonCopyable &) = delete;
    NonCopyable &operator=(const NonCopyable &) = delete;

  protected:
    NonCopyable() = default;
    ~NonCopyable() = default;
};

#define HAS_MEMBER(member)                                                     \
    template <typename T, typename... Args> struct has_member_##member {       \
      private:                                                                 \
        template <typename U>                                                  \
        static auto Check(int)                                                 \
            -> decltype(declval<U>().member(declval<Args>()...), true_type()); \
                                                                               \
        template <typename U> static false_type Check(...);                    \
                                                                               \
      public:                                                                  \
        enum { value = is_same<decltype(Check<T>(0)), true_type>::value };     \
    };

HAS_MEMBER(Foo)
HAS_MEMBER(Before)
HAS_MEMBER(After)

template <typename Func, typename... Args> struct Aspect : NonCopyable {
    Aspect(Func &&f) : m_func(forward<Func>(f)) {}

    template <typename T>
    typename enable_if<has_member_Before<T, Args...>::value &&
                       has_member_After<T, Args...>::value>::type
    Invoke(Args &&...args, T &&aspect) {
        aspect.Before(forward<Args>(args)...);
        m_func(forward<Args>(args)...);
        aspect.After(forward<Args>(args)...);
    }

    template <typename T>
    typename enable_if<has_member_Before<T, Args...>::value &&
                       !has_member_After<T, Args...>::value>::type
    Invoke(Args &&...args, T &&aspect) {
        aspect.Before(forward<Args>(args)...);
        m_func(forward<Args>(args)...);
    }

    template <typename T>
    typename enable_if<!has_member_Before<T, Args...>::value &&
                       has_member_After<T, Args...>::value>::type
    Invoke(Args &&...args, T &&aspect) {
        m_func(forward<Args>(args)...);
        aspect.After(forward<Args>(args)...);
    }

    template <typename Head, typename... Tail>
    void Invoke(Args... args, Head &&headAspect, Tail &&...tailAspect) {
        headAspect.Before(forward<Args>(args)...);
        Invoke(forward<Args>(args)..., forward<Tail>(tailAspect)...);
        headAspect.After(forward<Args>(args)...);
    }

  private:
    Func m_func;
};

template <typename... AP, typename... Args, typename Func>
void Invoke(Func &&f, Args &&...args) {
    Aspect<Func, Args...> asp(forward<Func>(f));
    asp.Invoke(forward<Args>(args)..., AP()...);
}

struct AA {
    void Before(int i) { cout << "Before AA" << i << endl; }
    void After(int i) { cout << "After AA" << i << endl; }
};

struct BB {
    void Before(int i) { cout << "Before BB" << i << endl; }
    void After(int i) { cout << "After BB" << i << endl; }
};

struct CC {
    void Before(int i) { cout << "Before CC" << i << endl; }
    void After(int i) { cout << "After CC" << i << endl; }
};

void HT(int a) { cout << "real HT function" << a << endl; }

void aop() {
    function<void(int)> f = bind(&HT, placeholders::_1);
    Invoke<AA, BB, CC>(f, 1);
}

int main() {
    aop();
    return 0;
}
