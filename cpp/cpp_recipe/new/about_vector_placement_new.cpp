#include <iostream>
#include <vector>

using namespace std;

int idx = 0;

struct Widget {
    Widget() {
        id = idx++;
        cout << "default ctor, id: " << id << endl;
    }

    explicit Widget(int v) : id(v) { cout << "param ctor, id: " << id << endl; }

    Widget(const Widget &r) {
        id = r.id;
        cout << "cp ctor, id: " << id << endl;
    };

    Widget(Widget &&r) {
        id = r.id;
        cout << "move ctor, id: " << id << endl;
    };

    int id;
};

int main() {
    cout << "begin" << endl;

    cout << "array" << endl;
    Widget a[3];

    cout << "vector" << endl;

    vector<Widget> v;
    v.reserve(5);

    // only placement new 'param ctor'
    v.emplace_back(0);

    // 'param ctor' and 'move ctor'
    v.emplace_back(Widget(1));

    // 'param ctor' and 'move ctor'
    v.push_back(Widget(2));

    // below can't be compiled, because `Widget(int v)` is explicit
    // v.push_back(2);

    // 'paraam ctor' and 'cp ctor'
    Widget w3(3);
    v.push_back(w3);

    // 'paraam ctor' and 'move ctor'
    Widget w4(4);
    v.push_back(move(w4));

    cout << "vector will expand" << endl;
    v.emplace_back(5);

    cout << "end" << endl;

    return 0;
}