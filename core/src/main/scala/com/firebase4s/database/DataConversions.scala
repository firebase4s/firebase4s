package com.firebase4s.database

import scala.collection.JavaConverters._

object DataConversions {

  /**
    * Convert Java values to Scala
    *
    * The underlying snapshot will always return one of the following:
    * <ul>
    *   <li>Boolean
    *   <li>String
    *   <li>Long
    *   <li>Double
    *   <li>Map&lt;String, Object&gt;
    *   <li>List&lt;Object&gt;
    * </ul>
    */
  private[database] def snapshotValueAsScala(value: Any): Any = {
    value match {
      case b: java.lang.Boolean => b.asInstanceOf[Boolean]
      case s: java.lang.String => s
      case l: java.lang.Long => l.toLong
      case d: java.lang.Double => d.toDouble
      case m: java.util.HashMap[String, Object] => m.asScala
      case l: java.util.List[Object] => l.asScala.toList
    }
  }


  /**
    * Perform simple conversion for Map and List
    */
  private[database] def refValueAsJava(value: Any): Any = {
    value match {
      case l: List[_] => l.asJava
      case m: Map[_, _] => mapAsJavaMap(m)
      case _ => value
    }
  }

}