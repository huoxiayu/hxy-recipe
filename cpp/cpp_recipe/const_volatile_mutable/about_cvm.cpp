#include <chrono>
#include <iostream>
#include <string>
#include <thread>
using namespace std;

class HashTable {
  public:
    // 逻辑上，HashTable的lookup操作是不会修改HashTable本身的，因此这里是const方法
    string lookup(const string &key) const {
        // 实际上，HashTable内部可以做缓存
        if (key == this->last_key_) {
            return this->last_value_;
        }

        // 这里可以缓存查询结果，因此需要修改内部状态：last_key_/last_value_
        this->last_key_ = key;
        this->last_value_ = this->lookupInternal(key);
        return this->last_value_;
    }

    string lookupInternal(const string &key) const {
        std::this_thread::sleep_for(std::chrono::seconds(1));
        return key + "_v";
    }
    // 这里需要声明为mutable，否则会产生编译错误
    mutable string last_key_;
    mutable string last_value_;
    int int_id = 1;
    mutable int mutable_int_id = 1;
};

// const：常量
// const mutable：逻辑上是常量，但内部有非常量的状态
void about_const_and_mutable() {
    HashTable ht;
    const HashTable cht = ht;

    cout << "ht.int_id: " << ht.int_id << endl;
    cout << "ht.mutable_int_id: " << ht.mutable_int_id << endl;
    ht.int_id = 2;
    ht.mutable_int_id = 2;
    cout << "ht.int_id: " << ht.int_id << endl;
    cout << "ht.mutable_int_id: " << ht.mutable_int_id << endl;

    cout << "cht.int_id: " << cht.int_id << endl;
    cout << "cht.mutable_int_id: " << cht.mutable_int_id << endl;

    // error: read-only variable is not assignable
    // (&cht)->int_id = 2;

    // error: cannot assign to variable 'cht' with const-qualified type
    // cht.int_id = 2;

    (&cht)->mutable_int_id = 2;
    cout << "cht.int_id: " << cht.int_id << endl;
    cout << "cht.mutable_int_id: " << cht.mutable_int_id << endl;

    string lookup_key("k");
    for (int i = 1; i <= 5; i++) {
        auto start = std::chrono::high_resolution_clock::now();
        string lookup_value = ht.lookup(lookup_key);
        auto end = std::chrono::high_resolution_clock::now();
        cout << "lookup_value: " << lookup_value << ", cost "
             << (end - start).count() << " ns" << endl;
    }
}

void about_volatile() {}

int main() {
    cout << "begin" << endl;

    about_const_and_mutable();

    cout << "end" << endl;
    return 0;
}
