package com.firebase4s.test.database

import org.scalatest._
import com.firebase4s.database.DatabaseReference
import com.firebase4s.database.Helpers._

class HelpersSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  val caseClassRef: DatabaseReference = db.ref("test/caseClassRef")
  case class User(name: String, age: Int)

  "Database Helpers" should {
    "successfully convert a case class to a Java Map" when {
      "provided with a non-nested case class" in {

        val user = User("tim", 44)

        val results = for {
          _ <- caseClassRef.set(user.toMap)
          values <- caseClassRef.get()
        } yield values

        results
          .map(_.getValue.map(_.asInstanceOf[Map[String, Any]])
          .getOrElse(Map.empty[String, Any]))
          .map(map => {
            assert(map.getOrElse("name", "") == user.name)
            assert(map.getOrElse("age", "") == user.age)
          })

      }
    }
  }
}
