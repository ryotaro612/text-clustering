package org.ranceworks.postclustering.token

import org.tartarus.snowball.ext.EnglishStemmer

class BasicTokenizer extends  Tokenizer {
  override def tokenize(text: String): List[String] =  {
    text.split("\\s+").toList
  }
}
