#include <iostream>

using namespace std;

namespace case1 {

template <class T, int SIZE> struct A {

    T data[SIZE];

    template <int N> T get(T min_val) {
        T t = data[N];
        return max(t, min_val);
    }
};

void go() {
    A<int, 3> a;
    int ret1 = a.get<1>(1);
    cout << "ret1: " << ret1 << endl;

    int ret2 = a.template get<1>(1);
    cout << "ret2: " << ret2 << endl;
}

} // namespace case1

namespace case2 {

template <typename T> double compute(T &t, double d) {
    return t.template compute<3>(d);
}

struct d2i {
    template <int n> double convert(double d) { return n * d; }
};

void go() {
    d2i d2i;
    cout << d2i.convert<1>(3.1415926) << endl;
    cout << d2i.convert<2>(3.1415926) << endl;
    cout << d2i.convert<3>(3.1415926) << endl;
}

} // namespace case2

int main() {
    case1::go();
    case2::go();
    return 0;
}
