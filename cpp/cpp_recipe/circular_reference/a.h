#pragma once

// 优先使用#include "xxx.h"
// 当且仅当头文件出现循环依赖导致编译失败的时候，才考虑通过前置声明的方式使编译通过
// 也可以考虑对循环依赖的类进行重新的梳理和设计

struct B;

struct A {
    B *b;
};
