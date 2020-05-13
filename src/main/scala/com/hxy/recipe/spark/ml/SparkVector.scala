package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors

class SparkVector extends Log {

	def vector(): Unit = {
		info("vector start")

		val sparseVector = Vectors.sparse(5, Array(1, 3), Array(1.0, 3.0))
		info(sparseVector)

		val denseVector = Vectors.dense(1.0, -1.0, 2.0)
		info(denseVector)

		info(LabeledPoint(1.0, sparseVector))
		info(LabeledPoint(2.0, denseVector))

		// 范数
		info(Vectors.norm(denseVector, 1))
		info(Vectors.norm(denseVector, 2))
		info(Vectors.norm(denseVector, 3))
		info(Vectors.norm(denseVector, 100))

		info(Vectors.sqdist(
			Vectors.dense(1.0, 2.0, 3.0),
			Vectors.dense(2.0, 4.0, 6.0)
		))

		info("vector end")
	}

}
