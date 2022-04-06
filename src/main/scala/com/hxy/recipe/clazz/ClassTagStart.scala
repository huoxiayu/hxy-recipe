package com.hxy.recipe.clazz

import scala.reflect.ClassTag
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

object ClassTagStart {

  def getValueFromMap[T](key: String, map: Map[String, Any]): Option[T] = {
    map.get(key) match {
      case Some(value: T) => Some(value)
      case _ => None
    }
  }

  def getValueFromMapWithClassTag[T](key: String, map: Map[String, Any])(implicit t: ClassTag[T]): Option[T] = {
    map.get(key) match {
      case Some(value: T) => Some(value)
      case _ => None
    }
  }

  def main(args: Array[String]): Unit = {
    val map: Map[String, Any] = Map("num" -> 1, "string" -> "string", "animal" -> Animal)

    val num: Option[Int] = getValueFromMap("num", map)
    println(s"num $num")

    val string: Option[String] = getValueFromMap("string", map)
    println(s"string $string")

    val animal: Option[Animal] = getValueFromMap("animal", map)
    println(s"animal $animal")

    // need fail but not
    val assignToLong: Option[Long] = getValueFromMap("animal", map)
    println(s"assignToLong $assignToLong")

    try {
      // class cast exception in runtime
      assignToLong.map(l => l + 1).foreach(println)
    } catch {
      case e: Exception => e.printStackTrace()
    }

    // now will get None with ClassTag
    // val assignToLongUsingClassTag: Option[Long] = getValueFromMapWithClassTag("animal", map)
    // println(s"assignToLongUsingClassTag $assignToLongUsingClassTag")

    List(1, true, 1D).foreach(v => {
      val tp = getTypeTag(v)
      println(tp)
    })
  }

  def getTypeTag[T: TypeTag](t: T): universe.TypeTag[T] = {
    typeTag[T]
  }

  class A

  // 上下文绑定语法：max1等价于max2
  def max1[T](a: T, b: T)(implicit p: Manifest[T]): Unit = {}

  def max2[T: Manifest](a: T, b: T): Unit = {}

}

case class Animal()

