package com.hxy.recipe.spark.ml

import org.apache.spark.ml.attribute.{Attribute, AttributeGroup, NumericAttribute}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkSession}

class SparkFeature(implicit spark: SparkSession) {

	import spark.implicits._

	/**
	  * ~分隔特征和label
	  * +合并 +0删除截距
	  * -删除 -0删除截距
	  * :乘
	  * 例如：
	  * 1、 y ~ a + b => y ~ w0 + w1 * a + w2 * b, w0为截距，w1和w2为相关系数
	  * 2、 y ~ a + b + a:b – 1 => y ~ w1 * a + w2 * b + w3 * a * b，其中w1，w2，w3是相关系数
	  */
	def rFormula(): Unit = {
		val df = Seq(
			(1, "US", 18, 1.0),
			(2, "US", 12, 1.0),
			(3, "CA", 18, 1.0),
			(4, "CA", 12, 0.0)
		).toDF("id", "country", "hour", "clicked")
		df.show

		val formula = new RFormula()
			.setFormula("clicked ~ country + hour") // 基于country和hour来预测clicked，字符串类型特征会数值化
			.setFeaturesCol("features")
			.setLabelCol("label")

		formula.fit(df).transform(df).show()
	}

	// 输入特征向量，输出原始特征向量子集
	def vectorSlicer(): Unit = {
		val rdd: RDD[Row] = spark.sparkContext.parallelize(Seq(Row(Vectors.dense(-2.0, 2.3, 0.0))))

		val numericAttribute = NumericAttribute.defaultAttr
		val attrs: Array[NumericAttribute] = Array("f1", "f2", "f3").map(numericAttribute.withName)
		val attrGroup = new AttributeGroup("userFeatures", attrs.asInstanceOf[Array[Attribute]])
		val df = spark.createDataFrame(rdd, StructType(Array(attrGroup.toStructField())))
		df.show

		val slicer = new VectorSlicer()
			.setInputCol("userFeatures")
			.setOutputCol("features")
			.setIndices(Array(1)) // 按index选择
			.setNames(Array("f3")) // 按name选择

		slicer.transform(df).show
	}

	// 用于将连续型特征转换为类别特征
	def quantileDiscretizer(): Unit = {
		val df = Seq(
			(0, 18.0),
			(1, 19.0),
			(2, 8.0),
			(3, 5.0),
			(4, 2.2)
		).toDF("id", "hour")
		df.show

		val discretizer = new QuantileDiscretizer()
			.setInputCol("hour")
			.setOutputCol("result")
			.setNumBuckets(3)

		val result = discretizer.fit(df).transform(df)
		result.show()
	}

	// 可以将若干列合并为一个向量并且铺平
	def vectorAssembler(): Unit = {
		val df = Seq(
			(0, 18, 100, Vectors.dense(0.0, 10.0, 0.5), 1.0)
		).toDF("id", "hour", "pace", "userFeatures", "clicked")
		df.show(false)

		new VectorAssembler()
			.setInputCols(Array("hour", "pace", "userFeatures"))
			.setOutputCol("features")
			.transform(df)
			.select("features", "clicked")
			.show(false)
	}

	def sqlTransformer(): Unit = {
		val df = Seq(
			(0, 1.0, 3.0),
			(1, 2.0, 4.0)
		).toDF("id", "v1", "v2")
		df.show

		// __THIS__用于指定输入的数据表
		val sqlTransformer = new SQLTransformer()
			.setStatement("SELECT *, (v1 + v2) AS v3, (v1 * v2) AS v4 FROM __THIS__")
		sqlTransformer.transform(df).show()
	}

	// 可用于改变特征中各维度的权重
	def elementWiseProduct(): Unit = {
		val df = Seq(
			("a", Vectors.dense(1.0, 2.0, 3.0)),
			("b", Vectors.dense(4.0, 5.0, 6.0))
		).toDF("id", "vector")
		df.show

		val transformingVector = Vectors.dense(0.0, 1.0, 2.0)
		val transformer = new ElementwiseProduct()
			.setScalingVec(transformingVector)
			.setInputCol("vector")
			.setOutputCol("transformedVector")
		transformer.transform(df).show()
	}

	def bucketizer(): Unit = {
		val data = Seq(-100, -0.5, -0.3, 0.0, 0.2, 10)
		val df = data.map(Tuple1.apply).toDF("features")
		df.show

		val bucketizer = new Bucketizer()
			.setInputCol("features")
			.setOutputCol("bucketedFeatures")
			.setSplits(Array(Double.NegativeInfinity, -0.5, 0.0, 0.5, Double.PositiveInfinity))

		bucketizer.transform(df).show()
	}

