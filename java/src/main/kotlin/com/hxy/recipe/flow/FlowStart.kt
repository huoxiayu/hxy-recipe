package com.hxy.recipe.flow

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

/**
 * @see  kotlinx.coroutines.flow.SharedFlow
 */
fun main() {
    runBlocking {
        val flow = flow {
            (1..3).forEach {
                println("generate $it")
                emit(it)
            }
        }
        println("start")
        flow.map {
            val ret = it * 2
            println("map from $it -> $ret")
            ret
        }.take(3).collect { println("hi $it") }
        println("end")
    }
}
