package com.hxy.recipe.channel

import com.hxy.recipe.coroutine.logX
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val channel = Channel<Int>()

    launch {
        (1..3).forEach {
            channel.send(it)
            logX("Send: $it")
        }
    }

    launch {
        for (i in channel) {
            logX("Receive: $i")
        }
    }

    logX("end")
}
