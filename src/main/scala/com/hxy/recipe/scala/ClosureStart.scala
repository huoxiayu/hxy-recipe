package com.hxy.recipe.scala

/**
  * @see scala.runtime.IntRef
  */
object ClosureStart {

	def main(args: Array[String]): Unit = {
		var inc = 1

		val incFunc = (x: Int) => x + inc
		val call = (x: Int, y: Int => Int) => y(x)

		println(incFunc(1))
		println(call(1, incFunc))

		inc += 1

		println(incFunc(1))
		println(call(1, incFunc))

		val numbers = List(1, 2, 3, 4, 5)
		var sum = 0
		numbers.foreach(sum += _)
		println(sum)
	}

}
