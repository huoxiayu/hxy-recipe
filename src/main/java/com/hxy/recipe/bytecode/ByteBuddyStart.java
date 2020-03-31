package com.hxy.recipe.bytecode;

import com.hxy.recipe.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

@Slf4j
public class ByteBuddyStart {

    private static class Foo {

        public static String staticFoo() {
            return "static foo";
        }

        public String instanceFoo() {
            return "instance foo";
        }

    }

    public static void redefine() {
        long start = System.currentTimeMillis();
        ByteBuddyAgent.install();
        new ByteBuddy()
            .redefine(Foo.class)
            .method(ElementMatchers.named("staticFoo"))
            .intercept(FixedValue.value("redefine static foo"))
            .method(ElementMatchers.named("instanceFoo"))
            .intercept(FixedValue.value("redefine instance foo"))
            .make()
            .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        long cost = System.currentTimeMillis() - start;
        log.info("redefine cost {} millis", cost);
    }

    public static void main(String[] args) {
        log.info("Foo.staticFoo(): {}", Foo.staticFoo());
        log.info("foo.instanceFoo(): {}", new Foo().instanceFoo());
        redefine();
        LogUtil.newLine();
        log.info("Foo.staticFoo(): {}", Foo.staticFoo());
        log.info("foo.instanceFoo(): {}", new Foo().instanceFoo());
    }

}
