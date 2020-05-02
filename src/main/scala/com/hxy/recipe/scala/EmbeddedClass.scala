package com.hxy.recipe.scala

import scala.collection.mutable.ArrayBuffer

object EmbeddedClass {

  def main(args: Array[String]): Unit = {
    val chatter1 = new Network
    val hxy = chatter1.join("hxy")
    val lqw = chatter1.join("lqw")

    val chatter2 = new Network
    val someone = chatter2.join("someone")

    hxy.contacts += lqw

    hxy.contacts += someone
  }

}

class Network {

  class Member(val name: String) {
    val contacts = new ArrayBuffer[Network#Member]
  }

  private val members = new ArrayBuffer[Member]

  def join(name: String): Member = {
    val m = new Member(name)
    members += m
    m
  }
}