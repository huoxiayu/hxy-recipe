package com.hxy.recipe.spark.streaming

import com.hxy.recipe.common.Log
import com.hxy.recipe.kafka.KafkaConstants
import com.hxy.recipe.kafka.model.{Event, JsonDeserializer}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.streams.StreamsConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingStart extends Log {

	private val expireInMillis = Seconds(60).milliseconds
	private val outputDir = "/huoxiayu/output/"
	private val checkPointDir = "/huoxiayu/checkpoint/"
	private val sparkConf: SparkConf = new SparkConf().setAppName("hxy-spark-streaming-app").setMaster("local[2]")
	implicit private val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

	Runtime.getRuntime.addShutdownHook(new Thread("spark-close-thread") {
		override def run(): Unit = {
			log.info("spark session close")
			spark.close()
		}
	})

	def main(args: Array[String]): Unit = {
		kafka()
	}

	def kafka()(implicit spark: SparkSession): Unit = {
		// windowDuration和slideDuration必须是batchDuration的整数倍
		val batchDuration = Seconds(5)
		val windowDuration = Seconds(15)
		val slideDuration = Seconds(5)
		val streamingContext = new StreamingContext(spark.sparkContext, batchDuration)
		streamingContext.checkpoint(checkPointDir)

		val kafkaParams = Map[String, Object](
			ConsumerConfig.GROUP_ID_CONFIG -> "hxy-spark-streaming",
			StreamsConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
			ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName,
			ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[JsonDeserializer].getName,
			ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
			ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false"
		)
		val topics = Array(KafkaConstants.TOPIC)
		val stream: InputDStream[ConsumerRecord[String, Event]] = KafkaUtils.createDirectStream[String, Event](
			streamingContext,
			PreferConsistent,
			Subscribe[String, Event](topics, kafkaParams)
		)

		val sourceEventTimes: DStream[(String, Int)] = stream.map(record => record.value())
			.filter(event => System.currentTimeMillis() - event.timestamp < expireInMillis)
			.map(event => (event.source + ":" + event.event, event.times))
			.reduceByKeyAndWindow((times1: Int, times2: Int) => times1 + times2, windowDuration, slideDuration)
		sourceEventTimes
			.repartition(1)
			.saveAsTextFiles(outputDir + "source-event-agg-")
		sourceEventTimes.print()

		val sourceTimes = sourceEventTimes.map(eventSourceTimes => {
			(eventSourceTimes._1.split(":")(0), eventSourceTimes._2)
		}).reduceByKey(_ + _)
		sourceTimes
			.repartition(1)
			.saveAsTextFiles(outputDir + "source-agg-")
		sourceTimes.print()

		stream.foreachRDD(rdd => {
			val offsetList = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
			log.info("try commit offsetList: {}", offsetList)
			stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetList)
		})

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
