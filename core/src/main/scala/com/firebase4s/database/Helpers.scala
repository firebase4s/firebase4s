package com.firebase4s.database

object Helpers {

  /**
    * A helper method for converting an instance of a case class to
    * a Map[String, Any] that can be set as a DatabaseRef value
    * @param a - An instance of the case class of type A
    * @tparam A
    */
  implicit class toMap[A](val a: A) extends AnyVal {
    import shapeless._
    import ops.record._

    def toMap[L <: HList](implicit
                          gen: LabelledGeneric.Aux[A, L],
                          tmr: ToMap[L]): Map[String, Any] = {
      val m: Map[tmr.Key, tmr.Value] = tmr(gen.to(a))
      m.map { case (k: Symbol, v) => k.name -> v }
    }
  }
}