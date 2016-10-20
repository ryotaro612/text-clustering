package org.ranceworks.postclustering.token

import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

import scala.annotation.tailrec

class EnglishTokenizer extends Tokenizer {
  override def tokenize(text: String): List[String] = {
    val analyzer = new EnglishAnalyzer()
    val tokenStream: TokenStream =  analyzer.tokenStream(this.getClass.getName, text)

    val charTermAttribute =  tokenStream.addAttribute(classOf[CharTermAttribute])

    @tailrec
    def findTokens(foundTokens: List[String], ts: TokenStream): List[String] = {
       ts.incrementToken() match {
        case true => findTokens(foundTokens :+ charTermAttribute.toString, ts)
        case false => foundTokens
      }
    }

    tokenStream.reset()
    val tokens = findTokens(List[String](), tokenStream)
    tokenStream.end()
    tokens
  }
}
