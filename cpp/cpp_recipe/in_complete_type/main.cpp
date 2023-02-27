/**
 * 不完整类型（incomplete type）是指类型缺少足够信息来确定该类型对象的大小
 * 比如：
 * struct Node {
 *     // struct Node在此处是不完整类型（即便这样，仍然可以使用它的指针）
 *     struct node* next;
 * };  // 到了此处，struct Node变成了完整类型
 * 又比如：
 * 本文件夹中的代码通过利用不完整类型的特点
 * 1、对外暴露不完整类型A_INTERFACE以及相关操作接口
 * 2、接口实现委托给A_IMPL（通过reinterpret_cast）
 * 3、封装成库后只暴露头文件，用户无法了解内部数据结构，只能严格按照接口要求进行调用
 * 4、对用户而言屏蔽了所有细节，并且库作者后续可以自由修改接口实现
 */
#include "a_interface.h"
#include <iostream>
#include <string>

void foo() {
    // ok
    A_INTERFACE *a_interface1, *a_interface2, *a_interface3;
    std::cout << a_interface1 << std::endl;
    std::cout << a_interface2 << std::endl;
    std::cout << a_interface3 << std::endl;

    // not ok
    // error: variable has incomplete type 'A_INTERFACE'
    // A_INTERFACE a_interface;
}

void bar() {
    int32_t a = 1;
    int64_t b = 2;
    bool c = true;
    std::string d = "d";
    A_INTERFACE *a_interface = new_A_INTERFACE(a, b, c, d);
    print_A_INTERFACE(a_interface);
    delete_A_INTERFACE(a_interface);
}

int main() {
    foo();
    bar();
    return 0;
}
