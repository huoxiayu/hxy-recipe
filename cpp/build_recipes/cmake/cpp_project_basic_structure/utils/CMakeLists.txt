# util CMakeLists.txt
cmake_minimum_required(VERSION 3.23.2)

project(utils LANGUAGES CXX)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src)

file(GLOB_RECURSE cpp_files ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)
add_library(utils STATIC ${cpp_files})
