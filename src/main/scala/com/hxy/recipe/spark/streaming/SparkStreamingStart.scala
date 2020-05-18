package com.hxy.recipe.spark.streaming

import com.hxy.recipe.common.Log
import com.hxy.recipe.kafka.KafkaConstants
import com.hxy.recipe.kafka.model.{Event, JsonDeserializer}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.streams.StreamsConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
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
			ConsumerConfig.GROUP_ID_CONFIG -> "hxy-spark-streaming",
			StreamsConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
			ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName,
			ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[JsonDeserializer].getName,
			ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest"
		)
		val topics = Array(KafkaConstants.TOPIC)
		val stream: InputDStream[ConsumerRecord[String, Event]] = KafkaUtils.createDirectStream[String, Event](
			streamingContext,
			PreferConsistent,
			Subscribe[String, Event](topics, kafkaParams)
		)

		stream.map(record => record.value())
			.map(event => (event.getSource + ":" + event.getEvent, event.getTimes))
			.reduceByKey(_ + _)
			.print()

		streamingContext.start()
		streamingContext.awaitTermination()
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
