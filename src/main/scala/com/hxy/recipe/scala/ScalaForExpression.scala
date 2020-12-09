package com.hxy.recipe.scala

object ScalaForExpression {

	def main(args: Array[String]): Unit = {
		val numList = 1 until 10
		for (num <- numList if num % 2 == 0 if num % 3 == 0) println(num)

		val list1 = List(1, 2, 3)
		val list2 = List(2, 3, 4)
		for (item1 <- list1 if item1 % 2 != 0; item2 <- list2 if item2 % 2 == 0) println(item1 + ":" + item2)

		val strList = List("yes_a", "yes_b", "no_c", " yes_d ", " yes_e", "yes_f ")
		for {
			str <- strList
			trim = str.trim
			if trim.contains("yes")
		} println(str)

		val intStrList = List("1", "2", "3")
		val intList = for (intStr <- intStrList) yield intStr.toInt
		println(intList)
	}

}
