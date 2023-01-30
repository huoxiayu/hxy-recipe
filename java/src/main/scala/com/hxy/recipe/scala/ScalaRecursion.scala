package com.hxy.recipe.scala

object ScalaRecursion {

	def main(args: Array[String]): Unit = {
		// 一个tailRecursion的栈帧-尾递归被优化成了循环
		tryWithExceptionPrint {
			tailRecursion(10)
		}

		// 多一个tailRecursion的栈帧-递归调用
		tryWithExceptionPrint {
			notTailRecursion(10)
		}
	}

	def tryWithExceptionPrint(r: => Unit): Unit = {
		try {
			r
		} catch {
			case e: Throwable => e.printStackTrace()
		}
	}

	def tailRecursion(x: Int): Int = {
		if (x == 0) {
			throw new RuntimeException("boom")
		} else {
			tailRecursion(x - 1)
		}
	}

	def notTailRecursion(x: Int): Int = {
		if (x == 0) {
			throw new RuntimeException("boom")
		} else {
			notTailRecursion(x - 1) + 1
		}
	}

}
