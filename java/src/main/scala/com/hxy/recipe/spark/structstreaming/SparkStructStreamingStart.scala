package com.hxy.recipe.spark.structstreaming

import java.util.concurrent.TimeUnit

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types._

object SparkStructStreamingStart {

	private val outputDir = "/huoxiayu/spark/struct/streaming/output/"
	private val checkPointDir = "/huoxiayu/spark/struct/streaming/checkpoint/"
	private val sparkConf: SparkConf = new SparkConf().setAppName("hxy-spark-struct-streaming-app").setMaster("local[2]")
	implicit private val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

	import spark.implicits._

	def main(args: Array[String]): Unit = {
		kafka()
	}

	def kafka(): Unit = {
		val kafkaStream = spark.readStream
			.format("kafka")
			.option("kafka.bootstrap.servers", "127.0.0.1:9092")
			.option("subscribe", "test")
			.option("startingOffsets", "earliest")
			.load()

		kafkaStream.printSchema()

		val schema = StructType(Seq(
			StructField("source", StringType, nullable = false),
			StructField("event", StringType, nullable = false),
			StructField("times", IntegerType, nullable = false),
			StructField("timestamp", LongType, nullable = false)
		))

		val selectQuery = kafkaStream
			.selectExpr("cast(key as string)", "cast(value as string)")
			.select(from_json($"value", schema).as("event"))
			.select(
				$"event.source".as("source"),
				$"event.event".as("event"),
				$"event.times".as("times"),
				($"event.timestamp" / 1000L).cast(TimestampType).as("timestamp")
			)

		selectQuery.printSchema()

		// group by not working. why?
		val groupByQuery = selectQuery
			.withWatermark("timestamp", "1 day")
			.groupBy(
				//window($"timestamp", "10 second", "5 second"),
				$"source"
			)
			.count()
			.select($"source", $"count")

		groupByQuery.printSchema()

		val streamQuery = groupByQuery
			.writeStream
			.format("console")
			.trigger(Trigger.ProcessingTime(1, TimeUnit.SECONDS))
			.outputMode(OutputMode.Complete())
			//.outputMode(OutputMode.Append())
			.option("truncate", "false")
			.option("path", outputDir)
			.option("checkpointLocation", checkPointDir)
			.start

		streamQuery.awaitTermination()
	}

}
