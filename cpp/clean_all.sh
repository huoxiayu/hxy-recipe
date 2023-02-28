#!/bin/bash

find . -perm +111 -type f | grep -v '.sh'  

list="*.out *dSYM"

for item in $list 
do 
    find . -iname $item | xargs rm -r 
done
