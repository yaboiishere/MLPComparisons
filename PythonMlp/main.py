import findspark
from pyspark.ml.feature import VectorAssembler, StringIndexer
from pyspark.sql import SparkSession
from pyspark.ml.classification import MultilayerPerceptronClassifier
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.ml import Pipeline
import time


def main():
    findspark.init()
    spark = SparkSession.builder.appName("mlp").master("local").getOrCreate()

    startTime = time.time()

    maxIter = 100
    layers = [4, 5, 4, 3]
    blockSize = 128
    seed = 1234

    dataframe = spark.read.csv("../Iris.csv", header=True, inferSchema=True)
    splits = dataframe.randomSplit([1.8, 0.2], seed)
    train = splits[0]
    test = splits[1]

    vectorAssembler = VectorAssembler(inputCols=["SepalLengthCm", "SepalWidthCm", "PetalLengthCm", "PetalWidthCm"],
                                      outputCol="features")
    indexer = StringIndexer(inputCol="Species", outputCol="label")

    trainer = MultilayerPerceptronClassifier(maxIter=maxIter, layers=layers, blockSize=blockSize, seed=seed)

    pipeline = Pipeline(stages=[vectorAssembler, indexer, trainer])

    model = pipeline.fit(train)

    result = model.transform(test)
    predictionAndLabels = result.select("prediction", "label")
    evaluator = MulticlassClassificationEvaluator(metricName="accuracy")

    endTime = time.time()
    print("Test set accuracy = " + str(evaluator.evaluate(predictionAndLabels)))
    print("Time taken = " + str(endTime - startTime) + " ms")


if __name__ == '__main__':
    main()
