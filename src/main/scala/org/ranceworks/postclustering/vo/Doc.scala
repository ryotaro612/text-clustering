package org.ranceworks.postclustering.vo
import org.apache.spark.mllib.linalg.Vector

case class Doc(id: String, wordOccurMap: Map[String, Int])

case class DocVec(id: String, wordVec: Vector)
