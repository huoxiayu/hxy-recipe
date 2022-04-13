package com.hxy.recipe.collection

fun main() {
    collectionStart()
    println("<----------->")
    sequenceStart()
}

fun collectionStart() {
    val list = listOf(1, 2, 3)
    val sum = list.map {
        println("mapping1 $it")
        it * 2
    }.map {
        println("mapping2 $it")
        it + 1
    }.sumOf { it }
    println("sum -> $sum")
}

// lazy
fun sequenceStart() {
    val list = sequenceOf(1, 2, 3)
    val sum = list.map {
        println("mapping1 $it")
        it * 2
    }.map {
        println("mapping2 $it")
        it + 1
    }.filter {
        it > 0
    }.take(3).sumOf { it }
    println("sum -> $sum")
}
