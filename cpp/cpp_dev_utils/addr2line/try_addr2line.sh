#!/bin/bash

# -g: produce debugging information
# -Wl,option: pass option as an option to the linker
# -Map=mapfile: print a link map to the file mapfile.  
gcc -g main.c -o main -Wl,-Map=code.map

# 0x000000000040052d divide
grep divide code.map

# divide
# main.c:3
# 即：divide在main.c的第3行定义
addr2line 0x000000000040052d -e main -f -C -s

# Floating point exception (core dumped)
./main 

# [6376098.807970] traps: main[92476] trap divide error ip:40053b sp:7ffd8f557f70 error:0 in main[400000+1000]
dmesg 

# divide
# main.c:4
# 即：在main.c的第4行调用divide的时候发生了Floating point exception
addr2line 40053b -e main -f -C -s





