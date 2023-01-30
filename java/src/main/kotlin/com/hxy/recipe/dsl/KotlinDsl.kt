package com.hxy.recipe.dsl

fun main() {
    val i = 1
    i.apply {
        println(this)
    }.map {
        it + 1
    }.also {
        println(it)
    }
}

inline fun <T, U> T.map(func: (T) -> U): U {
    return func(this)
}