	def maxAbsScaler(): Unit = {
		val data = Seq(
			Vectors.dense(1, 1),
			Vectors.dense(2, 2),
			Vectors.dense(3, 3),
			Vectors.dense(1, 2)
		).map(Tuple1.apply).toDF("features")

		val scaler = new MaxAbsScaler()
			.setInputCol("features")
			.setOutputCol("scaledFeatures")
			.fit(data)

		val scaledData = scaler.transform(data)
		scaledData.show(false)
	}

	def minMaxScaler(): Unit = {
		val data = Seq(
			Vectors.dense(1, 1),
			Vectors.dense(2, 2),
			Vectors.dense(3, 3),
			Vectors.dense(1, 2)
		).map(Tuple1.apply).toDF("features")

		val scaler = new MinMaxScaler()
			.setInputCol("features")
			.setOutputCol("scaledFeatures")
			.fit(data)

		val scaledData = scaler.transform(data)
		scaledData.show(false)
	}

	def standardScaler(): Unit = {
		val data = Seq(
			Vectors.dense(1, 1),
			Vectors.dense(2, 2),
			Vectors.dense(3, 3),
			Vectors.dense(1, 2)
		).map(Tuple1.apply).toDF("features")

		val scalerModel = new StandardScaler()
			.setInputCol("features")
			.setOutputCol("scaledFeatures")
			.setWithStd(true)
			.setWithMean(false)
			.fit(data)

		val scaledData = scalerModel.transform(data)
		scaledData.show(false)
	}

	def normalizer(): Unit = {
		val data = Seq(
			Vectors.dense(1, 1),
			Vectors.dense(2, 2),
			Vectors.dense(3, 3),
			Vectors.dense(1, 2)
		).map(Tuple1.apply).toDF("features")

		val normalizer = new Normalizer()
			.setInputCol("features")
			.setOutputCol("normFeatures")
			.setP(1.0)

		val l1NormData = normalizer.transform(data)
		l1NormData.show(false)

		val l2NormData = normalizer.transform(data, normalizer.p -> 2)
		l2NormData.show(false)

		val lInfNormData = normalizer.transform(data, normalizer.p -> Double.PositiveInfinity)
		lInfNormData.show(false)
	}

	def polynomialExpansion(): Unit = {
		val data = Seq(
			Vectors.dense(-2.0, 2.3),
			Vectors.dense(0.0, 0.0),
			Vectors.dense(0.6, -1.1)
		).map(Tuple1.apply).toDF("features")

		data.show(false)

		new PolynomialExpansion()
			.setInputCol("features")
			.setOutputCol("polyFeatures")
			.setDegree(2) // (x, y) => (x, y, x * x, x * y, y * y)
			.transform(data).show(false)
	}

	def pca(): Unit = {
		val data = Seq(
			Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),
			Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
			Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
		).map(Tuple1.apply).toDF("features")
		data.show(false)

		val pca = new PCA()
			.setInputCol("features")
			.setOutputCol("pcaFeatures")
			.setK(3)
			.fit(data)
		pca.transform(data).show(false)
	}

	def binarizer(): Unit = {
		val data = Seq(0.1, 0.3, 0.5, 0.7, 0.9).toDF("value")
		data.show

		new Binarizer()
			.setInputCol("value")
			.setOutputCol("binary")
			.setThreshold(0.5)
			.transform(data)
			.show
	}

	def ngram(): Unit = {
		val labeledWords = Seq(
			(0, Array("Hi", "I", "heard", "about", "Spark")),
			(1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
			(2, Array("Logistic", "regression", "models", "are", "neat"))
		).toDF("label", "words")
		labeledWords.show(false)

		val ngram = new NGram().setN(2).setInputCol("words").setOutputCol("ngram")
		ngram.transform(labeledWords).show(false)
	}

	def removeStopWords(): Unit = {
		val words = Seq(
			"I heard about Spark and I love Spark",
			"I wish Java could use case classes",
			"Logistic regression models are neat"
		).map(_.split(" ")).toDF("sentence")
		words.show(false)

		val stopWordsRemover = new StopWordsRemover().setInputCol("sentence").setOutputCol("filtered")
		stopWordsRemover.transform(words).show(false)
	}

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

	def chiSqSelector(): Unit = {
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
