package com.firebase4s.test.database

import scala.concurrent.Future
import org.scalatest._
import com.firebase4s.database.{DatabaseReference, DataSnapshot}
import com.firebase4s.util.FirebaseData._

class DataClassSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  val dataClassRef: DatabaseReference = db.ref("test/dataclass")
  case class User(name: String, age: Int)

  "FirebaseData" should {
    "successfully convert a case class to a Java Map" when {
      "provided with a non-nested case class" in {

        val user = User("tim", 44)
        val results = for {
          _ <- dataClassRef.set(user.asDataClass)
          values <- dataClassRef.get()
        } yield values

        results.map(snapshot => {
          val maybeMap = snapshot.getValueAs[Map[String, Any]]
          maybeMap.map(m => assert(m.getOrElse("name", "") == user.name))
          maybeMap.map(m => assert(m.getOrElse("age", "") == user.age))
          assert(snapshot.exists)
        })
      }
    }
  }

  "FirebaseData" should {
    "successfully convert a snapshot value to a case class" when {
      "the underlying Java map maps to the case class" in {

        val user = User("tim", 55)
        val results = for {
          _ <- dataClassRef.set(user.asDataClass)
          values <- dataClassRef.get()
        } yield values

        results.map(snapshot => {
          val x: Option[Map[String, Any]] = snapshot.getValueAsDataMap

          val r = x.flatMap(tim => {
            println(tim)
            to[User].from(tim)
          })

          println("OIXXOXOXOXO", r)
          assert(r.isDefined)

        })
      }
    }
  }
}
