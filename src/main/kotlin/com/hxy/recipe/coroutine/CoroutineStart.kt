package com.hxy.recipe.coroutine

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong

fun main() {
    // launch_()
    // println("<---->")
    // async_()
    // println("<---->")
    job_lifecycle()
}

fun launch_() {
    val c = AtomicLong()
    val start = System.currentTimeMillis()

    for (i in 1..10_0000L) {
        val job: Job = GlobalScope.launch {
            c.addAndGet(1L)
            delay(1000)
        }
        if (i == 1L) {
            println("job $job")
        }
    }

    Thread.sleep(2000L)
    println("cost -> ${System.currentTimeMillis() - start}, v -> ${c.get()}")
}

fun async_() {
    runBlocking {
        val deferred: Deferred<String> = async {
            delay(1000)
            println("async execute")
            "deferred-result"
        }

        println("here")
        val ret = deferred.await()
        println("ret -> $ret")
    }
}

fun Job.log() {
    logX("""
        isActive = $isActive
        isCancelled = $isCancelled
        isCompleted = $isCompleted
    """.trimIndent())
}

fun logX(any: Any?) {
    println("""
================================
$any
Thread:${Thread.currentThread().name}
================================
    """.trimIndent())
}

fun job_lifecycle() {
    runBlocking {
        val job = launch {
            delay(1000L)
        }
        job.log()
        job.cancel()
        job.log()
        delay(1500L)
    }
}
