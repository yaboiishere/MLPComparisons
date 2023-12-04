package iris.mlp

import org.apache.spark.sql.catalyst.optimizer.MergeScalarSubqueries.Header
import org.apache.spark.sql.{DataFrame, SparkSession}
object IrisReader {
  def readData(sparkSession: SparkSession): Array[DataFrame] = {
    sparkSession.read.option("header", value = true).option("inferSchema", value = true).csv("Iris.csv").randomSplit(Array(0.67, 0.33))
  }
}