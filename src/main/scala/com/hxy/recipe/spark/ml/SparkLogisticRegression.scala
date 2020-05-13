package com.hxy.recipe.spark.ml

import com.hxy.recipe.spark.ml.SparkMLRunner.info
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.ml.linalg.{DenseVector, Vectors}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.SparkSession

class SparkLogisticRegression(implicit spark: SparkSession) {

	import spark.implicits._

	def logisticRegression(): Unit = {
		val data = MLHelper.iris()
		data.toDF().show(false)

		val df = data.map(array => Vectors.dense(Array(array(2).toDouble, array(3).toDouble)) -> array(4))
			.toDF("features", "label")
		df.show

		MLHelper.colStats(df.rdd.map(i => i.getAs[DenseVector](0)))

		df.createOrReplaceTempView("iris")
		spark.sql("select * from iris where label != 'Iris-setosa'")
			.map(t => s"${t(1)}:${t(0)}")
			.show(false)

		// label index
		val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(df)
		info(s"labelIndexer.labels: ${labelIndexer.labels.mkString(",")}")

		// feature index
		val featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").fit(df)
		info(s"featureIndexer.categoryMaps: ${featureIndexer.categoryMaps}")

		// split training and test
		val Array(training, test) = df.randomSplit(Array(0.8, 0.3))

		// prepare lr model
		val logisticRegression = new LogisticRegression()
			.setLabelCol("indexedLabel")
			.setFeaturesCol("indexedFeatures")
			.setMaxIter(100)
			.setRegParam(0.3)
			.setElasticNetParam(0.5)
			.setFamily("multinomial")
		info(s"logisticRegression.explainParams(): ${logisticRegression.explainParams()}")

		// convert label index to label
		val invLabelIndexer = new IndexToString()
			.setInputCol("prediction")
			.setOutputCol("predictionLabel")
			.setLabels(labelIndexer.labels)

		val pipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, logisticRegression, invLabelIndexer))

		// training
		val pipelineModel = pipeline.fit(training)

		// print params
		val lrModel = pipelineModel.stages(2).asInstanceOf[LogisticRegressionModel]
		info(s"Coefficients: ${lrModel.coefficientMatrix}")
		info(s"Intercept: ${lrModel.interceptVector}")
		info(s"numClasses: ${lrModel.numClasses}")
		info(s"numFeatures: ${lrModel.numFeatures}")

		// save model
		val modelPath = "./target/classes/iris_model"
		pipelineModel.write.overwrite().save(modelPath)

		// load model
		val readPipelineModel = PipelineModel.load(modelPath)

		// test
		val predictions = readPipelineModel.transform(test)
		predictions.show(false)

		predictions.select($"indexedLabel", $"prediction").show(false)

		// model evaluate
		val evaluator = new MulticlassClassificationEvaluator()
			.setLabelCol("indexedLabel")
			.setPredictionCol("prediction")
		val accuracy = evaluator.evaluate(predictions)
		info(s"accuracy: $accuracy")
	}

}
