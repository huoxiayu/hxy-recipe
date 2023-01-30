cmake_minimum_required(VERSION 3.23.2)

project(cmake_02 LANGUAGES CXX)

add_library(lib
    STATIC 
        lib.h 
        lib.cpp
)

add_executable(a.out main.cpp lib.cpp)
