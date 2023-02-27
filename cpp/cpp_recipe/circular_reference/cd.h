#pragma once

struct C;
struct D;

struct C {
    // 没有struct D;这一行的话
    // 下面这行会出现错误：unknown type name 'D'
    D *d;
};

struct D {
    C *c;
};