package com.hxy.recipe.datastructure

import com.hxy.recipe.common.Log
import com.twitter.algebird.{HLL, HyperLogLog, HyperLogLogMonoid}
import org.apache.lucene.util.RamUsageEstimator

object TwitterHyperLogLog extends Log {

	private val hllMono = new HyperLogLogMonoid(11)

	def intArray2Hll(intArray: Seq[Int]): HLL = {
		intArray2Hll(intArray.toArray)
	}

	def intArray2Hll(intArray: Array[Int]): HLL = {
		intArray.par.map(i => hllMono.toHLL(i)).reduce(_ + _)
	}

	def byteArray2Hll(bytes: Array[Byte]): HLL = {
		HyperLogLog.fromBytes(bytes)
	}

	def string2Hll(str: String): HLL = hllMono.toHLL(str)

	def cardinality(hll: HLL): Long = hllMono.estimateSize(hll).toLong

	def andCardinality(hllList: Seq[HLL]): Long = hllMono.estimateIntersectionSize(hllList).toLong

	def orCardinality(hllList: Seq[HLL]): Long = hllMono.estimateSize(hllList.reduce(_ + _)).toLong

	def main(args: Array[String]): Unit = {
		val (dataset1, dataset2) = 0 to 10000000 partition (_ % 2 == 0)
		run(dataset1, dataset2, 5000000, 5000000, 0, 10000000)

		val (dataset3, dataset4) = 0 to 10000 partition (_ % 2 == 0)
		run(dataset3, dataset4, 5000, 5000, 0, 10000)

		run(0 to 500000, 250000 to 750000, 500000, 500000, 250000, 750000)
	}

	def run(dataset1: Seq[Int], dataset2: Seq[Int], hll1Size: Int, hll2Size: Int, andSize: Int, orSize: Int): Unit = {
		val hll1 = intArray2Hll(dataset1)
		val size1 = cardinality(hll1)
		log.info("hll1 {}", size1)
		log.info("hll1 human size {}", RamUsageEstimator.humanSizeOf(hll1))
		log.info("hll1 error rate {}", errorRate(size1, hll1Size))

		val hll2 = intArray2Hll(dataset2)
		val size2 = cardinality(hll2)
		log.info("hll2 {}", size2)
		log.info("hll2 human size {}", RamUsageEstimator.humanSizeOf(hll2))
		log.info("hll2 error rate {}", errorRate(size2, hll2Size))

		val sizeAnd = andCardinality(Seq(hll1, hll2))
		log.info("hll1 and hll2 {}", sizeAnd)
		log.info("and error rate {}", errorRate(sizeAnd, andSize))

		val sizeOr = orCardinality(Seq(hll1, hll2))
		log.info("hll1 or hll2 {}", sizeOr)
		log.info("or error rate {}", errorRate(sizeOr, orSize))
	}

	def errorRate(actual: Long, expect: Long): String = {
		if (expect <= 0L) {
			actual + "-gap"
		}
		else {
			if (actual == expect) {
				"0%"
			} else {
				Math.abs(actual - expect) * 100.0 / expect + "%"
			}
		}
	}
}
