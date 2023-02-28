#include <iostream>
#include <memory>
using namespace std;

static int32_t idx = 1;

struct Resource {
  public:
    Resource(int32_t len) : id(idx++), p(new int32_t[len]) {
        cout << "Resource ctor, id: " << id << ", p: " << p << endl;
    }

    Resource(const Resource &) = delete;
    Resource &operator=(const Resource &) = delete;

    Resource(Resource &&r) : id(idx++), p(r.p) { r.p = nullptr; };

    Resource &operator=(Resource &&r) {
        cout << "Resource mv_assign_ctor, " << r.id << " -> " << id << endl;
        if (this != &r) {
            cout << "in mv_assign_ctor, release p: " << p << endl;
            delete[] this->p;
            this->p = r.p;
            r.p = nullptr;
        }

        return *this;
    }

    ~Resource() {
        cout << "Resource dtor, id: " << id << endl;
        if (this->p) {
            cout << "in dtor, release p: " << p << endl;
            delete[] p;
            this->p = nullptr;
        }
    }

  private:
    int32_t id;
    int32_t *p;
};

int main() {
    cout << "begin" << endl;

    Resource r1(1024);
    Resource r2(1024);

    r1 = std::move(r1);

    r2 = std::move(r1);

    cout << "end" << endl;
    return 0;
}