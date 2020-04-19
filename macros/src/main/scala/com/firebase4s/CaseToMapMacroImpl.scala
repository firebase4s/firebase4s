package com.firebase4s

//import scala.reflect.macros.blackbox
import scala.reflect.macros.Context

class CaseToMapMacroImpl(val c: Context) {

  import c.universe._

  private[this] lazy final val AnyValType = weakTypeOf[AnyVal]

  def convert[T: WeakTypeTag](inst: Expr[T]): Expr[Map[String, Any]] = {
    val reificatedMap = inst.tree.tpe.typeSymbol.asClass.primaryConstructor.asMethod.paramLists
      .flatMap { params =>
        params.map { p =>
          internalTypeReificate(inst, p)
        }
      }

    c.Expr[Map[String, Any]](q"Map[String, Any](..$reificatedMap)")
  }

  def isCaseClass(sym: Symbol): Boolean =
    sym.isClass &&
    sym.asClass.isCaseClass

  def isAnyValCaseClass(termSym: Symbol): Boolean =
    isCaseClass(termSym.typeSignature.typeSymbol) &&
    termSym.typeSignature.<:<(AnyValType)

  private[this] final def internalTypeReificate[T](instance: Expr[T], termSymbol: Symbol): Expr[(String, Any)] =
    termSymbol match {
      case x if isAnyValCaseClass(x) =>
        c.Expr[(String, Any)](q"""${c
          .parse(s""""${x.name}"""")} -> $instance.${x.name.toTermName}.${requireSingleParamExtract(x).name.toTermName}""")
      case x if isCaseClass(x.typeSignature.typeSymbol) =>
        c.Expr[(String, Any)](
          q"""${c
            .parse(s""""${x.name}"""")} -> _root_.com.firebase4s.Helpers.toMap[$termSymbol]($instance.${x.name.toTermName}) // recall"""
        )
      case x if x.isTerm =>
        c.Expr[(String, Any)](q"""${c.parse(s""""${x.name}"""")} -> $instance.${x.name.toTermName}""")
      case _ =>
        c.abort(c.enclosingPosition, "Cannot expand a class that doesn't have a primary constructor")
    }

  private[this] final def requireSingleParamExtractAbort(typeSymbol: Symbol): Symbol =
    c.abort(c.enclosingPosition,
            s"The primary constructor of the AnyVal class must have only one argument. ${typeSymbol.name}")
  private[this] final def requireSingleParamExtract(termSymbol: Symbol): Symbol = {
    val typeSym = termSymbol.typeSignature.typeSymbol
    if (typeSym.isClass) {
      val pc = typeSym.asClass.primaryConstructor
      if (pc.isMethod) {
        pc.asMethod.paramLists match {
          case List(List(s)) => s
          case _             => requireSingleParamExtractAbort(typeSym)
        }
      } else requireSingleParamExtractAbort(typeSym)
    } else requireSingleParamExtractAbort(typeSym)
  }
}
