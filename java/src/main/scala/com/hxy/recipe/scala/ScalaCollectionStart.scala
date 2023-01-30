package com.hxy.recipe.scala

import com.hxy.recipe.common.Log

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

object ScalaCollectionStart extends Log {

	private[ScalaCollectionStart] case class Point(x: Int, y: Int, v: Int)

	def main(args: Array[String]): Unit = {
		zip()
		// list()
		// buffer()
		// set()
		// map()
		// collectionOp()
		par()
		view()
	}

	def zip(): Unit = {
		val array1 = Array(1, 2, 3)
		val array2 = Array("a", "b")
		for (
			(l, r) <- array1 zip array2
		) println(l + "->" + r)
	}

	def par(): Unit = {
		val range = 1 to 10
		range.foreach(info)

		newLine()

		range.par.foreach(info)
	}

	def view(): Unit = {
		val list = List(1, 2, 3)
		list.filter(i => {
			info(s"filter $i")
			true
		}).map(i => {
			info(s"map $i")
			i + 1
		})

		newLine()

		list.view.filter(i => {
			info(s"filter $i")
			true
		}).map(i => {
			info(s"map $i")
			i + 1
		}).force
	}

	def list(): Unit = {
		val list123 = 1 :: 2 :: 3 :: Nil
		info(s"list123: $list123")
		info(s"head: ${list123.head}")
		info(s"tail: ${list123.tail}")
		info(s"take: ${list123.take(2)}")
		info(s"drop: ${list123.drop(2)}")
		info(s"splitAt: ${list123.splitAt(2)}")
		info(s"apply: ${list123(0)}")
		info(s"indices: ${list123.indices}")

		val List(one, two, three) = list123
		info(s"one: $one, two: $two, three: $three")

		val first :: second :: others = list123
		info(s"first: $first, second: $second, others: $others")

		val list45 = List(4, 5)
		info(s":: => ${list123 :: list45}")
		info(s"::: => ${list123 ::: list45}")

		val list123list34 = List(list123, list45)
		info(s"list123list34.flatten: ${list123list34.flatten}")

		def append(xs: List[Int], ys: List[Int]): List[Int] = xs match {
			case Nil => ys
			case x :: xsr => x :: append(xsr, ys)
		}

		info(s"append: ${append(list123, list45)}")

		def reverse(xs: List[Int]): List[Int] = xs match {
			case Nil => Nil
			case head :: tail => reverse(tail) ::: List(head)
		}

		info(s"reverse: ${reverse(List(1, 2, 3))}")

		def insertSort(xs: List[Int]): List[Int] = xs match {
			case Nil => Nil
			case head :: tail => insertByOrder(head, insertSort(tail))
		}

		def insertByOrder(x: Int, xs: List[Int]): List[Int] = xs match {
			case Nil => List(x)
			case y :: ys => if (x < y) x :: xs else y :: insertByOrder(x, ys)
		}

		def mergeSort(xs: List[Int]): List[Int] = {
			xs.splitAt(xs.length / 2) match {
				case (left, Nil) => left
				case (Nil, right) => right
				case (left, right) => mergeByOrder(mergeSort(left), mergeSort(right))
			}
		}

		def mergeByOrder(xs: List[Int], ys: List[Int]): List[Int] = (xs, ys) match {
			case (_, Nil) => xs
			case (Nil, _) => ys
			case (x :: xsr, y :: ysr) => if (x < y) x :: mergeByOrder(xsr, ys) else y :: mergeByOrder(xs, ysr)
		}

		val oriList = Random.shuffle(1.to(10).toList)
		info(s"oriList: $oriList")
		info(s"insert sort: ${insertSort(oriList)}")
		info(s"merge sort: ${mergeSort(oriList)}")
	}

	def buffer(): Unit = {
		val lstBuf = new ListBuffer[Int]
		lstBuf += 1
		lstBuf += 2
		lstBuf += 3
		info(s"lstBuf: $lstBuf")
		info(s"0 +: lstBuf: ${0 +: lstBuf}")
		info(s"lstBuf :+ 4: ${lstBuf :+ 4}")
		info(s"lstBuf ++ lstBuf: ${lstBuf ++ lstBuf}")

		val arrayBuf = new ArrayBuffer[Int]
		arrayBuf.append(1)
		info(s"arrayBuf: $arrayBuf")
		info(s"arrayBuf ++ arrayBuf: ${arrayBuf ++ arrayBuf}")
	}

	def set(): Unit = {
		val set123 = Set(1, 2, 3)
		info(s"set123: $set123")
		val set234 = Set(2, 3, 4)
		info(s"set234: $set234")
		info(s"set123 + 4: ${set123 + 4}")
		info(s"set123 - 3: ${set123 - 3}")
		info(s"set123 ++ set234: ${set123 ++ set234}")
		info(s"set123 -- set234: ${set123 -- set234}")
		info(s"set123 & set234: ${set123 & set234}")
		info(s"set123 | set234: ${set123 | set234}")
	}

	def map(): Unit = {
		val map = Map(1 -> 1, 2 -> 2)
		info(s"map: $map")
		info(s"map + (3 -> 3): ${map + (3 -> 3)}")
		info(s"map - 2: ${map - 2}")
	}

	def collectionOp(): Unit = {
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

