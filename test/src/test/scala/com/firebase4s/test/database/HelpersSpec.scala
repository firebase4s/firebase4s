package com.firebase4s.test.database

import com.firebase4s.Helpers._
import com.firebase4s.database.DatabaseReference
import com.firebase4s.test.database.HelpersSpec._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike

import scala.collection.JavaConverters._

object HelpersSpec {
  case class A(str: String, lng: Long)
  case class B(dbl: Double) extends AnyVal
  case class C(a: A, b: B)
}
class HelpersSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  val caseClassRef: DatabaseReference = db.ref("test/caseClassRef")

  val c: C = C(A("AAA", 11), B(2.3d))

  "Database Helpers" should {
    "successfully convert a case class to a Java Map" when {
      "provided with a nested case class" in {
        val results = for {
          _ <- caseClassRef.set(toMap(c))
          values <- caseClassRef.get()
        } yield values

        results
          .map(
            _.getValue
              .map(_.asInstanceOf[Map[String, Any]])
              .fold(fail()) { r =>
                r.get("a").flatMap(_.asInstanceOf[java.util.HashMap[String, Any]].asScala.get("str")) shouldBe Some("AAA")
                r.get("a").flatMap(_.asInstanceOf[java.util.HashMap[String, Any]].asScala.get("lng")) shouldBe Some(11L)
                r.get("b") shouldBe Some(2.3d)
              }
          )

      }
    }
  }
}
