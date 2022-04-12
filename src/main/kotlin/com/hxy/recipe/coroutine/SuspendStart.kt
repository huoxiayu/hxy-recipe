package com.hxy.recipe.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun foo(): String {
    delay(1000)
    println("foo")
    return "foo-result"
}

suspend fun bar(): String {
    delay(10)
    println("bar")
    return "bar-result"
}

fun main() {
    runBlocking {
        val f = foo()
        println(f)

        val b = bar()
        println(b)
    }
}