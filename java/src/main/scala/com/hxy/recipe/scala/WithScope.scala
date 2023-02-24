package com.hxy.recipe.scala

object WithScope {
  private def withScope(func: => String): String = {
    println("with-scope")
    func
  }

  def bar(foo: String): String = withScope {
    println("bar: " + foo)
    "222"
  }

  def main(args: Array[String]): Unit = {
    println(
      bar("111")
    );
  }
}
