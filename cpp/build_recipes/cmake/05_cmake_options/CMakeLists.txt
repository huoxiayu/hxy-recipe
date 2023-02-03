cmake_minimum_required(VERSION 3.23.2)

project(cmake-05 LANGUAGES C CXX)

# show verbose log when make
set(CMAKE_VERBOSE_MAKEFILE ON)

message("system is: ${CMAKE_SYSTEM_NAME}")

message("C++ compiler flags: ${CMAKE_CXX_FLAGS}")

# compiler info
message(STATUS "Is the C++ compiler loaded? ${CMAKE_CXX_COMPILER_LOADED}")
if(CMAKE_CXX_COMPILER_LOADED)
  message(STATUS "The C++ compiler ID is: ${CMAKE_CXX_COMPILER_ID}")
  message(STATUS "Is the C++ from GNU? ${CMAKE_COMPILER_IS_GNUCXX}")
  message(STATUS "The C++ compiler version is: ${CMAKE_CXX_COMPILER_VERSION}")
endif()

message(STATUS "Is the C compiler loaded? ${CMAKE_C_COMPILER_LOADED}")
if(CMAKE_C_COMPILER_LOADED)
  message(STATUS "The C compiler ID is: ${CMAKE_C_COMPILER_ID}")
  message(STATUS "Is the C from GNU? ${CMAKE_COMPILER_IS_GNUCC}")
  message(STATUS "The C compiler version is: ${CMAKE_C_COMPILER_VERSION}")
endif()

# build type(Release/Debug)
if(NOT CMAKE_BUILD_TYPE)
  set(CMAKE_BUILD_TYPE Release)
endif()

# compile options
## global
add_compile_options(-std=c++20)

# better way to specify c++ standard
# set(CMAKE_CXX_STANDARD 20)

add_executable(a.out main.cpp)

target_include_directories(a.out
  PRIVATE
    ${PROJECT_BINARY_DIR}
)

list(APPEND flags "-Wall" "-Werror")
## specify target
target_compile_options(a.out PRIVATE ${flags})

# target_compile_definitions(-D can be leave out)
if(CMAKE_SYSTEM_NAME STREQUAL "Linux")
  target_compile_definitions(a.out PUBLIC "IS_LINUX")
endif()
if(CMAKE_SYSTEM_NAME STREQUAL "Darwin")
  target_compile_definitions(a.out PUBLIC "IS_MACOS")
endif()
if(CMAKE_SYSTEM_NAME STREQUAL "Windows")
  target_compile_definitions(a.out PUBLIC "IS_WINDOWS")
endif()

message("C++ compiler flags: ${CMAKE_CXX_FLAGS}")

# host processor instruction info
foreach(key
  IN ITEMS
    NUMBER_OF_LOGICAL_CORES
    NUMBER_OF_PHYSICAL_CORES
    TOTAL_VIRTUAL_MEMORY
    AVAILABLE_VIRTUAL_MEMORY
    TOTAL_PHYSICAL_MEMORY
    AVAILABLE_PHYSICAL_MEMORY
    IS_64BIT
    HAS_FPU
    HAS_MMX
    HAS_MMX_PLUS
    HAS_SSE
    HAS_SSE2
    HAS_SSE_FP
    HAS_SSE_MMX
    HAS_AMD_3DNOW
    HAS_AMD_3DNOW_PLUS
    HAS_IA64
    OS_NAME
    OS_RELEASE
    OS_VERSION
    OS_PLATFORM
  )
  cmake_host_system_information(RESULT _${key} QUERY ${key})
endforeach()
configure_file(config.h.in config.h @ONLY)
