cmake_minimum_required(VERSION 3.23.2)

project(cmake_04 LANGUAGES CXX)

# introduce a compile toogle 
option(compile_lib "compile_lib" ON)
message(STATUS "compile_lib: ${compile_lib}")

list(APPEND _sources_of_lib lib.h lib.cpp)
message(STATUS "_sources_of_lib: ${_sources_of_lib}")

if(compile_lib)
  add_library(lib STATIC ${_sources_of_lib})

  add_executable(a.out main.cpp)

  target_link_libraries(a.out lib)
else()
  add_executable(a.out main.cpp ${_sources_of_lib})
endif()
