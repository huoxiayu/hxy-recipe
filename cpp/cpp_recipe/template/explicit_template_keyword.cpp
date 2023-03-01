#include <iostream>

using namespace std;

template <class T, int SIZE> struct A {

    T data[SIZE];

    template <int N> T get(T min_val) {
        T t = data[N];
        return max(t, min_val);
    }
};

int main() {
    A<int, 3> a;
    int ret1 = a.get<1>(1);
    cout << "ret1: " << ret1 << endl;

    int ret2 = a.template get<1>(1);
    cout << "ret2: " << ret2 << endl;

    return 0;
}