package com.hxy.recipe.scala

object ScalaClass {

	def main(args: Array[String]): Unit = {
		val circle = new Circle(1)
		println(circle)
		println(circle.ir)
		val copy = circle.copy()
		println(copy)
		println(copy.ir)
		println(circle == copy)
		println(circle.ir == copy.ir)
	}

}

class Circle(r: Int) {
	require(r > 0)

	val ir: Int = r

	def copy(): Circle = {
		new Circle(r)
	}
}