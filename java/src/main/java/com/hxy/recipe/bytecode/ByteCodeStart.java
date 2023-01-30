package com.hxy.recipe.bytecode;

import java.util.Random;

public class ByteCodeStart {

    public static void main(String[] args) {
        new ByteCodeStart().byteCodeStart("byte code start");
    }

    private void byteCodeStart(String msg) {
        System.out.println(msg);

        // +-*/
        int a = 1, b = 2, c = 3, d = 4, e = 1;
        System.out.println(a + b - c * d / e);

        Random random = new Random();
        int rand = random.nextInt(10);

        for (int i = 0; i < rand; i++) {
            System.out.println(i);
        }

        if (rand > 5) {
            System.out.println("rand > 5");
        }

        generic(90);

        System.out.println(1 + 2 + 3 + "123");

        Interface interfaceInstance = new A();
        interfaceInstance.doSomething();

        new A().doSomething();
        new A().inheritDoSomething();

        new B().doSomething();
        new B().inheritDoSomething();
    }

    private void generic(Integer integer) {
        System.out.println(integer + 10);
    }

    private interface Interface {
        void doSomething();
    }

    private static class A implements Interface {

        @Override
        public void doSomething() {
            System.out.println("A.doSomething");
        }

        public void inheritDoSomething() {
            System.out.println("A.inherit.doSomething");
        }

    }

    private static class B extends A implements Interface {

        @Override
        public void doSomething() {
            System.out.println("B.doSomething");
        }

        @Override
        public void inheritDoSomething() {
            System.out.println("B.inherit.doSomething");
        }

    }

}
