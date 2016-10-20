package org.ranceworks.postclustering

import java.io.{File, StringReader}
import java.util

import collection.mutable.Stack
import org.scalatest._
import org.apache.commons.io.FileUtils
import org.apache.lucene.analysis.{TokenFilter, TokenStream}
import org.apache.lucene.analysis.core.StopFilterFactory
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.{CharTermAttribute, CharTermAttributeImpl, OffsetAttribute, OffsetAttributeImpl}
import org.apache.spark.rdd.RDD
import org.junit.Test
import org.ranceworks.postclustering.token.{BasicTokenizer, EnglishTokenizer}
import org.tartarus.snowball.ext.PorterStemmer

import collection.JavaConverters._

class AppTest extends FlatSpec with Matchers {

  "EnglishTokenizer" should "tokenize english sentence" in {
    val tokens = new EnglishTokenizer().tokenize("Fortune favors the bold")
    assert(tokens == List("fortun", "favor", "bold"))
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
