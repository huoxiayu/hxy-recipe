package com.hxy.recipe.common

import org.apache.spark.sql.DataFrame
import org.slf4j
import org.slf4j.LoggerFactory

trait Log {

	val log: slf4j.Logger = LoggerFactory.getLogger(this.getClass)

	def info(msg: String): Unit = log.info(msg)

	def info(msg: Any): Unit = log.info("" + msg)

	implicit def df2string(df: DataFrame): String = {
		val method = classOf[DataFrame].getDeclaredMethod("showString", classOf[Int], classOf[Int], classOf[Boolean])
		method.setAccessible(true)
		val result = method.invoke(
			df,
			100.asInstanceOf[Object],
			100.asInstanceOf[Object],
			false.asInstanceOf[Object]
		).asInstanceOf[String]
		"\n" + result
	}

}
