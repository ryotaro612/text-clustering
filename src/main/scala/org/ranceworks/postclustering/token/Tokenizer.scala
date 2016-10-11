package org.ranceworks.postclustering.token


trait Tokenizer extends  Serializable {

  def tokenize(text: String): List[String]
}
