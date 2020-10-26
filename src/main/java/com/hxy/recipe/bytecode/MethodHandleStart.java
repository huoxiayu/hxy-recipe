package com.hxy.recipe.bytecode;

import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 打印隐藏栈信息
 * -XX:+UnlockDiagnosticVMOptions -XX:+ShowHiddenFrames
 * -Djava.lang.invoke.MethodHandle.DUMP_CLASS_FILES=true
 */
@Slf4j
public class MethodHandleStart {

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup l = MethodHandles.lookup();
        MethodType t = MethodType.methodType(void.class, Object.class);
        MethodHandle mh = l.findStatic(MethodHandleStart.class, "methodHandleStackTrace", t);
        mh.invokeExact(new Object());
    }

    private static void methodHandleStackTrace(Object ignore) {
        new Exception().printStackTrace();
    }

}
