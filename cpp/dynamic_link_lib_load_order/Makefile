# Linux下顺序为：0～5
# -L dir
# 指定编译时动态链接库搜索路径为：dir
# 
# -Wl,options
# Pass options to the linker
# 
# -Wl,-rpath=dir
# Add a directory to the runtime library search path for linker
# ldd指令搜索优先级和round0~5保持一致

CC=gcc
CFLAG=-fPIC -c
LDFLAG=-shared

run0: export LD_LIBRARY_PATH=./lib2
run1: export LD_LIBRARY_PATH=./lib2
run2: export LD_LIBRARY_PATH=./lib2

all: round0 round1 round2 round3 round4 round5

compile:
	mkdir lib0 lib1 lib2 lib3
	
	# lib0
	$(CC) $(CFLAG) -o lib.o lib0.c
	$(CC) $(LDFLAG) lib.o -o liblib.so
	mv liblib.so lib0

	# lib1
	$(CC) $(CFLAG) -o lib.o lib1.c
	$(CC) $(LDFLAG) lib.o -o liblib.so
	mv liblib.so lib1

	# lib2
	$(CC) $(CFLAG) -o lib.o lib2.c
	$(CC) $(LDFLAG) lib.o -o liblib.so
	mv liblib.so lib2

	# lib3
	$(CC) $(CFLAG) -o lib.o lib3.c
	$(CC) $(LDFLAG) lib.o -o liblib.so
	mv liblib.so lib3

	# lib4
	$(CC) $(CFLAG) -o lib.o lib4.c
	$(CC) $(LDFLAG) lib.o -o liblib.so
	mv liblib.so /lib

	# lib5
	$(CC) $(CFLAG) -o lib.o lib5.c
	$(CC) $(LDFLAG) lib.o -o liblib.so
	mv liblib.so /usr/lib

	echo -e "\n"

round0: compile run0 
run0:
	echo "run0"
	# LD_PRELOAD
	export LD_PRELOAD=./lib0/liblib.so && gcc -o a.out main.c -L lib3 -llib -Wl,-rpath=./lib1 && ./a.out 
	echo -e "\n"

round1: compile run1
run1: main.c
	echo "run1"
	# -Wl,-rpath=./lib1
	# readelf -d a.out | less
	# 0x000000000000001d (RUNPATH)            Library runpath: [lib1]
	gcc -o a.out main.c -L lib3 -llib -Wl,-rpath=./lib1 && ./a.out 
	echo -e "\n"

round2: compile run2
run2: main.c
	echo "run2"
	# LD_LIBRARY_PATH
	gcc -o a.out main.c -L lib3 -llib && ./a.out 
	echo -e "\n"
	
round3: compile run3
run3: main.c
	echo "run3"	
	echo `pwd`/lib3 > /etc/ld.so.conf.d/lib.conf
	ldconfig > /dev/null 2>&1 # flush cache
	cat /etc/ld.so.conf.d/lib.conf
	# ./lib3
	gcc -o a.out main.c -L lib3 -llib && ./a.out 
	rm /etc/ld.so.conf.d/lib.conf && ldconfig > /dev/null 2>&1  
	echo -e "\n"
	
round4: compile run4
run4: main.c
	echo "run4"
	# /lib/liblib.so
	gcc -o a.out main.c -L lib3 -llib && ./a.out 
	echo -e "\n"
	
round5: compile run5
run5: main.c
	echo "run5"
	rm /lib/liblib.so
	# /usr/lib/liblib.so
	gcc -o a.out main.c -L lib3 -llib && ./a.out 
	echo -e "\n"
	
clean:
	rm -rf lib.o a.out /lib/liblib.so /usr/lib/liblib.so lib0 lib1 lib2 lib3 
