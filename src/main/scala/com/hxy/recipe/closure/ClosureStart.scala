package com.hxy.recipe.closure

object ClosureStart {

	def main(args: Array[String]): Unit = {
		val multiplier3 = multiplierGenerator(3)
		println(multiplier3(2))

		val multiplier10 = multiplierGenerator(10)
		println(multiplier10(5))
	}

	def multiplierGenerator(factor: Int) = {
		i: Int => i * factor
	}

}
