package com.firebase4s.test.database

import scala.concurrent.Future
import org.scalatest._
import com.firebase4s.database.DataSnapshot

class SimpleSequenceSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  "DatabaseReference" should {
    "successfully set a value as a List[_] at its location" when {
      "when provided with a value of type Seq[A]" in {

        val intVectorRef = db.ref("test/intVector")
        val intVector: Vector[Int] = Vector(1, 2, 3)

        val stringIterableRef = db.ref("test/stringIterable")
        val stringIterable: Iterable[String] = Iterable("apple", "banana", "clementine")

        def setValues(): Future[List[Any]] =
          Future.sequence(
            List(
              intVectorRef.set(intVector),
              stringIterableRef.set(stringIterable)
            )
          )

        def getValues(): Future[List[(DataSnapshot, Any)]] =
          Future
            .sequence(
              List(
                intVectorRef.get(),
                stringIterableRef.get()
              )
            )
            .map(_.zip(List(intVector.toList, stringIterable.toList)))

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
