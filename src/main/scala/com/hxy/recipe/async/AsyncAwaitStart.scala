package com.hxy.recipe.async

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

/**
 * depends on macro
 */
object AsyncAwaitStart {

  def main(args: Array[String]): Unit = {
    val future = async {
      val f1: Future[Boolean] = async {
        true
      }
      val f2 = async {
        42
      }
      if (await(f1)) await(f2) else 0
    }

    val x: Int = Await.result(future, 2 seconds)
    println(x)
  }

}
