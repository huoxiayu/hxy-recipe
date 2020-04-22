package com.hxy.recipe

object ScalaStart {

	private val name = "name"

	def hello() = "hello"

	// 字符串插值
	def hello(msg: String) = s"hello $msg"

	def sayHello(msg: String): Unit = println(s"hi $msg")


	def main(args: Array[String]) {
		// 正常函数调用
		println(hello())

		println(hello("scala"))

		// 0参数函数调用简写，实际上是：INVOKESPECIAL com/hxy/recipe/ScalaStart$.name ()Ljava/lang/String;
		println(name)

		// 1参数函数调用简写
		sayHello {
			"scala"
		}

		println {
			hello("scala")
		}

		// block is expression
		println({
			1.to(10).sum
		})

		// infix expression
		println(1 to 10)

		// normal class
		new Greeter("hello", "!").greet("scala")
		// no toString override
		println(new Greeter("[", "]"))
		// judge equality using reference
		println(new Greeter("[", "]") == new Greeter("[", "]"))
		println(new Greeter("[", "]").equals(new Greeter("[", "]")))
		println(new Greeter("[", "]").eq(new Greeter("[", "]")))

		// case class with toString override
		println(Point(1, 1))
		// judge equality by compare value
		println(Point(1, 1) == Point(1, 1))
		println(Point(1, 1).equals(Point(1, 1)))
		println(Point(1, 1).eq(Point(1, 1)))

		// tuple
		val hostAndPort = ("127.0.0.1", "8080")
		println(hostAndPort)
		println(hostAndPort._1)
		println(hostAndPort._2)

		// deconstruction tuple
		val (host, port) = hostAndPort
		println(s"host:$host, port:$port")

		// use -> construct tuple
		println("a" -> 1 getClass)

		// for expression
		val hostAndPortList = List("127.0.0.1" -> "8080", "192.168.0.1" -> "8888")
		for ((hst, prt) <- hostAndPortList) {
			println(s"hst:$hst, prt:$prt")
		}

		println("<-------------------->")


	}


}

class Greeter(prefix: String, suffix: String) {
	def greet(name: String): Unit = println(prefix + " " + name + suffix)

	override def toString(): String = s"Greeter(prefix:$prefix suffix:$suffix)"
}

case class Point(x: Int, y: Int)

case object CaseObj