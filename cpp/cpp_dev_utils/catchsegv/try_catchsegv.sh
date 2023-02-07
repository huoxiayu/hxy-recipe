#!/bin/bash

gcc -g main.c -o main

# see: catchsegv.output
catchsegv ./main

