package com.hxy.recipe.spark

import breeze.linalg._

object Breeze {

	def main(args: Array[String]): Unit = {
		// matrix
		println(DenseMatrix((1.0, 2.0), (3.0, 4.0)))
		println

		println(DenseMatrix.zeros[Double](2, 3))
		println

		println(DenseMatrix.eye[Double](5))
		println

		// vector
		println(DenseVector.zeros[Double](3))
		println

		println(DenseVector.ones[Double](3))
		println

		println(DenseVector.fill(3) {
			5.0
		})
		println

		println(DenseVector.range(1, 10, 3))
		println

		println(diag(DenseVector(1.0, 2.0, 3.0)))
		println
	}

}
