package com.hxy.recipe.agent;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentStart {

    private static class Foo {
        public void foo() {
            log.info("Foo.foo() called");
        }
    }

    public static void main(String[] args) {
        Foo foo = new Foo();
        do {
            foo.foo();
            Utils.sleep(2L);
        } while (true);
    }

}
