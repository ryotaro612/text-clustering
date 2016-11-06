package org.ranceworks.postclustering.token

import collection.JavaConverters._

class JapaneseTokenizer extends Tokenizer {


  override def tokenize(text: String): List[String] = {
    val tokenizer =  new com.atilika.kuromoji.ipadic.Tokenizer()
    tokenizer.tokenize(text).asScala.toList.map(token => token.getBaseForm)
  }
}
