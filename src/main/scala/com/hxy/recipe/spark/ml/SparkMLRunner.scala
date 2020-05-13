package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkMLRunner extends Log {

	val sparkConf: SparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
	implicit val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

	def main(args: Array[String]): Unit = {
		log.info("spark ml runner start")

		new SparkKMeans().kmeans()

		log.info("spark ml runner end")
	}

}
