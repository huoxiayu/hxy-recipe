package com.hxy.recipe.spark

import com.google.gson.Gson
import com.hxy.recipe.common.Log
import com.hxy.recipe.util.Utils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.collection.JavaConverters._


object SparkSqlRunner extends Log {

	val personList: Seq[Person] = Seq(
		Person(16, "hxy-like-three-games", Seq("lol", "dnf", "king-glory").asJava, Hobby("blue", "iron-man")),
		Person(16, "hxy-only-play-dnf", Seq("dnf").asJava, Hobby("red")),
		Person(18, "old-hxy-do-not-like-game", Seq().asJava, Hobby())
	)

	val heroList: Seq[Hero] = Seq(
		Hero("jinx", Some("lulu"), Some(100)),
		Hero("LeeSin", None, None)
	)

	val gson = new Gson()

	val personJsonList: Seq[String] = personList.map(gson.toJson(_))
	personJsonList.foreach(log.info)

	def main(args: Array[String]): Unit = {
		val sparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
		val spark = SparkSession.builder().config(sparkConf).getOrCreate()
		import spark.implicits._

		// default column name: _1 _2
		val sexGameTimeDataFrame = Seq(
			("boy", "dnf", 1),
			("boy", "dnf", 2),
			("boy", "dnf", 3),
			("girl", "dnf", 1),
			("boy", "king-glory", 1),
			("boy", "king-glory", 2),
			("boy", "king-glory", 2),
			("girl", "king-glory", 1),
			("girl", "king-glory", 2),
			("girl", "king-glory", 3)
		).toDF

		info("sexGameTimeDataFrame")
		sexGameTimeDataFrame.show

		info("sexGameTimeDataFrame groupBy")
		sexGameTimeDataFrame.groupBy($"_1", $"_2")
			.agg(collect_list($"_3").as("times"))
			.select(concat_ws(":", $"_1", $"_2").as("sex:game"), $"times")
			.show

		val df = spark.read.json(spark.createDataset(spark.sparkContext.parallelize(personJsonList)))
		info("person data frame show")
		df.show

		info("person filter name contains play data frame show")
		df.filter(array_contains(split($"name", "-"), "play")).show

		Utils.sleep()

		val explodedDf = df.select($"age", $"name" as "player", explode($"registerGameList").alias("game"))
		explodedDf.printSchema
		explodedDf.show

		val heroDF = spark.sparkContext.parallelize(heroList).toDF()
		heroDF.filter($"pet" =!= "dzx").show // filter None atomically
		heroDF.filter($"mp" >= 50).show

		spark.close()
	}

}

case class Hobby(
					favoriteColor: String = "",
					favoriteMovie: String = ""
				)

case class Person(
					 age: Int,
					 name: String,
					 registerGameList: java.util.List[String],
					 hobby: Hobby
				 )

case class Hero(
				   name: String,
				   pet: Option[String],
				   mp: Option[Int]
			   )
