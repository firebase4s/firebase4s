package com.firebase4s

import scala.language.experimental.macros

object Helpers {
  def toMap[T](inst: T): Map[String, Any] = macro CaseToMapMacroImpl.convert[T]
}
