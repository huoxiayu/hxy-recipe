package com.hxy.recipe.spark.ml

import com.hxy.recipe.common.Log
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.sql.SparkSession

class SparkKMeans(implicit spark: SparkSession) extends Log {

	import spark.implicits._

	def kmeans(): Unit = {
		val data = MLHelper.iris().map(line => line.take(4).map(_.toDouble)).toDF("features")
		data.show()

		val kmeansModel = new KMeans()
			.setK(3)
			.setFeaturesCol("features")
			.setPredictionCol("prediction")
			.fit(data)
		kmeansModel.clusterCenters.foreach(center => info(s"center $center"))

		val predictions = kmeansModel.transform(data)
		predictions.show(20, truncate = false)

		val evaluate = new ClusteringEvaluator()
			.setFeaturesCol("features")
			.setPredictionCol("prediction")
			.evaluate(predictions)
		info(s"evaluate: $evaluate")
	}

}
