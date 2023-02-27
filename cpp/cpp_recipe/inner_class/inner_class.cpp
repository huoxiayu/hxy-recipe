#include <iostream>
using namespace std;

// 内部类是外部类的友元
class Outer {
  public:
    class Inner;

    void outer_public() const { cout << "outer_public" << endl; }

  private:
    int32_t i32 = 1;

    void outer_private() const { cout << "outer_private" << endl; }

  public:
    class Inner {
      public:
        void inner_public() const { cout << "inner_public" << endl; }

        void inner_visit_outer_private(const Outer &outer) {
            outer.outer_private();
            cout << "outer.i32 is: " << outer.i32 << endl;
        }

      private:
        int32_t i32 = 2;

        void inner_private() const { cout << "inner_private" << endl; }
    };
};

Outer outer;
Outer::Inner inner;

int main() {
    cout << "main begin" << endl;

    outer.outer_public();
    // error: 'outer_private' is a private member of 'Outer'
    // outer.outer_private();

    inner.inner_public();
    // error: 'inner_private' is a private member of 'Outer::Inner'
    // inner.inner_private();

    inner.inner_visit_outer_private(outer);

    cout << "main end" << endl;
    return 0;
}
