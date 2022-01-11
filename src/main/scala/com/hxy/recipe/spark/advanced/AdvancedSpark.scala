package com.hxy.recipe.spark.advanced

import com.hxy.recipe.util.ScalaAspect._
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import scala.language.postfixOps

object AdvancedSpark {

  val PARQUET_PREFIX = "tmp_parquet/"
  val JSON_PREFIX = "tmp_json/"

  val USER = "user"
  val GOODS = "goods"
  val PV = "pv"
  val PURCHASE = "purchase"

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("advanced-spark")
      .setMaster("local[1]")

    implicit val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    generateData

    select

    sleepForever()
  }

  def select(implicit spark: SparkSession): Unit = {
    import spark.implicits._

    loop(2) {
      val readParquet = spark.read.parquet(PARQUET_PREFIX + PV)

      val parquetGroupBy = time("parquet-process") {
        val df1 = readParquet
          .groupBy($"userId")
          .agg(count($"timestamp").as("cnt"))
        df1.explain(true)

        val df1Cnt = df1.count()
        println(s"df1Cnt: $df1Cnt")

        val reduce1 = df1.collect().map(row => row.getLong(1)).sum
        println(s"reduce1: $reduce1")
      }

      val parquetGet = time("parquet-get") {
        1 to 10 foreach {
          userId => {
            readParquet.filter($"userId" === userId)
              .select($"timestamp")
              .count()
          }
        }
      }

      val readJson = spark.read.json(JSON_PREFIX + PV)
      val jsonGroupBy = time("json-process") {
        val df2 = readJson
          .groupBy($"userId")
          .agg(count($"timestamp").as("cnt"))
        df2.explain(true)

        val df2Cnt = df2.count()
        println(s"df2Cnt: $df2Cnt")

        val reduce2 = df2.collect().map(row => row.getLong(1)).sum
        println(s"reduce2: $reduce2")
      }

      val jsonGet = time("json-get") {
        1 to 10 foreach {
          userId => {
            readJson.filter($"userId" === userId)
              .select($"timestamp")
              .count()
          }
        }
      }

      println(s"statistic: $parquetGroupBy, $parquetGet, $jsonGroupBy, $jsonGet")
    }
  }

  def generateData(implicit spark: SparkSession): Unit = {
    import spark.implicits._

    // user
    val userSize = 10000
    val userDataFrame: DataFrame = spark.sparkContext
      .parallelize(1 to userSize map (userId => User(userId, "user_" + userId)))
      .toDF()
    println(s"user.cnt -> ${userDataFrame.count()}")

    // generate goods
    val random = ThreadLocalRandom.current()
    val priceMaxRange = 100D
    val goodsSize = 200
    val goodsDataFrame: DataFrame = spark.sparkContext
      .parallelize(1 to goodsSize map (goodsId => {
        Goods(goodsId, "goods_" + goodsId, random.nextDouble() * priceMaxRange)
      }))
      .toDF()
    println(s"goodsDataFrame.cnt -> ${goodsDataFrame.count()}")

    // generate pv
    val pvDataFrame = userDataFrame.crossJoin(goodsDataFrame)
      .drop($"userName")
      .drop($"goodsName")
      .map(row => Pv(
        row.getInt(0),
        row.getInt(1),
        row.getDouble(2),
        System.currentTimeMillis()
      )).toDF()

    println(s"pvDataFrame.cnt -> ${pvDataFrame.count()}")

    // generate purchase event
    val purchaseDataFrame = pvDataFrame.sample(withReplacement = false, 0.1D)
    println(s"purchaseDataFrame.cnt -> ${purchaseDataFrame.count()}")

    val dataFrameSeq = Seq(
      userDataFrame -> USER,
      goodsDataFrame -> GOODS,
      pvDataFrame -> PV,
      purchaseDataFrame -> PURCHASE
    )

    dataFrameSeq.foreach(p => {
      val (df, fileName) = p
      writeToParquet(df, fileName)
      writeToJson(df, fileName)
    })
  }

  def writeToParquet(dataFrame: DataFrame, fileName: String): Unit = {
    dataFrame
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .parquet(s"$PARQUET_PREFIX/$fileName")
    println(s"$fileName save as parquet end")
  }

  def writeToJson(dataFrame: DataFrame, fileName: String): Unit = {
    dataFrame
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .json(s"$JSON_PREFIX/$fileName")
    println(s"$fileName save as json end")
  }

}

case class Goods(goodsId: Int,
                 goodsName: String,
                 price: Double)

case class User(userId: Int,
                userName: String)

case class Pv(userId: Int,
              goodId: Int,
              price: Double,
              timestamp: Long,
              randomString: String = UUID.randomUUID().toString,
              randomInt: Int = ThreadLocalRandom.current().nextInt(),
              randomLong: Long = ThreadLocalRandom.current().nextLong(),
              randomBool: Boolean = ThreadLocalRandom.current().nextBoolean())