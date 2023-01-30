package com.hxy.recipe.coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun main() {
    println("main start")

    listOf(listOf(1, 2, 3), listOf(4, 5)).flatten()

    val job: CoroutineContext = Job()
    val scope = CoroutineScope(job)

    scope.launch {
        println("async start")
        val b = async {
            delay(1000L)
            "async"
        }
        b.await()
        println("async end")
    }

    Thread.sleep(2000L)
    println("main end")
}
