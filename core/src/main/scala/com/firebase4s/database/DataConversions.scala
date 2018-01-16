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
    * Converts a Map to its Java counterpart for the purpose of updating several
    * child locations simultaneously.  Any instances of Option.some are converted
    * to their underlying values and any instances of Option.none are converted to
    * null values.
    *
    * @param update
    * @return
    */
  def childUpdateAsJava(update: Map[String, AnyRef]): java.util.Map[String, AnyRef] = {
    mapAsJavaMap(update.mapValues(value => {
      if (value.isInstanceOf[Option[_]]) {
        value match {
          case Some(v) => v.asInstanceOf[AnyRef]
          case None => null
        }
      } else {
        value
      }
    }))
  }

  /**
    * Perform simple conversion for Map and List
    */
  private[database] def refValueAsJava(value: Any): Any = {
    value match {
      case l: List[_] => l.asJava
      case m: Map[_, _] => mapAsJavaMap(m)
      case Some(v) => v
      case None => null
      case _ => value
    }
  }

}