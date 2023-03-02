thread_local int v = 1;
int vv = 2;

/*
0000000000001140 <_Z2goi>:
1140:       f3 0f 1e fa             endbr64
1144:       64 8b 04 25 fc ff ff    mov    %fs:0xfffffffffffffffc,%eax    ;用户态使用fs寄存器引用glibc线程中的TLS（Thread Local Storage）
114b:       ff
114c:       01 f8                   add    %edi,%eax
114e:       03 05 bc 2e 00 00       add    0x2ebc(%rip),%eax        # 4010 <vv>
1154:       c3                      retq
1155:       66 2e 0f 1f 84 00 00    nopw   %cs:0x0(%rax,%rax,1)
115c:       00 00 00
115f:       90                      nop
*/
int go(int x) {
    int sum = x;
    sum += v;
    sum += vv;
    return sum;
}

int main() {
    return 0;
}

