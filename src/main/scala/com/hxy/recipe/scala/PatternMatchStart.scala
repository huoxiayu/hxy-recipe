package com.hxy.recipe.scala

import java.util.concurrent.ThreadLocalRandom

object PatternMatchStart {

	def main(args: Array[String]): Unit = {
		val tuple3 = (1, "a", 3.14)
		val (first, second, _) = tuple3
		println(s"first: $first, second: $second")

		val rand = ThreadLocalRandom.current().nextInt(100)
		println(rand)

		val sum = 1.to(rand).sum
		println(sum)

		println {
			1.to(rand).sum match {
				case i if i < 10 => i * 2
				case i if i < 100 => i + 10
				case i => i
			}
		}

		arrayUnpack(null)
		arrayUnpack("1".split(","))
		arrayUnpack("1,2".split(","))
		arrayUnpack("1,2,3".split(","))
	}

	def arrayUnpack(array: Array[String]): Unit = {
		array match {
			case Array(onlyOne) => println(s"onlyOne: $onlyOne")
			case Array(left, right) => println(s"left: $left, right: $right")
			case _ => println("none match")
		}
	}

}
