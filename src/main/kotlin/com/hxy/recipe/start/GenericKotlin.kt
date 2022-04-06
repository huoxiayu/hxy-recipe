package com.hxy.recipe.start

import java.util.*

/**
 * @see com.hxy.recipe.generic.GenericEraseJava
 */
fun main() {
    repeat(10) {
        val listOf: List<Number> = list()
        listOf.forEach { println(check(it)) }
        println("<-------------------->")
    }
}

fun list(): List<Number> {
    return when (Random().nextInt() % 3) {
        0 -> {
            println("Int-Int")
            listOf(1, 2)
        }
        1 -> {
            println("Double-Double")
            listOf(1.0, 2.0)
        }
        else -> {
            println("Double-Int")
            listOf(1.0, 2)
        }
    }
}

// No Problem!
inline fun <reified T : Number> check(value: T): Class<T> {
    return value.javaClass
}
