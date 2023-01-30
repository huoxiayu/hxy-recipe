package com.hxy.recipe.scala

import java.util.concurrent.ThreadLocalRandom

object PatternMatchStart {

	def main(args: Array[String]): Unit = {
		class Car(val make: String, val model: String, val year: Short, val topSpeed: Short)

		object ChopShop {
			def unapply(x: Car) = Some((x.make, x.model, x.year, x.topSpeed))
		}

		val m = new Car("Chevy", "BBA", 1978, 120) match {
			case ChopShop(s, t, _, _) => (s, t)
			case _ => ("Ford", "Edsel")
		}

		println(m)

		object Twice {
			def apply(x: Int): Int = x * 2

			def unapply(z: Int): Option[Int] = if (z % 2 == 0) Some(z / 2) else None
		}

		val x = Twice(21) // 42
		println(x)

		x match {
			case Twice(n) => println(n) // 21
			case _ => println("none match")
		}

		// 等价于
		Twice.unapply(x) match {
			case Some(n) => println(n) // 21
			case _ => println("none match")
		}

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
