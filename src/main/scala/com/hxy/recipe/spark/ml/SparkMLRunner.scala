package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkMLRunner extends Log {

	val sparkConf: SparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
	implicit val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

	def main(args: Array[String]): Unit = {
		// https://blog.csdn.net/liulingyuan6/article/details/53410832 => bucketizer
		log.info("spark ml runner start")

	 	new SparkFeature().standardScaler()

		log.info("spark ml runner end")
	}

}
