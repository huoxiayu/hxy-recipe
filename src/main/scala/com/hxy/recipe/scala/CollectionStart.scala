package com.hxy.recipe.scala

import com.hxy.recipe.common.Log

object CollectionStart extends Log {

	private[CollectionStart] case class Point(x: Int, y: Int, v: Int)

	def main(args: Array[String]): Unit = {
		val nums = List(1, 2, 3, 4, 5)
		info(nums.sum)
		info(nums.min)
		info(nums.max)
		info(nums.forall(_ > 2))
		info(nums.exists(_ % 2 == 0))
		nums.sliding(2, 3).foreach(info)

		val points = List(Point(1, 1, 1), Point(2, 2, 2), Point(3, 3, 3))
		info(points.maxBy(point => point.v))
		info(points.foldLeft(0)((sum, point) => sum + point.v))
		info(points.scanLeft(0)((sum, point) => sum + point.v))
	}

}

