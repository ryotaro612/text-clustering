package org.ranceworks.postclustering

import java.io.File
import java.nio.charset.StandardCharsets
import java.util

import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.ranceworks.postclustering.token.Tokenizer
import org.ranceworks.postclustering.vo.Doc

import scala.language.postfixOps



class DocBuilder(sc: SparkContext) {

  def toRdd(files : List[File], tokenizer: Tokenizer): RDD[Doc] = {
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

  def createDocMatrix(docs: RDD[Doc]): RDD[(String, Vector)] = {
    val allWords: List[String] = docs.map(doc => doc.wordOccurMap.keySet).reduce((k1,k2) => k1 ++ k2).toList
    docs.map(doc =>
      (doc.id, Vectors.dense(allWords.map(w => doc.wordOccurMap.applyOrElse(w, (word: String)=> 0).toDouble).toArray)))
  }
}

































