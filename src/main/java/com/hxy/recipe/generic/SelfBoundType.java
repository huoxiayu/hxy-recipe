package com.hxy.recipe.generic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelfBoundType {

    public static void main(String[] args) {

    }

    // 泛型模板类A
    private static class A<T> {
        T property;

        void setProperty(T t) {
            property = t;
        }

        T getProperty() {
            return property;
        }
    }

    private static class B {
    }

    // 正常用法
    private static class C extends A<B> {
    }

    // 泛型参数 = 自己
    private static class D extends A<D> {
    }

    // 自限定泛型模板，限定E只能是E的子类
    private static class E<T extends E> {
        T property;

        void setProperty(T t) {
            property = t;
        }

        T getProperty() {
            return property;
        }
    }

    private static class F extends E<F> {

    }

}
