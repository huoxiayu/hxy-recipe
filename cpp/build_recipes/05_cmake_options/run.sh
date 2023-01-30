#!/bin/bash
set -x 

path=./build 

process() {
	if [ -d $path ]; then
		rm -r $path
	fi
	mkdir -p $path

	build_type=$1
	echo "build_type is: $build_type"
	cd $path && cmake -DCMAKE_BUILD_TYPE=$1 .. && make && time ./a.out 
	cd -
}

process Release

process Debug 
