package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkMLRunner extends Log {

	val sparkConf: SparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
	implicit val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

	def main(args: Array[String]): Unit = {
		// https://zhuanlan.zhihu.com/p/24320870 => 2.3 特征选择
		log.info("spark ml runner start")

		new SparkFeature().quantileDiscretizer()

		log.info("spark ml runner end")
	}

}
