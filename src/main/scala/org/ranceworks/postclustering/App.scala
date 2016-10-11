package org.ranceworks.postclustering

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util

import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.rdd.RDD

import scala.language.postfixOps
import collection.JavaConverters._
object App {


  val sc  = new SparkContext(new SparkConf() setAppName "postclustering" setMaster "local")

  def toRdd(files : List[File]) : List[RDD[String]] = {

    val b:  RDD[File] = sc.parallelize(files)
    val c: RDD[String] = b.map(f => FileUtils.readFileToString(f, StandardCharsets.UTF_8))
    c.take(1).foreach(f=> println(f))
    files map (f => sc.textFile(f.getPath))

  }
  def main(args: Array[String]) {
    val a: util.Collection[File] =   FileUtils.listFiles(new File(getClass.getResource("C50").toURI) ,Array("txt"),true)
  }


  def execute() = {
     // The appName parameter is a name for your application to show on the cluster UI.
    val sc  = new SparkContext(new SparkConf() setAppName "postclustering" setMaster "local")
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
