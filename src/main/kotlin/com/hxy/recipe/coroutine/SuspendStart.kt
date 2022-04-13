package com.hxy.recipe.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun foo(): String {
    delay(1000)
    println("foo")
    return "foo-result"
}

suspend fun bar(str: String): String {
    delay(10)
    println(str)
    return "$str-result"
}

fun main() {
    runBlocking {
        val f = foo()
        println(f)

        val b = bar("bar")
        println(b)
    }
}