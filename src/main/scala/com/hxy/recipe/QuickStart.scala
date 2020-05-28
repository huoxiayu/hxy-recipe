package com.hxy.recipe

import org.scalatest.matchers.should.Matchers._

object QuickStart {

	var debug: Boolean = true

	def print(msg: => Any): Unit = {
		if (debug) {
			println(msg.toString)
		}
	}

	def get: Int = {
		println("call get")
		1
	}

	def main(args: Array[String]): Unit = {
		print(get)

		debug = false

		print(get)

	}

	1 should be(1)
}
