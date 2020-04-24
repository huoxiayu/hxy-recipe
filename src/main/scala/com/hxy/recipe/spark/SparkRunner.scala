package com.hxy.recipe.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object SparkRunner {

	val schema = new StructType().add("id", IntegerType, nullable = false)

	def toRow(i: Int) = Row(i)

	def toPair(i: Int): (Int, Int) = (i, i * 2)

	def main(args: Array[String]): Unit = {
		val sparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
		val spark = SparkSession.builder().config(sparkConf).getOrCreate()
		val sparkContext = spark.sparkContext

		import spark.implicits._

		val rdd_1_to_10 = sparkContext.parallelize(1 to 10, 1)
		val rdd_1_to_5 = sparkContext.parallelize(1 to 5, 1)
		val rdd_pair_1_to_10 = rdd_1_to_10.map(toPair)
		val rdd_pair_1_to_5 = rdd_1_to_5.map(toPair)
		val df_1_to_10 = rdd_1_to_10.toDF("id")
		val df_1_to_5 = rdd_1_to_5.toDF("id")
		val df1 = Seq((1, "a", "x"), (1, "b", "y")).toDF("triggerId", "column_1", "extra")
		val df2 = Seq((1, "c", "x"), (1, "d", "z")).toDF("triggerId", "column_2", "extra")

		println("left outer")
		df1.join(df2, Seq("extra"), "leftouter").show

		println("left anti")
		df1.join(df2, Seq("extra"), "leftanti").show

		println("left semi")
		df1.join(df2, Seq("extra"), "leftsemi").show

		// rdd_1_to_10.map(_ * 2).foreach(println)
		// rdd_1_to_10.mapPartitions(it => it.map(_ + 10)).foreach(println)
		// rdd_1_to_10.sample(withReplacement = false, 0.5).foreach(println)
		// rdd_1_to_10.intersection(sparkContext.parallelize(1 to 5)).foreach(println)
		// rdd_1_to_10.map(i => (i % 2 == 0, i)).groupByKey().foreach(i => println(i._1 + ":" + i._2))
		// rdd_1_to_10.map(i => (i % 2 == 0, i)).reduceByKey(_ + _).foreach(println)
		// rdd_1_to_10.map(i => (i % 2 == 0, i)).aggregateByKey(0)((k, v) => k + v, _ + _).foreach(println)
		// rdd_1_to_10.map(i => (i, i % 2 == 0)).sortBy(_._1, ascending = false).foreach(println)
		// rdd_1_to_5.cartesian(rdd_1_to_10).foreach(println)
		// val longAcc = sparkContext.longAccumulator("long-acc")
		// sparkContext.parallelize(1 to 10000, 20).foreach(longAcc.add(_))
		// println(s"longAcc.sum: ${longAcc.sum}, longAcc.value: ${longAcc.value}")
		// rdd_pair_1_to_10.cogroup(rdd_pair_1_to_5).foreach(println)
		// df_1_to_5.join(df_1_to_10, Seq("id"), "inner").show
		// df_1_to_5.join(df_1_to_10, Seq("id"), "right_outer").show

		/**
		  * spark中用蒙特卡洛概率求解pi的值
		  * 在边长为a的正方形中随机投点，落在此正方形内切圆中的概率为内切圆面积与正方形面积的比值
		  * 即：pi * (a / 2) * (a / 2) / (a * 2) = pi / 4
		  */
		// val times = 1000000
		// val pi = sparkContext.parallelize(1 to times, 20)
		// 	.map(_ => {
		// 		val x = ThreadLocalRandom.current().nextDouble() * 2 - 1
		// 		val y = ThreadLocalRandom.current().nextDouble() * 2 - 1
		// 		if (x * x + y * y <= 1) 1 else 0
		// 	}).reduce(_ + _) * 4.0D / times
		// println(s"pi is: $pi")
		// val df1_join_df2 = df1
		// 	.join(df2, Seq("triggerId"), "inner")
		// 	.select($"triggerId", $"column_1", $"column_2").cache
		// df1_join_df2.show
		// df1_join_df2.dropDuplicates(Seq("triggerId", "column_1")).show

		val id_app_usage = Seq(
			(1, Map("app1" -> 10, "app2" -> 5)),
			(2, Map("app1" -> 5, "app3" -> 1))
		).toDF("id", "appUsage")
		id_app_usage.show
		id_app_usage.select($"id", explode($"appUsage")).show // key value
		id_app_usage.select($"id", explode($"appUsage") as Seq("app", "useTime")).show // app useTime

		spark.close()
	}

}
