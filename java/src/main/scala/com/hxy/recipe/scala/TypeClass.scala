package com.hxy.recipe.scala

// todo: see scala shapeless
object TypeClass {

  trait HasId[A] {
    def getId(a: A): Int
  }

  final case class User(id: Int, name: String)

  implicit val userHasId: HasId[User] = (user: User) => user.id

  def printId[A](a: A)(implicit hia: HasId[A]): Unit = println(
    s"Found id ${hia.getId(a)}"
  )

  trait HasLabel[A] {
    def getLabel(a: A): String
  }

  implicit def hasLabelFromId[A](implicit hia: HasId[A]): HasLabel[A] = (a: A) => s"ID:${hia.getId(a)}"

  def print[A](a: A)(implicit hla: HasLabel[A]): Unit = println(s"Label: ${hla.getLabel(a)}")

  def main(args: Array[String]): Unit = {
    val user = User(1, "Foo")
    printId(user)
    print(user)
  }

}
