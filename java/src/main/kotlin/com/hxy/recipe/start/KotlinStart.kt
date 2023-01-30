package com.hxy.recipe.start
// package

// import
import com.hxy.recipe.util.KotlinUtils
import kotlin.math.abs
import kotlin.random.Random

// main
fun main() {
    // basic()

    // branchAndLoop()

    // array()

    // collection()

    // func()

    // extendFunc()

    clazz()

    alias()

    delegate()

    label()

    other()
}

fun basic(): Unit {
    // no need ;

    val a: Int = 1  // final
    println("a: $a")

    var b: Long = 5 // not final
    println("b: $b")

    b = 7
    println("b: $b")

    KotlinUtils.print() // function call

    val multiLines = """
        first line
        second line
        last line
    """.trimIndent()
    println(multiLines)

    val str1: String = "string"
    var str2: String? = null

    println("str2.len: " + ((str2?.length) ?: 0))

    str2 = str1 // ok
    // str1 = str2           this line is illegal
    // str1 = str2!!         throws KotlinNullPointerException
    println("str2.len: " + str2.length)
}

fun branchAndLoop() {
    val bool = Random(System.currentTimeMillis()).nextBoolean()
    if (bool) {
        println("true if")
    } else {
        println("false if")
    }

    val ifExpression = if (bool) 1 else 0
    println("ifExpression: $ifExpression")

    val whenStr = when (ifExpression) {
        1 -> "yes"
        0 -> "no"
        else -> throw RuntimeException("unreachable")
    }
    println("whenStr: $whenStr")

    println(1..10)

    for (i in 10 downTo 1 step 2) {
        println("for loop -> $i")
    }
}

fun array(): Unit {
    val intArray = intArrayOf(1, 2, 3)
    println("intArray: ${intArray.contentToString()}")

    for (i in intArray.indices) {
        println("visit by indices: ${intArray[i]}")
    }

    val longArray = LongArray(3) { i -> i * 2L }
    println("longArray: ${longArray.contentToString()}")

    val strArray = Array(3) { i -> i.toString() }
    println("strArray: ${strArray.contentToString()}")
}

fun collection(): Unit {
    /**
     * @see Collection
     * @see MutableCollection
     */
    val mList = mutableListOf(1, 2, 3)
    println("mList: $mList")

    mList.add(4)
    println("mList: $mList")

    val set = setOf(1, 2) // no add
    println("set: $set")

    val map = mapOf(1 to 2, 3 to 4, 5 to 6)
    println("map: $map")
}

/**
 * @see kotlin.jvm.functions.Function0
 * @see kotlin.jvm.functions.Function22
 */
fun func(): Unit {
    // declare function
    fun oneFunc(func: () -> Unit): Unit {
        func()
    }

    fun funcNamedParam(first: Int = 1, second: Int = 2): Int = first + second
    println(funcNamedParam())
    println(funcNamedParam(first = 6))
    println(funcNamedParam(second = 9))

    val unitFunc: () -> Unit = {}
    oneFunc(unitFunc)
    oneFunc { println("oneFunc") }

    fun abs(a: Int) = kotlin.math.abs(a)

    val anotherAbs: (Int) -> Int = { a: Int -> abs(a) }
    println("anotherAbs(-7): ${anotherAbs(7)}")

    val echo: (String) -> Unit = { toEcho: String ->
        println(toEcho)
    }
    echo("echo")
    echo.invoke("echo.invoke")

    val refEcho = echo::invoke
    refEcho("refEcho")

    fun repeat(cnt: Int = 5, action: () -> Unit): Unit {
        if (cnt > 0) {
            action()

            repeat(cnt - 1, action)
        }
    }

    repeat(1) { println("hi") }

    newLine()

    repeat { println("hi") }

    newLine()

    fun twoFuncCompose(action1: () -> Unit, action2: () -> Unit): Unit {
        action1()
        action2()
    }

    val a1: () -> Unit = { println("action1") }
    twoFuncCompose(a1) { println("action2") }

    newLine()

    val str = "closure"
    fun closure(): Unit {
        println(str)
    }

    closure()

    newLine()

    var strVar = "var-closure-1"
    fun varClosure(): Unit {
        println(strVar)
    }
    varClosure()

    newLine()

    strVar = "var-closure-2"
    varClosure()

    val f: () -> Unit = Runnable {
        println("runnable")
    }::run

    println("f: $f")
    f.invoke()
}

// 扩展是静态解析的
fun extendFunc(): Unit {

    fun <T> List<T>.print(): Unit {
        for (i in this) {
            println("List<T>.print: $i")
        }
    }

    listOf(1, 2, 3).print()
}

fun interface Aop {
    fun print(): Unit
}

class Biz(aop: Aop) : Aop by aop {

}

class OverrideBiz(aop: Aop) : Aop by aop {

    override fun print(): Unit {
        println("OverrideBiz")
    }

}

fun delegate(): Unit {
    val aop = Aop { println("Aop") }
    Biz(aop).print()
    // override success
    OverrideBiz(aop).print()
}

fun label(): Unit {

}

fun clazz(): Unit {
    // 不声明open默认都是不允许继承的final的类
    open class A(val a: String) {
        open fun print(): Unit {
            println(a)
        }
    }

    class B(a: String, val b: String) : A(a) {

        override fun print(): Unit {
            println(b)
        }

    }

    val a = A("a")
    println(a)

    a.print()

    val b = B("a", "b")
    println(b)

    b.print()

    data class Man(val name: String, val age: Int)

    val man = Man("hxy", 16)
    println(man)

    println(man.hashCode())

    println(man.copy(name = "huoxiayu"))

    val (name, age) = man
    println("decompose -> name: $name, age: $age")

    val fruit = when (abs(Random(System.currentTimeMillis()).nextInt(3))) {
        0 -> Fruit.Apple
        1 -> Fruit.Pear
        2 -> Fruit.Other("other")
        else -> throw RuntimeException()
    }
    println(fruit)

    // complete branch, no else
    val kind = when (fruit) {
        Fruit.Apple -> "apple"
        Fruit.Pear -> "pear"
        is Fruit.Other -> fruit.name
    }
    println(kind)
}

sealed class Fruit {

    object Apple : Fruit()

    object Pear : Fruit()

    data class Other(val name: String) : Fruit()

}

typealias IntSet = HashSet<Int>
typealias Int2LongFunc = (Int) -> (Long)
typealias BiConsumer<X, Y> = (X, Y) -> Unit

// sam types: single-abstract-method-types
fun interface Int2LongInterface {
    operator fun invoke(int: Int): Long
}

fun alias(): Unit {
    val intSet: IntSet = IntSet(10)
    intSet.addAll(listOf(1, 2, 3))
    println("intSet: $intSet")

    val i2L = Int2LongInterface { it.toLong() }
    println(i2L(10))

    val i2LFunc: Int2LongFunc = { i2L.invoke(it) }
    println(i2LFunc(10))
    println(i2LFunc(10))

    val printConsumer: BiConsumer<Int, Long> = { a: Int, b: Long -> println("a: $a, b: $b") }
    printConsumer(1, 2L)
}

fun other(): Unit {

}

fun newLine(): Unit {
    println("<---------------->")
}