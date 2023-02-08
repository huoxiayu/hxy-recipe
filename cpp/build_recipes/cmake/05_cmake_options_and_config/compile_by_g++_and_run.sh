#!/bin/bash

g++ -DANOTHER_CONFIG_H -DIS_MACOS -O3 -std=c++20 main.cpp && ./a.out

