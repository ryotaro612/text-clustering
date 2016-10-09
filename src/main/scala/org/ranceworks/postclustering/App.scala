package org.ranceworks.postclustering

import java.io.File
import java.nio.file.Files

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.rdd.RDD

import scala.language.postfixOps

object App {
  def main(args: Array[String]) {

    val logFile = "YOUR_SPARK_HOME/README.md" // Should be some file on your system
    val conf = new SparkConf() setAppName "Simple Application" setMaster "local"
    val sc = new SparkContext(conf)

    // Load and parse the data
    val data: RDD[String]= sc.textFile(getClass getResource "kmeans_data.txt" getPath)
    val parsedData : RDD[Vector] = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    // Cluster the data into two classes using KMeans
    val numClusters = 2
    val numIterations = 20
    val clusters = KMeans.train(parsedData, numClusters, numIterations)

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)

    clusters.clusterCenters.foreach {
      center => println(f"${center.toArray.mkString("[", ", ", "]")}%s")
    }

    parsedData.foreach { tuple =>
      println( clusters.predict(tuple))
      //println(f"${tuple._2.toArray.mkString("[", ", ", "]")}%s " +
       // f"(${tuple._1}%s) : cluster = ${clusters.predict(tuple._2)}%d")
    }
    // Save and load model
    //clusters.save(sc, "target/org/apache/spark/KMeansExample/KMeansModel")
    //val sameModel = KMeansModel.load(sc, "target/org/apache/spark/KMeansExample/KMeansModel")
  }
}
