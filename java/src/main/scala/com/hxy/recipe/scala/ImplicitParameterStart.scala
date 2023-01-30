package com.hxy.recipe.scala

import com.hxy.recipe.common.Log

object ImplicitParameterStart extends Log {

	abstract class Adder[A] {
		def add(x: A, y: A): A

		def unit: A
	}

	implicit val stringAdder: Adder[String] = new Adder[String] {
		def add(x: String, y: String): String = x concat y

		def unit: String = ""
	}

	implicit val intAdder: Adder[Int] = new Adder[Int] {
		def add(x: Int, y: Int): Int = x + y

		def unit: Int = 0
	}

	def sum[A](xs: List[A])(implicit m: Adder[A]): A = if (xs.isEmpty) m.unit else m.add(xs.head, sum(xs.tail))

	def main(args: Array[String]): Unit = {
		// uses stringAdder implicitly
		println(sum(List("a", "b", "c")))

		// uses intAdder implicitly
		println(sum(List(1, 2, 3)))
	}

}
