package org.ranceworks.postclustering

import java.io.File
import java.util

import collection.mutable.Stack
import org.scalatest._
import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.ranceworks.postclustering.token.{EnglishTokenizer, JapaneseTokenizer}

import collection.JavaConverters._
import org.ranceworks.postclustering.vo.Doc

class AppTest extends FlatSpec with Matchers {

  "EnglishTokenizer" should "tokenize english sentence" in {

    /*
   println( new JapaneseTokenizer().tokenize("犬も歩けば棒に当たる"))

    val sc  = new SparkContext(new SparkConf() setAppName "postclustering" setMaster "local")

    val a: util.Collection[File] =
      FileUtils.listFiles(new File(getClass.getResource("C50/C50test").toURI) ,Array("txt"),true)

    val builder = new DocBuilder(sc)

    val matrix =  builder.buildMatrix(a.asScala.toList, new EnglishTokenizer())
    val mp: Map[String, Int] = CKMeans.run(matrix, 50, 20)

    mp.foreach { m =>
      println(m._1 + "->"  + m._2)
    }
    val docs: RDD[Doc] = builder.toRdd(a.asScala.toList, new EnglishTokenizer)

    val pData: RDD[(String, Vector)] = builder.createDocMatrix(docs)
    val parsedData: RDD[Vector] = pData.map(p=> p._2)

    val clusters = KMeans.train(parsedData, 50, 20)

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
    */
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
