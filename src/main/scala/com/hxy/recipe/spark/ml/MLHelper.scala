package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.ml.linalg.{DenseVector => MLDenseVector}
import org.apache.spark.mllib.linalg.{Vector => MLLIBVector}
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object MLHelper extends Log {

	/**
	  * data format
	  * 1. sepal length in cm
	  * 2. sepal width in cm
	  * 3. petal length in cm
	  * 4. petal width in cm
	  * 5. class:
	  * Iris Setosa
	  * Iris Versicolour
	  * Iris Virginica
	  */
	def iris()(implicit spark: SparkSession): RDD[Array[String]] = {
		spark.sparkContext.textFile("./target/classes/iris.data")
			.filter(line => line.trim.nonEmpty)
			.map(_.split(","))
	}

	def colStats(rdd: RDD[MLDenseVector]): Unit = {
		printStats(rdd.map(org.apache.spark.mllib.linalg.DenseVector.fromML).map(_.asInstanceOf[MLLIBVector]))
	}

	private def printStats(vectors: RDD[MLLIBVector]): Unit = {
		val statistics = Statistics.colStats(vectors)
		info(s"statistics.max: ${statistics.max}")
		info(s"statistics.min: ${statistics.min}")
		info(s"statistics.mean: ${statistics.mean}")
		info(s"statistics.variance: ${statistics.variance}")
		info(s"statistics.normL1: ${statistics.normL1}")
		info(s"statistics.normL2: ${statistics.normL2}")
	}

}
