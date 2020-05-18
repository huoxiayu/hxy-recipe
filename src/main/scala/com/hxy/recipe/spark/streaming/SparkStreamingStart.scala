package com.hxy.recipe.spark.streaming

import com.hxy.recipe.common.Log
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingStart extends Log {

	val sparkConf: SparkConf = new SparkConf().setAppName("hxy-spark-streaming-app").setMaster("local[2]")
	implicit val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

	def main(args: Array[String]): Unit = {
		kafka()
	}

	def kafka()(implicit spark: SparkSession): Unit = {
		val streamingContext = new StreamingContext(spark.sparkContext, Seconds(5))
		val kafkaParams = Map[String, Object](
			"bootstrap.servers" -> "localhost:9092",
			"key.deserializer" -> classOf[StringDeserializer],
			"value.deserializer" -> classOf[StringDeserializer],
			"group.id" -> "hxy-spark-streaming",
			"auto.offset.reset" -> "latest",
			"enable.auto.commit" -> Boolean.box(true)
		)
		val topics = Array("test")
		val stream = KafkaUtils.createDirectStream[String, String](
			streamingContext,
			PreferConsistent,
			Subscribe[String, String](topics, kafkaParams)
		)

		stream.foreachRDD(rdd => {
			rdd.foreach(record => info(s"consume: ${(record.key(), record.value())}..."))
		})

		streamingContext.start()

		val timeoutInMillis = 10000L
		streamingContext.awaitTerminationOrTimeout(timeoutInMillis)
	}

	/**
	  * nc -lk 8888
	  * 1 2 3
	  * 1 1 1 2 2 3 3 3 3 3 3
	  */
	def nc()(implicit spark: SparkSession): Unit = {
		val streamingContext = new StreamingContext(spark.sparkContext, Seconds(5))
		val lines = streamingContext.socketTextStream("localhost", 8888)
		val words = lines.flatMap(_.split(" "))
		val pairs = words.map(word => (word, 1))
		val wordCounts = pairs.reduceByKey(_ + _)
		wordCounts.print(10)
		streamingContext.start()
		streamingContext.awaitTermination()
	}

}
