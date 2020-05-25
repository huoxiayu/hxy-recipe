package com.hxy.recipe.scala

object CaseClassStart {

	case class Person(
						 age: Int,
						 name: String
					 )

	def main(args: Array[String]): Unit = {
		val person = Person(16, "hxy")
		println(person.productElement(0))
		println(person.productElement(1))

		val unapply = Person.unapply(person)
		println(unapply.get)

		println(person.isInstanceOf[Serializable])
	}

}
