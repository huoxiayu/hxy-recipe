package com.hxy.recipe.start

fun main() {
    foo(arrayOf(0, 1, 2, 3, 4))
    println("<---------->")
    fooWithReturnLabel(arrayOf(0, 1))
}

fun foo(array: Array<Int>) {
    repeat(2) {
        array.forEach {
            if (it == 2) {
                return
            } else {
                println("receive $it")
            }
        }
    }
}

fun fooWithReturnLabel(array: Array<Int>) {
    repeat(2) {
        array.forEach {
            if (it == 2) {
                // because forEach is inlined
                return@forEach
            } else {
                println("receive $it")
            }
        }
    }
}
