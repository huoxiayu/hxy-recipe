package com.hxy.recipe.scala

import java.util.concurrent.ThreadLocalRandom

object PatternMatchStart {

	def main(args: Array[String]): Unit = {
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
	}

}
