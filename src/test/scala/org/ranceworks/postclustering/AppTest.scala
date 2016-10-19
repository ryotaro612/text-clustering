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
import org.ranceworks.postclustering.token.BasicTokenizer
import org.tartarus.snowball.ext.PorterStemmer

import collection.JavaConverters._

class AppTest extends FlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val b = new EnglishAnalyzer()

    val ts: TokenStream = b.tokenStream("", new StringReader("Applications usually do not invoke analysis – Lucene does it for them. Applications construct Analyzers and pass then into Lucene, as follows pop values in last-in-first-out order"))


    val c: CharTermAttribute = ts.addAttribute(classOf[CharTermAttribute])
    val offsetAtt:OffsetAttribute  = ts.addAttribute(classOf[OffsetAttribute])


   try {
     ts.reset()
     while (ts.incrementToken()) {
       // Use AttributeSource.reflectAsString(boolean)
       // for token stream debugging.
       //System.out.println("token: " + ts.reflectAsString(true))
       println(c.toString)

       //System.out.println("token start offset: " + offsetAtt.startOffset())
       //System.out.println("  token end offset: " + offsetAtt.endOffset())
     }
     ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
   } finally {
     ts.close(); // Release resources associated with this stream.
   }

   }

  /*
    val a =  new PorterStemmer()
    a.setCurrent("Opens")
    a.stem()
    println(a.getCurrent)
    */


  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
