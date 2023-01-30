package com.hxy.recipe.scala.puzzle

import scala.collection.{Iterable, mutable}

object ScalaPuzzle {

  def main(args: Array[String]): Unit = {
    // func()

    // collection()

    // parameterizedType()

    closureSimple()

    closure()
  }

  def func(): Unit = {
    val list = List(1, 2, 3)

    println("init map1")
    val map1: Int => Int = {
      println("map1")
      _ + 1
    }

    println("call map1")
    list.map(map1)

    println("init map2")
    val map2: Int => Int = i => {
      println("map2")
      i + 1
    }

    println("call map2")
    list.map(map2)
  }

  def collection(): Unit = {
    def sumSize(iterOfIter: Iterable[Iterable[_]]): Int = {
      val map = iterOfIter.map(_.size)
      println(map)

      val sum = map.sum
      println(sum)
      sum
    }

    sumSize(List(Set(1, 2), List(3, 4)))
    sumSize(Set(List(1, 2), Set(3, 4)))
  }

  def parameterizedType(): Unit = {
    def applyNMulti[T](n: Int)(arg: T, f: T => T): T = {
      (1 to n).foldLeft(arg) { (ace, _) => f(ace) }
    }

    def applyNCurried[T](n: Int)(arg: T)(f: T => T): T = {
      (1 to n).foldLeft(arg) { (ace, _) => f(ace) }
    }

    def nextInt(n: Int): Int = n * n + 1

    def nextNumber[N](n: N)(implicit numericOps: Numeric[N]): N = {
      numericOps.plus(numericOps.times(n, n), numericOps.one)
    }

    println(applyNMulti(3)(2, nextInt))

    println(applyNCurried(3)(2)(nextInt))

    println(applyNMulti(3)(2.0, nextNumber[Double]))

    println(applyNCurried(3)(2.0)(nextNumber))
  }

  def closureSimple(): Unit = {
    // scala -print X.scala
    def fun(): () => Int = {
      val i = 0
      var j = 1
      () => i + j
    }

    fun()
  }

  def closure(): Unit = {
    val accessors1 = mutable.Buffer.empty[() => Int]
    val accessors2 = mutable.Buffer.empty[() => Int]

    val data = Seq(100, 110, 120)

    /**
     * @see scala.runtime.IntRef
     */
    var j = 0
    for (i <- data.indices) {
      accessors1 += (() => data(i))
      accessors2 += (() => data(j))

      j += 1
    }

    accessors1.foreach(a1 => println(a1()))
    newLine()

    try {
      accessors2.foreach(a2 => println(a2()))
    } catch {
      case e: Throwable => println("error:" + e)
    } finally {
      newLine()
    }
  }

  def newLine(): Unit = {
    println("<----------------->")
  }

}

/**
 * 1、scala-class:
 *
 * object ScalaStart {
 *
 * val map1: Int => Int = {
 * println("map1")
 * _ + 1
 * }
 *
 * def fun(): () => Int = {
 * val i = 1
 * var j = 2
 * () => i + j
 * }
 *
 * }
 *
 * 2、scala -print ScalaStart.scala
 *
 * 3、output
 * package <empty> {
 * object ScalaStart extends Object {
 * private[this] val map1: Function1 = _;
 * <stable> <accessor> def map1(): Function1 = ScalaStart.this.map1;
 * def fun(): Function0 = {
 * val i: Int = 1;
 * var j: runtime.IntRef = scala.runtime.IntRef.create(2);
 * {
 * (() => ScalaStart.this.$anonfun$fun$1(i, j))
 * }
 * };
 * final <artifact> private[this] def $anonfun$map1$1(x$1: Int): Int = x$1.+(1);
 * final <artifact> private[this] def $anonfun$fun$1(i$1: Int, j$1: runtime.IntRef): Int = i$1.+(j$1.elem);
 * def <init>(): ScalaStart.type = {
 * ScalaStart.super.<init>();
 * ScalaStart.this.map1 = {
 * scala.Predef.println("map1");
 * {
 * ((x$1: Int) => ScalaStart.this.$anonfun$map1$1(x$1))
 * }
 * };
 * ()
 * }
 * }
 * }
 */