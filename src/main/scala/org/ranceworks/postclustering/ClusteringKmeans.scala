package org.ranceworks.postclustering

import java.io.File
import java.nio.charset.StandardCharsets
import java.util

import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.ranceworks.postclustering.token.Tokenizer
import org.ranceworks.postclustering.vo.{Doc, DocVec}

import scala.language.postfixOps

object App {
  val sc  = new SparkContext(new SparkConf() setAppName "postclustering" setMaster "local")

  def toRdd(files : List[File], tokenizer: Tokenizer) : RDD[Map[String, Int]] = {
    val fileRDD: RDD[String]
    = sc.parallelize(files) map (file => FileUtils readFileToString(file, StandardCharsets.UTF_8))

    val docMap: RDD[Map[String, Int]] = fileRDD.map(tokenizer.tokenize) map { words =>
      words./:(Map[String, Int]())((map: Map[String, Int], word: String) => {
        map contains word match {
          case true => map + (word -> (map(word) + 1))
          case false => map + (word -> 1)
        }})
    }
    docMap
  }
  def createDocMatrix(docMap: RDD[Map[String, Int]]): RDD[Vector] = {
    val allWords: List[String]
    = docMap.map(map => map.keySet).reduce((wordSetA, wordSetB) => wordSetA ++ wordSetB).toList
    docMap.map(map =>
      Vectors.dense(allWords.map(word => map.applyOrElse(word, (word: String) => 0).toDouble).toArray)
    )
  }

  def main(args: Array[String]) {
    val a: util.Collection[File] = FileUtils.listFiles(new File(getClass.getResource("C50").toURI) ,Array("txt"),true)
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

object ClusteringKMeans {

  def run(docMatrix: RDD[DocVec], numClusters: Int, numIteration: Int): Map[String, Int]  = {

    val clusters: KMeansModel =  KMeans.train(docMatrix.map(v=> v.wordVec), numClusters, numIteration)
    val c: RDD[Map[String, Int]] = docMatrix.map{ vec => Map(vec.id -> clusters.predict(vec.wordVec))}
    c.reduce((a, b) => a ++ b)

  }
}

class DocBuilder(sc: SparkContext) {

   private def toRdd(files : List[File], tokenizer: Tokenizer): RDD[Doc] = {
    val fileRDD: RDD[(String, String)]
    = sc.parallelize(files) map (file =>
      (file.getAbsolutePath, FileUtils readFileToString(file, StandardCharsets.UTF_8)))
    fileRDD.map((a: (String, String)) => (a._1, tokenizer.tokenize(a._2))).map {(tuple: (String, List[String])) =>
      val mp = tuple._2./:(Map[String, Int]())((map: Map[String, Int], word: String) => {
        map contains word match {
          case true => map + (word -> (map(word) + 1))
          case false => map + (word -> 1)
        }})
      Doc(tuple._1, mp)
    }
  }

  def buildMatrix(files : List[File], tokenizer: Tokenizer): RDD[DocVec] = {
    val docs:  RDD[Doc]  = toRdd(files, tokenizer)
    val allWords: List[String] = docs.map(doc => doc.wordOccurMap.keySet).reduce((k1,k2) => k1 ++ k2).toList
    docs.map(doc =>
      DocVec(doc.id,
        Vectors.dense(allWords.map(w => doc.wordOccurMap.applyOrElse(w, (word: String)=> 0).toDouble).toArray)))
  }

  private def createDocMatrix(docs: RDD[Doc]): RDD[DocVec] = {
    val allWords: List[String] = docs.map(doc => doc.wordOccurMap.keySet).reduce((k1,k2) => k1 ++ k2).toList
    docs.map(doc =>
      DocVec(doc.id,
        Vectors.dense(allWords.map(w => doc.wordOccurMap.applyOrElse(w, (word: String)=> 0).toDouble).toArray)))
  }
}
