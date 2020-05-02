package com.hxy.recipe.scala

object EnumStart {

  def main(args: Array[String]): Unit = {
    Color.values.foreach(println)
    Sex.values.foreach(println)

    val red = Color.withName("red")
    println(red)
    println(red.getClass)
    val boy = Sex(1)
    println(boy)
    println(boy.getClass)
    println(red.getClass == boy.getClass) // true
  }

}

object Color extends Enumeration {
  val red, yellow, green = Value
}

object Sex extends Enumeration {
  val boy: Sex.Value = Value(1, "boy")
  val girl: Sex.Value = Value(2, "girl")
}