package com.hxy.recipe.spark

import com.google.gson.Gson
import com.hxy.recipe.util.Utils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.collection.JavaConverters._

object SparkSqlRunner {

	val personList = Seq(
		Person(16, "hxy-like-three-games", Seq("lol", "dnf", "king-glory").asJava, Hobby("blue", "iron-man")),
		Person(16, "hxy-only-play-dnf", Seq("dnf").asJava, Hobby("red")),
		Person(18, "old-hxy-do-not-like-game", Seq().asJava, Hobby())
	)

	val gson = new Gson()

	val jsonList = personList.map(gson.toJson(_))
	jsonList.foreach(println)

	def main(args: Array[String]): Unit = {
		val sparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
		val spark = SparkSession.builder().config(sparkConf).getOrCreate()
		import spark.implicits._

		val df = spark.read.json(spark.createDataset(spark.sparkContext.parallelize(jsonList)))
		df.show

		val explodedDf = df.select($"age", $"name" as "player", explode($"registerGameList").alias("game"))
		explodedDf.printSchema
		explodedDf.show

		Utils.sleepInMinutes(30L)

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