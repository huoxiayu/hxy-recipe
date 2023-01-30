package com.hxy.recipe.spark

import breeze.linalg._
import breeze.math.Complex
import breeze.plot.Figure
import breeze.plot._

object Breeze {

	def main(args: Array[String]): Unit = {
		visual()
	}

	def complex(): Unit = {
		val i = Complex.i

		val complex1_2 = 1 + 2 * i
		println(complex1_2)

		val complex2_3 = 2 + 3 * i
		println(complex2_3)

		println(complex1_2 + complex2_3)

		println(complex1_2 - complex2_3)

		println(complex1_2 * complex2_3)

		println(complex1_2 / complex2_3)
	}

	def vector(): Unit = {
		val sparseVector = SparseVector[Double](3)()
		pln(sparseVector)

		sparseVector(0) = 1
		sparseVector(1) = 2
		sparseVector(2) = 3
		pln(sparseVector)

		pln(normalize(sparseVector))

		// 转置
		pln(sparseVector.t)

		pln(sparseVector.t * sparseVector)

		pln(sparseVector + sparseVector)

		pln(sparseVector.mapActivePairs((_, v) => v + 1))

		val denseVector = DenseVector(2, 0, 3, 2, -1)
		pln(denseVector)

		pln(denseVector + denseVector)

		denseVector.update(0, 1)
		pln(denseVector)

		pln(DenseVector.zeros[Double](3))

		pln(DenseVector.ones[Double](3))

		pln(DenseVector.fill(3) {
			5.0
		})

		pln(DenseVector.range(1, 10, 3))

		pln(diag(DenseVector(1.0, 2.0, 3.0)))
	}

	def matrix(): Unit = {
		pln(DenseMatrix((1.0, 2.0), (3.0, 4.0)))

		pln(DenseMatrix.zeros[Double](2, 3))

		pln(DenseMatrix.eye[Double](5))
	}

	def visual(): Unit = {
		val f = Figure()
		val p = f.subplot(0)
		val x = DenseVector(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8)
		val y = DenseVector(1.1, 2.1, 0.5, 1.0, 3.0, 1.1, 0.0, 0.5, 2.5)
		p += plot(x, y)
		p.xlabel = "x axis"
		p.ylabel = "y axis"
		f.saveas("lines-graph.spark.png")
	}

	def pln(x: Any): Unit = {
		println(x)
		println
	}

}
