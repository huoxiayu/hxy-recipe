package com.hxy.recipe.coroutine

import java.util.concurrent.atomic.AtomicLong

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineStart {

}

fun main() {
    val c = AtomicLong()
    val start = System.currentTimeMillis()

    for (i in 1..10_0000L) {
        GlobalScope.launch {
            c.addAndGet(i)
            delay(1000)
        }
    }

    println(System.currentTimeMillis() - start)
    println(c.get())
}
