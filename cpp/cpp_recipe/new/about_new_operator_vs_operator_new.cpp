// 0. new的过程：分配内存、类型转换 & 调用构造函数、返回指针
// 1. new operator/delete operator就是new/delete操作符
// 2. operator new/delete是函数，只负责：分配/释放空间，不会调用构造/析构函数
#include <iostream>

using namespace std;

class X {
  public:
    X() { cout << "X ctor" << endl; }
    ~X() { cout << "X dtor" << endl; }

    void *operator new(size_t size, string str) {
        cout << "call operator new, size " << size << " , prompt: " << str
             << endl;
        return ::operator new(size);
    }

    void operator delete(void *ptr) {
        cout << "call operator delete" << endl;
        ::operator delete(ptr);
    }

    int num = 1;
};

int main() {
    X *x1 = new ("11111") X;
    delete x1;

    cout << endl;

    X *x2 = new ("22222") X;
    delete x2;

    return 0;
}
