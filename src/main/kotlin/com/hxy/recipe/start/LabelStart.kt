package com.hxy.recipe.start

fun main() {
    run("foo0") {
        foo0()
    }

    run("foo1") {
        foo1()
    }

    run("foo2") {
        foo2()
    }
}

fun run(prompt: String, block: () -> Unit) {
    println("$prompt start")
    block()
    println("$prompt end")
}

fun foo0() {
    loop@ for (i in 1..5) {
        inner_loop@ for (j in 1..5) {
            println("i -> $i, j -> $j")
            if (i == 2 && j == 2) break@loop
            if (j == 3) break@inner_loop
        }
    }
}

fun foo1() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return
        println("it -> $it")
    }
    println("this point is unreachable")
}

fun foo2() {
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit
        println("it -> $it")
    }
    print("done with explicit label")
}

// same to foo2 but with label named: forEach
fun foo3() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach
        println("it -> $it")
    }
    print("done with explicit label")
}