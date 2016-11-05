package org.ranceworks.postclustering.javaconverter

import collection.JavaConverters._

object JConverter {

  def convertMap[A,B](map: java.util.Map[A,B]): Map[A,B] = mapAsScalaMapConverter(map).asScala.toMap

  def convertList[A](list: java.util.List[A]): List[A] = list.asScala.toList
}
