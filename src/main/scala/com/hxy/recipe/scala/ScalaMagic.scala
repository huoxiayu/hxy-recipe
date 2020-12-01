package com.hxy.recipe.scala

object ScalaMagic {

	// 编译成字节码时会尽可能地使用基本类型
	def plus(a: Int, b: Int): Int = a + b

	// scala中的==其实也是方法调用，编译器会做左侧操作数做是否为null的判断
	null == List(1, 2, 3)

	def main(args: Array[String]): Unit = {

	}

}
