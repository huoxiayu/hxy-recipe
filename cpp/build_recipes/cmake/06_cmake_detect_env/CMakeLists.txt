cmake_minimum_required(VERSION 3.23.2)

project(cmake-06 LANGUAGES NONE)

# detect python
find_package(PythonInterp REQUIRED)

# Execute a tiny Python script
execute_process(
  COMMAND
    ${PYTHON_EXECUTABLE} "-c" "print('Hello, world!')"
  RESULT_VARIABLE _status
  OUTPUT_VARIABLE _hello_world
  ERROR_QUIET
  OUTPUT_STRIP_TRAILING_WHITESPACE
)

message(STATUS "RESULT_VARIABLE is: ${_status}")
message(STATUS "OUTPUT_VARIABLE is: ${_hello_world}")

# compare the "manual" messages with the following handy helper
include(CMakePrintHelpers)
cmake_print_variables(_status _hello_world)
