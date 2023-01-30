package com.hxy.recipe.scala.fp

object ScalaFunctionalProgramming {

  def main(args: Array[String]): Unit = {
    val addType: (Int, Int) => Int = add
    println("addType: " + addType)

    println("add(1, 2): " + add(1, 2))

    val addCurry: Int => Int => Int = curry(add)
    println("addCurry: " + addCurry)

    val addOne: Int => Int = addCurry(1)
    println("addOne: " + addOne)

    println("addOne(9): " + addOne(9))

    val oriFunc: (Int, Int) => Int = uncurry(addCurry)
    println("oriFunc(1, 2): " + oriFunc(1, 2))
  }

  def add(x: Int, y: Int): Int = x + y

  def curry[A, B, C](f: (A, B) => C): A => (B => C) = {
    a: A => b: B => f(a, b)
  }

  def uncurry[A, B, C](f: A => B => C): (A, B) => C = {
    (a: A, b: B) => f(a)(b)
  }

}
