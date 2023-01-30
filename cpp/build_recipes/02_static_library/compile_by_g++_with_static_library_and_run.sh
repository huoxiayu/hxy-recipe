#!/bin/bash

# -c：只编译不链接（因为是要生成库）
g++ -c lib.cpp -I. -o lib.o
ar -rcs liblib.a lib.o

# -L：指定加载库文件的路径
# -l：指定加载的库文件名称（不用带lib.a）
g++ main.cpp -L. -llib && ./a.out

