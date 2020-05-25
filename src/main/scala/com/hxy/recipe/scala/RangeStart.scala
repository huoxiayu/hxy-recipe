package com.hxy.recipe.scala

object RangeStart {

	def main(args: Array[String]): Unit = {
		1 to 3 foreach println
		Range(1, 3, 2).foreach(print)
		println
		Range(1, 3, 2).inclusive.foreach(print)
	}

}
