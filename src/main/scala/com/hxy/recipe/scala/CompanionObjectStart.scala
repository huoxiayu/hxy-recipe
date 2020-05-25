package com.hxy.recipe.scala

object CompanionObjectStart {

	def main(args: Array[String]): Unit = {
		val name = new Name("hxy")
		Name.visitPrivateField(name)
		Name.visitPrivateMethod(name)
	}

}

class Name(
			  private val name: String
		  ) {

	private def privatePrint(): Unit = {
		println(name)
	}

}

object Name {

	def visitPrivateField(name: Name): Unit = {
		println(name.name)
	}

	def visitPrivateMethod(name: Name): Unit = {
		name.privatePrint()
	}

}