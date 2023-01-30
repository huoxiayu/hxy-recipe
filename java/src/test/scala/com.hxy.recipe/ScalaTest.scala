package com.hxy.recipe

import org.scalatest.matchers.should.Matchers._

object ScalaTest {

	def main(args: Array[String]): Unit = {
		assert(3 == 3)
		val v = 4
		v shouldEqual 4
		v should equal(4)
		val bool = true
		bool should be(true)

		val list = List(1, 2, 3)
		intercept[IndexOutOfBoundsException] {
			println(list(3))
		}
	}

}
