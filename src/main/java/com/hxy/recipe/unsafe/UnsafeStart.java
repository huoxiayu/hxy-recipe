package com.hxy.recipe.unsafe;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

@Slf4j
public class UnsafeStart {

    private static class A {
        private A() {
            throw new RuntimeException("not support A");
        }

        @Override
        public String toString() {
            return "A:" + super.toString();
        }
    }

    public static void main(String[] args) throws InstantiationException {
        Unsafe unsafe = getUnsafe();
        log.info("unsafe {}", unsafe);

        // no exception throw
        Object o = unsafe.allocateInstance(A.class);
        log.info("o: {}", o);

        // throw exception
        new A();
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
