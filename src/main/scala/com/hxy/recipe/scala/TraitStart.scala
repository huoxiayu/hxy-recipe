package com.hxy.recipe.scala

import com.hxy.recipe.common.Log

import scala.collection.mutable.ArrayBuffer

object TraitStart extends Log {

	def main(args: Array[String]): Unit = {
		// stackable traits
		val basicQueue = new BasicQueue
		basicQueue.put(1)
		// ArrayBuffer(1)
		info(basicQueue.get())

		// from right to left, double and then plus one
		val magicQueue = new MagicQueue
		magicQueue.put(5)
		// ArrayBuffer(11)
		info(magicQueue.get())
	}

}

trait Queue {
	def get(): Seq[Int]

	def put(i: Int): Unit
}

class BasicQueue extends Queue {
	val buffer = new ArrayBuffer[Int]()

	override def get(): Seq[Int] = buffer

	override def put(i: Int): Unit = buffer += i
}

trait PlusOneQueue extends BasicQueue {
	override def put(i: Int): Unit = super.put(i + 1)
}

trait DoubleQueue extends BasicQueue {
	override def put(i: Int): Unit = super.put(i * 2)
}

class MagicQueue extends BasicQueue with PlusOneQueue with DoubleQueue