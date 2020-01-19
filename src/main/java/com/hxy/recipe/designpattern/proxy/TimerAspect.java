package com.hxy.recipe.designpattern.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class TimerAspect implements InvocationHandler {

    private final Object target;

    public TimerAspect(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = method.getName();
        log.info("method {} begin", methodName);
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("method {} end, cost {} millis", methodName, System.currentTimeMillis() - start);
        }
    }

    public static <T> T withTimeAspect(T target) {
        TimerAspect timerAspect = new TimerAspect(target);
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), timerAspect);
    }

}
