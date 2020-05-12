package com.hxy.recipe.spark

import com.hxy.recipe.common.Log
import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object SparkMLRunner extends Log {

	def main(args: Array[String]): Unit = {
		val sparkConf = new SparkConf().setAppName("hxy-spark-app").setMaster("local[2]")
		val spark = SparkSession.builder().config(sparkConf).getOrCreate()

		val training = spark.createDataFrame(Seq(
			(1.0, Vectors.dense(0.0, 1.1, 0.1)),
			(0.0, Vectors.dense(2.0, 1.0, -1.0)),
			(0.0, Vectors.dense(2.0, 1.3, 1.0)),
			(1.0, Vectors.dense(0.0, 1.2, -0.5))
		)).toDF("label", "features")

		info("training: " + training)

		val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.01)

		val binomialModel = lr.fit(training)
		info("Binomial model's numClasses: " + binomialModel.numClasses)

		info("Binomial model's numFeatures: " + binomialModel.numFeatures)

		info("Binomial model's coefficients: " + binomialModel.coefficients)

		info("Binomial model's intercept: " + binomialModel.intercept)

		info("Binomial model's hasSummary: " + binomialModel.hasSummary)

		val summary = binomialModel.summary

		log.info(df2string(summary.predictions))

		info("Binomial summary's objectiveHistory: " + summary.objectiveHistory.mkString(","))

		info("Binomial summary's totalIterations: " + summary.totalIterations)

		val binomialSummary = summary.asInstanceOf[BinaryLogisticRegressionSummary]

		info(binomialSummary.precisionByThreshold)

		info(binomialSummary.recallByThreshold)

		info(binomialSummary.fMeasureByThreshold)

		info(binomialSummary.pr)

		info(binomialSummary.roc)

		info("Binomial summary's areaUnderROC: " + binomialSummary.areaUnderROC)

		val test = spark.createDataFrame(Seq(
			(1.0, Vectors.dense(-1.0, 1.5, 1.3)),
			(0.0, Vectors.dense(3.0, 2.0, -0.1)),
			(1.0, Vectors.dense(0.0, 2.2, -1.5))
		)).toDF("label", "features")

		info(test)

		info(binomialModel.evaluate(test).predictions)

		val data = Array(
			Vectors.dense(-1.0, 1.5, 1.3),
			Vectors.dense(3.0, 2.0, -0.1),
			Vectors.dense(0.0, 2.2, -1.5)
		)
		val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")
		info(binomialModel.transform(df))

		val multinomialTraining = spark.createDataFrame(Seq(
			(3.0, Vectors.dense(0.0, 1.1, 0.1)),
			(0.0, Vectors.dense(2.0, 1.0, -1.0)),
			(0.0, Vectors.dense(2.0, 1.3, 1.0)),
			(1.0, Vectors.dense(0.0, 1.2, -0.5))
		)).toDF("label", "features")

		info(multinomialTraining)

		val multinomialModel = lr.fit(multinomialTraining)
		info("Multinomial model's numClasses: " + multinomialModel.numClasses)

		info("Multinomial model's numFeatures: " + multinomialModel.numFeatures)

		info("Multinomial model's coefficients: " + multinomialModel.coefficientMatrix)

		info("Multinomial model's intercept: " + multinomialModel.interceptVector)

		info("Multinomial model's hasSummary: " + multinomialModel.hasSummary)

		info(multinomialModel.transform(df))
	}

}
