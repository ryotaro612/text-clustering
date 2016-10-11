package org.ranceworks.postclustering.token

/**
  * Created by kzs on 2016/10/11.
  */
class BasicTokenizer extends  Tokenizer {
  override def tokenize(text: String): List[String] =  {
    text.split("\\s+").toList
  }
}
