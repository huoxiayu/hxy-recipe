package com.hxy.recipe.scala

import java.util.concurrent.ThreadLocalRandom

object DslStart {

  def main(args: Array[String]): Unit = {
    // implement break by exception(slow)
    import scala.util.control.Breaks._
    breakable {
      while (true) {
        val rand = ThreadLocalRandom.current().nextInt(100)
        if (rand > 10) {
          println(rand)
        } else {
          break
        }
      }
    }
  }

}
