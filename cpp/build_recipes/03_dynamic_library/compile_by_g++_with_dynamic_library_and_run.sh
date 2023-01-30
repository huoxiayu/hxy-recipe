#!/bin/bash


g++ -fPIC -shared lib.cpp -I. -o liblib.so

g++ main.cpp -L. -llib && ./a.out

