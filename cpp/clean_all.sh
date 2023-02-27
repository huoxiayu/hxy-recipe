#!/bin/bash

list="*.out *dSYM"

for item in $list 
do 
    find . -iname $item | xargs rm -r 
done
