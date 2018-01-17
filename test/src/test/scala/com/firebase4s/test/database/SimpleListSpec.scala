package com.firebase4s.test.database

import scala.concurrent.Future
import org.scalatest._
import com.firebase4s.database.DataSnapshot

class SimpleListSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  "DatabaseReference" should {
    "successfully set a value at its location" when {
      "provided with a list containing simple values" in {

        val intListRef = db.ref("test/intList")
        val intList: List[Int] = List(1, 2, 3)

        val stringListRef = db.ref("test/stringList")
        val stringList: List[String] = List("apple", "banana", "clementine")

        def setValues(): Future[List[Any]] =
          Future.sequence(
            List(
              intListRef.set(intList),
              stringListRef.set(stringList)
            )
          )

        def getValues(): Future[List[(DataSnapshot, Any)]] =
          Future
            .sequence(
              List(
                intListRef.get(),
                stringListRef.get(),
              )
            )
            .map(_.zip(List(intList, stringList)))

        val results: Future[List[(DataSnapshot, Any)]] = for {
          _ <- setValues()
          values <- getValues()
        } yield values

        results.map(resultsList => {
          assert(resultsList.forall(r => r._1.getValue.contains(r._2)))
        })
      }
    }
  }
}
