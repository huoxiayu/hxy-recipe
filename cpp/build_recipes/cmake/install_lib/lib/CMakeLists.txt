cmake_minimum_required(VERSION 3.0)

project(cmake_install_lib VERSION 1.0)

add_library(cmake_install_lib cmake_install_lib.cc)

include(CMakePackageConfigHelpers)

# 库版本信息
write_basic_package_version_file(
	cmake_install_libConfigVersion.cmake
	VERSION ${PACKAGE_VERSION}
	COMPATIBILITY AnyNewerVersion
)

# install头文件
install(FILES "cmake_install_lib.h" DESTINATION  ${CMAKE_INSTALL_PREFIX}/include/)

# install库文件
install(TARGETS cmake_install_lib
	EXPORT cmake_install_libTargets
	RUNTIME DESTINATION bin
	LIBRARY DESTINATION lib
	ARCHIVE DESTINATION lib
	PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_PREFIX}/include/
)	

# for `find_package`
install(EXPORT cmake_install_libTargets
	FILE cmake_install_libTargets.cmake
	DESTINATION lib/cmake/cmake_install_lib
)

configure_file(cmake_install_libConfig.cmake.in cmake_install_libConfig.cmake @ONLY)
install(FILES 
	"${CMAKE_CURRENT_BINARY_DIR}/cmake_install_libConfig.cmake"
	"${CMAKE_CURRENT_BINARY_DIR}/cmake_install_libConfigVersion.cmake"
	DESTINATION lib/cmake/cmake_install_lib
)
