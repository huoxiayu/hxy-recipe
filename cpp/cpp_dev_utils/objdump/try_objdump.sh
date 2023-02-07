#!/bin/bash

# Linux动态链接库规则：libxxx.so
# xxx即为库名字
g++ -g -shared -fPIC sum.cpp -o libsum.so

# 链接其他动态库时，-l后只需跟xxx即可，不需要写成libxxx.so
g++ -g -L. -lsum main.cpp -o main

./main 

# see: libsum.so.objdump.txt
objdump -d -S libsum.so 

# see: main.objdump.txt
objdump -d -S main 

objdump -x libsum.so | grep "NEEDED"
#  NEEDED               libstdc++.so.6
#  NEEDED               libm.so.6
#  NEEDED               libgcc_s.so.1
#  NEEDED               libc.so.6

objdump -T -demangle libsum.so
# DYNAMIC SYMBOL TABLE:
# 0000000000000000  w   D  *UND*	0000000000000000              __gmon_start__
# 0000000000000000  w   D  *UND*	0000000000000000              _Jv_RegisterClasses
# 0000000000000000  w   D  *UND*	0000000000000000              _ITM_deregisterTMCloneTable
# 0000000000000000  w   D  *UND*	0000000000000000              _ITM_registerTMCloneTable
# 0000000000000000  w   DF *UND*	0000000000000000  GLIBC_2.2.5 __cxa_finalize
# 0000000000201030 g    D  .bss	0000000000000000  Base        _end
# 0000000000201028 g    D  .got.plt	0000000000000000  Base        _edata
# 0000000000201028 g    D  .bss	0000000000000000  Base        __bss_start
# 0000000000000550 g    DF .init	0000000000000000  Base        _init
# 000000000000069c g    DF .fini	0000000000000000  Base        _fini
# 0000000000000685 g    DF .text	0000000000000014  Base        sum(int, int)

# --demangle:
# C++中的模板/泛型是依靠name mangling实现的
# objdump增加--demangle参数可以demangle符号名
# 例如将类似这样的名字：<_Z3sumii@plt>变为<sum(int, int)@plt>





