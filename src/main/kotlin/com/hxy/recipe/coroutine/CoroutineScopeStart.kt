package com.hxy.recipe.coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine

/**
 * @see kotlinx.coroutines.Dispatchers
 */
fun main() = runBlocking {
    val job: CoroutineContext = Job()
    val scope = CoroutineScope(job)

    scope.launch {
        logX("First start!")
        delay(1000L)
        logX("First end!") // 不会执行
    }

    scope.launch {
        logX("Second start!")
        delay(1000L)
        logX("Second end!") // 不会执行
    }

    scope.launch {
        logX("Third start!")
        delay(1000L)
        logX("Third end!") // 不会执行
    }

    delay(500L)

    scope.cancel()

    delay(1000L)
}
