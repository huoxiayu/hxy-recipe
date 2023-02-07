# root CMakeLists.txt
cmake_minimum_required(VERSION 3.23.2)

project(cpp_project_basic_structure LANGUAGES CXX)

# show verbose log when make
set(CMAKE_VERBOSE_MAKEFILE ON)

# 指定C++标准版本
set(CMAKE_CXX_STANDARD 17)  

# 编译参数
set(CMAKE_CXX_FLAGS "-Wall")

# 设置可执行文件的输出目录
set(EXECUTABLE_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/bin)  

# 设置库文件的输出目录
set(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/lib)	 

# 子模块
add_subdirectory(utils)
add_subdirectory(common)
add_subdirectory(application)
