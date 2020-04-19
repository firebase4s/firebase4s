package com.firebase4s.test.database

import com.firebase4s.database.{ DataSnapshot, DatabaseReference }
import com.firebase4s.test.Test.db
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike

import scala.concurrent.Future

class SimpleValuesSpec extends AsyncWordSpecLike with Matchers {

  "DatabaseReference" should {
    "successfully set a value at its location" when {
      "provided with a simple value" in {

        val stringRef: DatabaseReference = db.ref("test/string")
        val intRef: DatabaseReference = db.ref("test/int")
        val longRef: DatabaseReference = db.ref("test/long")
        val doubleRef: DatabaseReference = db.ref("test/double")
        val booleanRef: DatabaseReference = db.ref("test/boolean")

        def setValues(): Future[List[Any]] =
          Future.sequence(
            List(
              stringRef.set("apple"),
              intRef.set(1),
              longRef.set(1L),
              doubleRef.set(1.2),
              booleanRef.set(true)
            )
          )

        def getValues(): Future[List[(DataSnapshot, Any)]] =
          Future
            .sequence(
              List(
                stringRef.get(),
                intRef.get(),
                longRef.get(),
                doubleRef.get(),
                booleanRef.get()
              )
            )
            .map(_.zip(List("apple", 1, 1L, 1.2, true)))

        val results: Future[List[(DataSnapshot, Any)]] = for {
          _ <- setValues()
          values <- getValues()
        } yield values

        results.map(resultsList => assert(resultsList.forall(r => r._1.getValue.fold(fail())(_ == r._2))))
      }
    }
  }
}
