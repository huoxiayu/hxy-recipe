package com.hxy.recipe.spark.ml

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.SparkSession

class SparkPipeline(implicit spark: SparkSession) {

	import spark.implicits._

	def pipeline(): Unit = {
		// label: contains word `spark`
		val training = Seq(
			(0, "a b c d spark", 1.0),
			(1, "b d", 0.0),
			(2, "spark f g h", 1.0),
			(3, "map reduce", 0.0),
			(4, "hadoop scala", 0.0)
		).toDF("id", "text", "label")
		training.show

		val tokenizer = new Tokenizer().setInputCol("text").setOutputCol("word")
		tokenizer.transform(training).show

		val hashingTF = new HashingTF().setNumFeatures(1000).setInputCol(tokenizer.getOutputCol).setOutputCol("features")
		hashingTF.transform(tokenizer.transform(training)).show

		val logisticRegression = new LogisticRegression().
			setMaxIter(10).
			setRegParam(0.01)

		val pipeline = new Pipeline().setStages(Array(tokenizer, hashingTF, logisticRegression))

		val pipelineModel = pipeline.fit(training)

		val test = spark.createDataFrame(Seq(
			(4, "spark i j k"),
			(5, "l m n"),
			(6, "spark a"),
			(7, "apache hadoop")
		)).toDF("id", "text")

		pipelineModel
			.transform(test)
			.select("id", "text", "probability", "prediction")
			.show
	}


}
