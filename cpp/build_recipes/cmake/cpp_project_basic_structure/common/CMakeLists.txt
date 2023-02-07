# common CMakeLists.txt
cmake_minimum_required(VERSION 3.23.2)

project(common LANGUAGES CXX)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/../utils/include)

file(GLOB_RECURSE cpp_files ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)

link_directories(${CMAKE_CURRENT_SOURCE_DIR} ../lib)

add_library(common STATIC ${cpp_files})
