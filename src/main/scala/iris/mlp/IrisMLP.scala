package iris.mlp

import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.classification.{MultilayerPerceptronClassifier, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession

object IrisMLP {

  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().master("local[1]").appName("RandomForestClassifierExample").getOrCreate()
    val startTime = System.currentTimeMillis()

    val irisData = IrisReader.readData(session)
    val trainData = irisData(0)
    val testData = irisData(1)

    val layers = Array[Int](4, 5, 4, 3)

    val assembler = new VectorAssembler().setInputCols(Array("SepalLengthCm", "SepalWidthCm", "PetalLengthCm", "PetalWidthCm")).setOutputCol("features")
    val indexer = new StringIndexer().setInputCol("Species").setOutputCol("label")
    val trainer = new MultilayerPerceptronClassifier().setLayers(layers).setBlockSize(128).setSeed(1234L).setMaxIter(100)

    val pipeline = new Pipeline().setStages(Array[PipelineStage](assembler, indexer, trainer))
    val model = pipeline.fit(trainData)

    val result = model.transform(testData)
    val predictionAndLabels = result.select("prediction", "label")
    val evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy")

    val endTime = System.currentTimeMillis()
    println(s"Test set accuracy = ${evaluator.evaluate(predictionAndLabels)}")
    println(s"Time taken = ${endTime - startTime} ms")
  }
}