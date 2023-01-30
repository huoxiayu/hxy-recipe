package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors

object SparkVector extends Log {

	def vector(): Unit = {
		info("vector start")

		val sparseVector = Vectors.sparse(3, Array(0, 2), Array(1, 3))
		info(sparseVector)

		info(sparseVector.argmax)

		info(sparseVector.compressed)

		val denseVector = Vectors.dense(1, 1, 1)
		info(denseVector)

		info(LabeledPoint(1, sparseVector))
		info(LabeledPoint(2, denseVector))

		// 范数
		info("norm1: " + Vectors.norm(denseVector, 1))
		info("norm2: " + Vectors.norm(denseVector, 2))
		info("norm3: " + Vectors.norm(denseVector, 3))
		info("norm100: " + Vectors.norm(denseVector, 100))

		info(Vectors.sqdist(
			Vectors.dense(1, 2, 3),
			Vectors.dense(2, 4, 6)
		))

		info("vector end")
	}

	def main(args: Array[String]): Unit = {
		vector()
	}

}

