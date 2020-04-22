package com.hxy.recipe.clazz

object GenericInScala extends App {

	class Animal

	class Bird extends Animal

	class Consumer[+T](t: T) {
		println(s"consume $t")
	}

	class Produce[-T](t: T) {
		println(s"produce $t")
	}

	val birdC: Consumer[Bird] = new Consumer[Bird](new Bird)
	val animalC: Consumer[Animal] = birdC

	val animalP: Produce[Animal] = new Produce[Animal](new Animal)
	val birdP: Produce[Bird] = animalP

}
