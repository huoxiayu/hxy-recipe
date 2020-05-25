package com.hxy.recipe.scala

/**
  * Function1是逆变的，因此Function1[Any, String]是Function1[Int, String]的子类型
  * Function1部分源码如下：
  * trait Function1[-T1,+R] extends AnyRef { self =>
  * def apply(v1: T1): R
  * }
  */
object GenericInStart extends App {

	val f1: Int => String = (x: Int) => s"Int($x)"
	val f2: Any => String = (x: Any) => s"Any($x)"

	// 需要一个Int => String
	def print(x: Int, func: Int => String): Unit = {
		println(func(x))
	}

	print(1, f1)

	// 传递一个Any => String，即Any => String是Int => String的子类型
	print(1, f2)

}
