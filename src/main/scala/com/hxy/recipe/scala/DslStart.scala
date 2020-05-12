package com.hxy.recipe.scala

import java.util.concurrent.ThreadLocalRandom

import scala.util.{Failure, Success, Try}

object DslStart {

	def main(args: Array[String]): Unit = {
		tryDsp()
		breakDsl()
	}

	def tryDsp(): Unit = {
		1 to 100 foreach { _ =>
			haveATry match {
				case Success(value) => println(s"success return $value")
				case Failure(exception) => println(s"fail for exception $exception")
			}
		}
	}

	def haveATry: Try[Int] = {
		Try {
			if (ThreadLocalRandom.current().nextBoolean()) {
				ThreadLocalRandom.current().nextInt(100) + 1
			} else {
				if (ThreadLocalRandom.current().nextBoolean()) {
					0
				} else {
					throw new RuntimeException("error")
				}
			}
		}
	}

	def breakDsl(): Unit = {
		// implement break by exception(slow)
		import scala.util.control.Breaks._
		breakable {
			while (true) {
				val rand = ThreadLocalRandom.current().nextInt(100)
				if (rand > 10) {
					println(rand)
				} else {
					break
				}
			}
		}
	}

}
