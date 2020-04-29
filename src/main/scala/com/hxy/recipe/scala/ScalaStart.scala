package com.hxy.recipe.scala

import com.hxy.recipe.common.Log
import lombok.extern.slf4j.Slf4j

import scala.collection.mutable

@Slf4j
object ScalaStart extends Log {

	private[ScalaStart] case class Point(x: Int, y: Int)

	private var name: String = _

	def hello() = "hello"

	def hello(msg: String) = s"hello $msg"

	def sayHello(msg: String): Unit = log.info(s"hi $msg")

	def main(args: Array[String]) {
		val hello = "hello"
		log.info(hello)
		log.info(s"$hello")

		info(name)
		name = "name"
		info(name)

		info {
			"hello"
		}

		// block is expression
		val sum = {
			1.to(10).sum
		}
		info(sum)

		// infix expression
		info(1 to 10 sum)

		new Greeter("hello", "!").greet("scala")

		info(Point(1, 1))
		// both true, scala中的==用的是equals，case class默认会生成按值比较的equals方法
		info(Point(1, 1) == Point(1, 1))
		info(Point(1, 1).equals(Point(1, 1)))
		// false，eq判断引用等
		info(Point(1, 1).eq(Point(1, 1)))

		// tuple
		val hostAndPort = ("127.0.0.1", "8080")
		info(hostAndPort)
		info(hostAndPort._1)
		info(hostAndPort._2)

		// deconstruction tuple
		val (host, port) = hostAndPort
		info(s"host:$host, port:$port")

		// use -> construct tuple
		info(1 -> 1 getClass)

		// for expression
		val hostAndPortList = List("127.0.0.1" -> "8080", "192.168.0.1" -> "8888")
		for ((hst, prt) <- hostAndPortList) {
			info(s"hst:$hst, prt:$prt")
		}

		// multi for loop
		for (i <- Seq("a", "b"); j <- 1 to 5) {
			info(s"$i$j")
		}

		val scores = new mutable.HashMap[String, Int]
		// update method
		scores("bob") = 59
		// apply method
		info(scores("bob"))
		info(scores {
			"bob"
		})
		val apply = new Apply(5)
		info("apply(): " + apply(5))
		info("apply{}: " + apply {
			5
		})

		// type String = java.lang.String
		val string: String = "string"
		info(string)
	}

}

class Greeter(prefix: String, suffix: String) extends Log {
	def greet(name: String): Unit = info(prefix + " " + name + suffix)
}

class Apply(val base: Int) {
	def apply(i: Int): Int = base + i
}