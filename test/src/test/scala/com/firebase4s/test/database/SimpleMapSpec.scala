package com.firebase4s.test.database

import scala.concurrent.Future
import org.scalatest._
import com.firebase4s.database.DataSnapshot

class SimpleMapSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  "DatabaseReference" should {
    "successfully set a value at its location" when {
      "provided with a Map[String, _] containing simple values" in {

        val intMapRef = db.ref("test/intMap")
        val intMap: Map[String, Int] = Map("one" -> 1, "two" -> 2)

        val stringMapRef = db.ref("test/stringMap")
        val stringMap: Map[String, String] = Map("a" -> "apple", "b" -> "banana")

        def setValues(): Future[List[Any]] =
          Future.sequence(
            List(
              intMapRef.set(intMap),
              stringMapRef.set(stringMap)
            )
          )

        def getValues(): Future[List[(DataSnapshot, Any)]] =
          Future
            .sequence(
              List(
                intMapRef.get(),
                stringMapRef.get(),
              )
            )
            .map(_.zip(List(intMap, stringMap)))

        val results: Future[List[(DataSnapshot, Any)]] = for {
          _ <- setValues()
          values <- getValues()
        } yield values

        results.map(resultsList => {
          assert(resultsList.forall(r => r._1.getValue.contains(r._2)))
        })
      }

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

    "convert values to List[A]" when {
      "setting values of type Seq[A]" in {

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
