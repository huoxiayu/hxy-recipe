package com.hxy.recipe.scala

object SelfTypeStart {

	def main(args: Array[String]): Unit = {
		val up = new UserPrint with Print
		up.selfPrint()
		up.print()

		val outer = new Outer
		val inner = new outer.Inner
		println(inner)
	}

}

trait Print {
	def print(): Unit = {
		println("print")
	}
}

class UserPrint {
	self: Print =>

	def selfPrint(): Unit = {
		self.print()
	}
}

class Outer {
	outer =>
	val v = "outer"

	class Inner {
		inner =>
		val v = "inner"
		println(outer.v)
		println(inner.v)
		println(this.v)
	}

}