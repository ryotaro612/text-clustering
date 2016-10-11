package org.ranceworks.postclustering

import java.io.File
import java.util

import collection.mutable.Stack
import org.scalatest._
import org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.junit.Test
import org.ranceworks.postclustering.token.BasicTokenizer

import collection.JavaConverters._

class AppTest extends FlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
     //   println(new File(getClass.getResource("C50").toURI))
    val files: List[File]
      = FileUtils.listFiles(new File(getClass.getResource("C50").toURI), Array("txt"),true).asScala.toList

    val tokens: RDD[List[String]] =  App.toRdd(files.take(2), new BasicTokenizer)

    tokens.take(2).foreach(f => println(f))
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
