package org.ranceworks.postclustering.token

import collection.JavaConverters._

class JapaneseTokenizer extends Tokenizer {

  val tokenizer =  new com.atilika.kuromoji.ipadic.Tokenizer()

  override def tokenize(text: String): List[String] = {
    tokenizer.tokenize(text).asScala.toList.map(token => token.getBaseForm)
  }
}
