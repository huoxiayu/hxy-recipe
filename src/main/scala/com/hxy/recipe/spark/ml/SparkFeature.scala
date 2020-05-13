package com.hxy.recipe.spark.ml

import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

class SparkFeature(implicit spark: SparkSession) {

	import spark.implicits._

	// 特征提取
	def tfIdf(): Unit = {
		val sentences = Seq(
			(0, "I heard about Spark and I love Spark"),
			(0, "I wish Java could use case classes"),
			(1, "Logistic regression models are neat")
		).toDF("label", "sentence")

		val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
		val wordsData = tokenizer.transform(sentences)
		wordsData.show

		val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(2000)
		val rawFeatures = hashingTF.transform(wordsData)
		rawFeatures.select("rawFeatures").show(false)

		val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
		val idfModel = idf.fit(rawFeatures)
		val rescaledData = idfModel.transform(rawFeatures)
		rescaledData.select("features", "label").show(false)
	}

	// 特征提取
	def wordVec(): Unit = {
		val document = Seq(
			"Hi I heard about Spark".split(" "),
			"I wish Java could use case classes".split(" "),
			"Logistic regression models are neat".split(" ")
		).map(Tuple1.apply).toDF("text")
		document.show(false)

		val word2Vec = new Word2Vec()
			.setInputCol("text")
			.setOutputCol("result")
			.setVectorSize(3)
			.setMinCount(0)

		val model = word2Vec.fit(document)
		val result = model.transform(document)
		result.show(false)
	}

	// 特征提取
	def countVectorizer(): Unit = {
		val df = Seq(
			(0, Array("a", "b", "c", "d")),
			(1, Array("a", "b", "b", "c", "a")),
			(2, Array("d", "e"))
		).toDF("id", "words")

		val model = new CountVectorizer()
			.setInputCol("words")
			.setOutputCol("features")
			.setVocabSize(10)
			.setMinDF(2)
			.fit(df)

		println(model.vocabulary.mkString(","))

		model.transform(df).show(false)

		val cvm = new CountVectorizerModel(Array("a", "b", "c"))
			.setInputCol("words")
			.setOutputCol("features")

		cvm.transform(df).show(false)
	}

	// 特征变换
	def featureTransform(): Unit = {
		val data = Seq(
			(0, "a"),
			(1, "b"),
			(2, "c"),
			(3, "a"),
			(4, "a"),
			(5, "c")
		).toDF("id", "category")

		data.show

		val stringIndex = new StringIndexer().setInputCol("category").setOutputCol("categoryIdx")
		val idxData = stringIndex.fit(data).transform(data)

		idxData.show

		val indexString = new IndexToString().setInputCol("categoryIdx").setOutputCol("originalCategory")
		val originalData = indexString.transform(idxData)
		originalData.show

		val oneHotEncoder = new OneHotEncoderEstimator()
			.setInputCols(Array("categoryIdx"))
			.setOutputCols(Array("categoryVec"))

		val oneHotEncoderData = oneHotEncoder.fit(idxData).transform(idxData)
		oneHotEncoderData.show

		val vectorData = Seq(
			Vectors.dense(-1.0, 1.0, 1.0),
			Vectors.dense(-1.0, 3.0, 1.0),
			Vectors.dense(0.0, 5.0, 1.0)
		).map(Tuple1.apply).toDF("features")
		vectorData.show

		val indexer = new VectorIndexer().
			setInputCol("features").
			setOutputCol("vectorIdx").
			setMaxCategories(2)

		val indexerModel = indexer.fit(vectorData)

		val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet

		println(s"choose ${categoricalFeatures.size} categorical features: " + categoricalFeatures.mkString(", "))

		val indexedData = indexerModel.transform(vectorData)
		indexedData.show
	}

	// 特征选取
	def featureSelect(): Unit = {
		val df = Seq(
			(1, Vectors.dense(0.0, 0.0, 18.0, 1.0), 1),
			(2, Vectors.dense(0.0, 1.0, 12.0, 0.0), 0),
			(3, Vectors.dense(1.0, 0.0, 15.0, 0.1), 0)
		).toDF("id", "features", "label")

		df.show()

		val selector = new ChiSqSelector()
			.setNumTopFeatures(1) // 选择和标签关联性最强的topN个特征
			.setFeaturesCol("features")
			.setLabelCol("label")
			.setOutputCol("selected-feature")

		val selector_model = selector.fit(df)
		val result = selector_model.transform(df)

		result.show(false)
	}

}
