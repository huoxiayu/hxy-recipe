package com.hxy.recipe.common

import org.slf4j
import org.slf4j.LoggerFactory

trait Log {

	val log: slf4j.Logger = LoggerFactory.getLogger(this.getClass)

	def info(msg: Any): Unit = log.info("" + msg)

}
