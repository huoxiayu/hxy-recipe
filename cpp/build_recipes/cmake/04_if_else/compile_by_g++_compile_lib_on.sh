#!/bin/bash

g++ -c lib.cpp -I. -o lib.o
ar -rcs liblib.a lib.o

g++ main.cpp -L. -llib && ./a.out

