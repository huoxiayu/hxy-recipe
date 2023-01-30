package com.hxy.recipe.spark.ml

import java.util.concurrent.ThreadLocalRandom

import com.hxy.recipe.spark.ml.SparkMLRunner.info
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionTrainingSummary}
import org.apache.spark.sql.{DataFrame, SparkSession}

class SparkLinearRegression(implicit spark: SparkSession) {

	import spark.implicits._

	def linearRegression(): Unit = {
		// 2x + 3y + 3
		def generate(x: Double, y: Double): Double = {
			2 * x + 3 * y + 3 + 2 * ThreadLocalRandom.current().nextGaussian()
		}

		val data: Seq[(Double, Double, Double)] = 1.to(10000).map(i => {
			val x = ThreadLocalRandom.current().nextInt(i).toDouble
			val y = ThreadLocalRandom.current().nextInt(i).toDouble
			(x, y, generate(x, y))
		})

		val dataDF: DataFrame = data.toDF("x", "y", "label")
		dataDF.show(10)

		val vectorDf = new VectorAssembler()
			.setInputCols(Array("x", "y"))
			.setOutputCol("features")
			.transform(dataDF)
		vectorDf.show(10)

		val model = new LinearRegression()
			.setMaxIter(100000)
			.setRegParam(0.1)
			.setElasticNetParam(0.1)
			.fit(vectorDf)
		info(s"coefficients: ${model.coefficients}")
		info(s"intercept: ${model.intercept}")

		val trainingSummary: LinearRegressionTrainingSummary = model.summary
		info(s"numIterations: ${trainingSummary.totalIterations}")
		info(s"objectiveHistory: ${trainingSummary.objectiveHistory.mkString(",")}")

		trainingSummary.residuals.show(10)
		info(s"rmse: ${trainingSummary.rootMeanSquaredError}")
		info(s"r2: ${trainingSummary.r2}")

		trainingSummary.predictions.show(10)
	}


}
