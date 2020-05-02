package com.hxy.recipe.scala

object PackageStart {

  def main(args: Array[String]): Unit = {
    import org.hxy.game._
    println(lol)

    import name.hxy.collection.game._
    println(games)

    import a.b.c.d._
    println(e)
  }

}

package org {
  package hxy {

    object game {
      val lol = "LOL"
    }

  }

}

package name {
  package hxy {
    package collection {

      import scala.collection.mutable.ArrayBuffer

      object game {
        val games: ArrayBuffer[String] = ArrayBuffer("dota", "king-glory")
      }

    }

  }

}

package a.b.c {

  object d {
    val e = "e"
  }

}