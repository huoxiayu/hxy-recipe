package com.hxy.recipe.delegate

class DelegateStart {
    // empty
}

interface Do {
    fun doSomething();
}

class Print1 : Do {
    override fun doSomething() {
        println("print1")
    }
}

class Print2 : Do {
    override fun doSomething() {
        println("print2")
    }
}

class Delegate(doer: Do) : Do by doer

fun main() {
    val p1 = Print1()
    val p2 = Print2()
    Delegate(p1).doSomething()
    Delegate(p2).doSomething()
}