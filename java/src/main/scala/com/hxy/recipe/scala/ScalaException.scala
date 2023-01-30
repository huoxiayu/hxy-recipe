package com.hxy.recipe.scala

import scala.util.Random

object ScalaException {

	def main(args: Array[String]): Unit = {
		val tryFinally: Int = try {
			1
		} finally {
			2
		}
		println("tryFinally:" + tryFinally)

		val tryCatch: Int = try {
			1 / 0
		} catch {
			case _: Throwable => 2
		}
		println("tryCatch:" + tryCatch)

		val tryCatchFinally: AnyVal = try {
			1 / 0
		} catch {
			case e: Exception => println(e)
		} finally {
			2 + 2
		}

		// scala.runtime.BoxedUnit
		println("tryCatchFinally.getClass:" + tryCatchFinally.getClass)

		// tryCatchFinally:()
		println("tryCatchFinally:" + tryCatchFinally)

		val a = functionWithRuntimeException(2)
		println(a)

		val b = functionWithRuntimeException(4)
		println(b)

		for (_ <- 1 to 10) {
			ex()
		}
	}

	def functionWithRuntimeException(n: Int): Int = {
		if (n % 2 == 0) {
			n / 2
		} else {
			throw new RuntimeException("%s is not even number".format(n)) // 类型为Nothing
		}
	}

	def functionWithCheckedException(n: Int): Int = {
		if (n % 2 == 0) {
			n / 2
		} else {
			throw new Exception("%s is not even number".format(n)) // 类型为Nothing
		}
	}

	// scala不强制要求捕获所有异常
	def ex(): Unit = {
		try {
			val rand = math.abs(Random.nextInt()) % 3
			if (rand == 0) throw Ex0("ex0")
			if (rand == 1) throw Ex1("ex1")
			if (rand == 2) throw Ex2("ex2")
		} catch {
			case Ex0(msg) => println("ex0:" + msg)
			case Ex1(msg) => println("ex1:" + msg)
		}
	}

}

case class Ex0(msg: String) extends Exception(msg)

case class Ex1(msg: String) extends Exception(msg)

case class Ex2(msg: String) extends Exception(msg)

