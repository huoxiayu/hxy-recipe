# Application CMakeLists.txt
# Generate Executable File
cmake_minimum_required(VERSION 3.23.2)

project(APPLICATION LANGUAGES CXX)

set(CMAKE_VERBOSE_MAKEFILE ON)

file(GLOB_RECURSE cpp_files ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/../utils/include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/../common/include)

add_executable(application ${cpp_files})

link_directories(${CMAKE_CURRENT_SOURCE_DIR}/../lib)

target_link_libraries(application utils common)
