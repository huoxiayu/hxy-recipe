package com.hxy.recipe.scala

object YieldStart {

  def main(args: Array[String]): Unit = {
    val a = for (i <- 1 to 5) yield i * 2
    println(a)
  }

}
