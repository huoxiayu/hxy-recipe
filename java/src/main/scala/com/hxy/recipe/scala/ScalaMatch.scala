package com.hxy.recipe.scala

object ScalaMatch {

	def main(args: Array[String]): Unit = {
		val str = "str"
		println(str match {
			case "str" => println("here")
		})

		println(str match {
			case "some" => println("yeah")
		})

		println(str match {
			case "some" => println("yeah")
			case _ => "none match"
		})
	}

}
