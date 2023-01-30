package com.hxy.recipe.scala

import com.hxy.recipe.common.Log

import scala.collection.mutable.ArrayBuffer
import scala.language.{implicitConversions, postfixOps}

object ImplicitConvertStart extends Log {

  implicit class ImplicitRangeConvert(val start: Int) extends AnyVal {
    def -->(end: Int): Range.Inclusive = start to end
  }

  def main(args: Array[String]): Unit = {
    val arrayBuffer = ArrayBuffer("ls", "-al", "~")
    import scala.collection.JavaConverters._
    val processBuilder = new ProcessBuilder(arrayBuffer.asJava)
    val result = processBuilder.command().asScala
    val resultClazz = result.getClass
    info(s"result: $result, resultType: $resultClazz")

    // implicit convert
    info("hello".intersect("word"))

    // normal invoke
    new Person("hxy").eat("dumplings")
    import ImplicitPersonConvert.implicitConvert
    // using implicit convert
    "hxy".eat("dumplings")

    // infix format
    "hxy" eat "dumplings"

    info(1 -> 10)
    info(1 -> 10 getClass)

    info(1 → 10)
    info(1 → 10 getClass)

    val x = 1 --> 10
    info(x)
    info(x.getClass)
  }

}


class Person(val name: String) extends Log {
  def eat(food: String): Unit = info(s"$name eat $food")
}

object ImplicitPersonConvert {
  implicit def implicitConvert(name: String): Person = new Person(name)
}


