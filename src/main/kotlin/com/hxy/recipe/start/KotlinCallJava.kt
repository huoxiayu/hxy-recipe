@file:JvmName("KotlinCallJavaMain")

package com.hxy.recipe.start

fun main(): Unit {
    println(JavaCallKotlin::class.java)

    JavaCallKotlin.javaStaticPrint()

    JavaCallKotlin().javaInstancePrint()
}

val echo: (String) -> Unit = { toEcho: String ->
    println(toEcho)
}

object KotlinObject {

    @JvmStatic
    fun kotlinObjectPrint(): Unit {
        println("kotlinObjectPrint")
    }

}

class KotlinCallJava {

    companion object {
        @JvmStatic
        fun kotlinStaticPrint(): Unit {
            println("kotlinStaticPrint")
        }
    }

    fun kotlinInstancePrint(): Unit {
        println("kotlinInstancePrint")
    }

}

