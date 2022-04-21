package com.hxy.recipe.stack;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StackWalkerStart {

    public void methodOne() {
        this.methodTwo();
    }

    public void methodTwo() {
        this.methodThree();
    }

    // stack walking code
    public void methodThree() {
        List<StackWalker.StackFrame> stackTrace = StackWalker
                .getInstance()
                .walk(this::walk);
        log.info("stackTrace -> {}", stackTrace);
    }

    public List<StackWalker.StackFrame> walk(Stream<StackWalker.StackFrame> stackFrameStream) {
        return stackFrameStream.collect(Collectors.toList());
    }

    public static void main(String[] args) {
        StackWalkerStart sws = new StackWalkerStart();
        sws.methodOne();
    }

}
