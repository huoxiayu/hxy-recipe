package com.hxy.recipe.scala

object RegexStart {

	def main(args: Array[String]): Unit = {
		val regex = """^(?!.*(_|-|:))(.*)$""".r
		val strings = Seq("Brain", "Brain-", "Brain:", "Brain_", "-a", "a_", "::")
		for (string <- strings) {
			regex.findFirstMatchIn(string) match {
				case Some(v) => println(v)
				case None => println("none match")
			}
		}
	}

}
