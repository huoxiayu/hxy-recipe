package com.hxy.recipe.scala

import java.text.MessageFormat

import com.hxy.recipe.common.Log

import scala.math._

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

	// 变长参数写法
	def sum(xs: Int*): Int = {
		if (xs.isEmpty) {
			0
		} else {
			xs.head + sum(xs.tail: _*)
		}
	}

	def decorate(msg: String, left: String = "[", right: String = "]"): String = left + msg + right

	def main(args: Array[String]): Unit = {
		info(sum(Seq(): _*))
		info(sum(1 to 10: _*))

		// + is method
		val x: BigInt = 123456789
		val y: BigInt = 987654321
		info(x + y)

		// type cast
		info(MessageFormat.format("my favorite number is {0}", 42.asInstanceOf[AnyRef]))

		info(decorate("good"))
		info(decorate(left = "(", right = ")", msg = "good"))
		info(decorate("good", right = ")"))

		info(pow(2, 4))

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
