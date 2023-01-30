package com.hxy.recipe.coroutine;

import kotlin.coroutines.Continuation;

public class JavaCoroutineStart {

    public static void main(String[] args) {
        Continuation<? super String> $completion = null;
        SuspendStartKt.foo($completion);
        SuspendStartKt.bar("str", $completion);
    }

}
