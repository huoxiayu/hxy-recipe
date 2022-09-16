package com.hxy.recipe.scala

import scala.language.reflectiveCalls

object DuckTyping {

  case class A() {
    def msg() = "aaa"
  }

  case class B() {
    def msg() = "bbb"
  }

  // it is slow, because `structural types` rely on runtime reflection(see bytecode)
  // call_msg takes any object that has a msg method
  // Type classes can express the same thing!
  def call_msg(x: {def msg(): String}): Unit = {
    val msg = x.msg()
    println(msg)
  }

  def main(args: Array[String]): Unit = {
    call_msg(A())
    call_msg(B())
  }

}
