package com.hxy.recipe.start

class KotlinLazyStart {
    val s: String by lazy {
        println("processing")
        "str"
    }
}


fun main() {
    val go = KotlinLazyStart()
    println(go.s)
    println(go.s)
    println(go.s)
}