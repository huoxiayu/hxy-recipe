package com.hxy.recipe.scala

import com.hxy.recipe.common.Log

object FunctionStart extends Log {

	def notImplementedMethod(i: Int): Int = ???

	def factorial(x: Int): Int = {
		def fact(x: Int, accumulator: Int): Int = {
			if (x <= 1) {
				accumulator
			} else {
				fact(x - 1, x * accumulator)
			}
		}

		fact(x, 1)
	}

	def main(args: Array[String]): Unit = {
		info("function compose")
		val double_func: Int => Int = i => i * 2
		val plus_one_func: Int => Int = i => i + 1
		1 to 10 map double_func.andThen(plus_one_func) foreach info

		// currying
		info("currying")
		val multiply: Int => Int => Int = factor => _ * factor
		val multiply3 = multiply(3)
		info(multiply3(3))
		info(multiply(5)(3))

		info("nested method")
		info(factorial(4))

		info("partial function")

		info("not implemented method")
		notImplementedMethod(1)
	}

}
