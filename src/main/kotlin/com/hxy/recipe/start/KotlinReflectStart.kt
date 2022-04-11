package com.hxy.recipe.start

import kotlin.reflect.full.memberProperties

data class Data(
    val str: String,
    val i: Int,
    val l: Long,
    val b: Boolean,
    val d: Double,
)

fun readMembers(obj: Any) {
    obj::class.memberProperties.forEach {
        println("${obj::class.simpleName}.${it.name}=${it.getter.call(obj)}")
    }
}

fun main() {
    readMembers(Data("str", 1, 2L, true, 3.0))
}
