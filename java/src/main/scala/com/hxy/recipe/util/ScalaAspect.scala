package com.hxy.recipe.util

import java.util.concurrent.TimeUnit

object ScalaAspect {

  def time(prompt: String)(block: => Unit): Long = {
    val start = System.currentTimeMillis()

    block

    val cost = System.currentTimeMillis() - start
    println(s"$prompt cost $cost millis")

    cost
  }

  def loop(repeatTimes: Int = 3)(block: => Unit): Unit = {
    1 to repeatTimes foreach (_ => block)
  }

  def sleepForever(): Unit = {
    TimeUnit.DAYS.sleep(1L)
  }

}
