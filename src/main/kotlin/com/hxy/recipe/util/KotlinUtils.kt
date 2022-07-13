package com.hxy.recipe.util

object KotlinUtils {

    fun print(): Unit {
        println("utils.print")
    }

}

fun main() {
    val list = listOf(1, 2, 3)
    println(list)

    val mapList = list.map { it + 1 }
    println(mapList)

    mapList.forEach { println("ele -> $it") }
}