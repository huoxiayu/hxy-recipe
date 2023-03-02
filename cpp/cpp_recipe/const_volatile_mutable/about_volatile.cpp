// g++ -std=c++20 -O3
// volatile: A situation that is volatile is likely to change suddenly and unexpectedly
// 核心：volatile修饰的东西我们不能对它做出任何假设
// 不同于JVM上的volatile，C++中的volatile没有可见性保证，即：没有任何内存屏障的作用

/*
0000000100003f90 <__Z16without_volatilePi>:
100003f90: 55                           pushq   %rbp
100003f91: 48 89 e5                     movq    %rsp, %rbp
100003f94: 8b 07                        movl    (%rdi), %eax     ;一次内存访问
100003f96: 01 c0                        addl    %eax, %eax       ;add计算
100003f98: 5d                           popq    %rbp
100003f99: c3                           retq
100003f9a: 66 0f 1f 44 00 00            nopw    (%rax,%rax)
 */
// 即：这里无法对volatile变量做任何假设，必须按部就班的执行2次内存访问
int without_volatile(int* p) {
	int x = *p;
	int y = *p;
	return x + y;
}

/*
0000000100003fa0 <__Z13with_volatilePVi>:
100003fa0: 55                           pushq   %rbp
100003fa1: 48 89 e5                     movq    %rsp, %rbp
100003fa4: 8b 07                        movl    (%rdi), %eax     ;第一次内存访问
100003fa6: 03 07                        addl    (%rdi), %eax     ;第二次内存访问+add
100003fa8: 5d                           popq    %rbp
100003fa9: c3                           retq
100003faa: 66 0f 1f 44 00 00            nopw    (%rax,%rax)
 */
int with_volatile(volatile int* p) {
	int x = *p;
	int y = *p;
	return x + y;
}

int main() {
    return 0;
}

