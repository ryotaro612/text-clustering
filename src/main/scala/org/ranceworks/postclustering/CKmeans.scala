package org.ranceworks.postclustering

import java.io.File
import java.nio.charset.StandardCharsets

import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.ranceworks.postclustering.token.Tokenizer
import org.ranceworks.postclustering.vo.{Doc, DocVec}

import scala.language.postfixOps

object CKMeans {

  def run(docMatrix: RDD[DocVec], numClusters: Int, numIteration: Int): Map[String, Int]  = {

    val clusters: KMeansModel =  KMeans.train(docMatrix.map(v=> v.wordVec), numClusters, numIteration)
    val c: RDD[Map[String, Int]] = docMatrix.map{ vec => Map(vec.id -> clusters.predict(vec.wordVec))}
    c.reduce((a, b) => a ++ b)

  }
}

class DocBuilder(sc: SparkContext) {

  def buildMatrix(files : List[File], tokenizer: Tokenizer): RDD[DocVec] = {
    val docs:  RDD[Doc]  = toRdd(files, tokenizer)
    docToMatrix(docs)
  }
  def buildMatrix(texts: Map[String, String], tokenizer: Tokenizer): RDD[DocVec] = {
    val docs:  RDD[Doc]  = toRdd(texts, tokenizer)
    docToMatrix(docs)
  }

  private def docToMatrix(docs: RDD[Doc]): RDD[DocVec] = {
    val allWords: List[String] = docs.map(doc => doc.wordOccurMap.keySet).reduce((k1,k2) => k1 ++ k2).toList
    docs.map(doc =>
      DocVec(doc.id,
        Vectors.dense(allWords.map(w => doc.wordOccurMap.applyOrElse(w, (word: String)=> 0).toDouble).toArray)))
  }
  def toRdd(texts : Map[String, String], tokenizer: Tokenizer): RDD[Doc] = {
    sc.parallelize(texts.toSeq).map(idText => Doc(idText._1, countWords(tokenizer.tokenize(idText._2))))
  }

  private def countWords(words: List[String]): Map[String, Int]  = {
    words./:(Map[String, Int]())((mp, word) => mp contains word match {
      case true => mp + (word -> (mp(word) + 1))
      case false => mp + (word -> 1)
    })
  }

  private def toRdd(files : List[File], tokenizer: Tokenizer): RDD[Doc] = {
    val fileRDD: RDD[(String, String)]
    = sc.parallelize(files) map (file =>
      (file.getAbsolutePath, FileUtils readFileToString(file, StandardCharsets.UTF_8)))
    fileRDD.map((tuple: (String, String)) => Doc(tuple._1, countWords(tokenizer.tokenize(tuple._2))))
  }
}
