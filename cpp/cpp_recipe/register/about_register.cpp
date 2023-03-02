void go0() {
    for (int i = 0; i < 1000000; i++) {

    }
}

void go1() {
    // register关键字只能起到hint的作用
    register int i = 0;
    for (; i < 1000000; i++) {
        
    }
}

int main() {
    return 0;
}

