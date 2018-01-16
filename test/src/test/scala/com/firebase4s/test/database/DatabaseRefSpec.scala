package com.firebase4s.test.database

import scala.concurrent.Future
import org.scalatest._
import com.firebase4s.database.{ DataSnapshot, DatabaseReference }

class DatabaseRefSpec extends AsyncWordSpecLike with Matchers {
  import com.firebase4s.test.Test.db

  val emptyRef: DatabaseReference = db.ref("test/empty")

  "DatabaseRef" should {
    "return an empty snapshot" when {
      "no value exists at the ref location" in {
        val result = for {
          _ <- emptyRef.set(None)
          snapshot <- emptyRef.get()
        } yield snapshot
        result map { snapshot =>
          assert(snapshot.getValue.isEmpty)
        }
      }
    }

    "delete the value at its location" when {
      "setting Option.None as a value" in {
        val result = for {
          _ <- emptyRef.set("value")
          _ <- emptyRef.set(None)
          snapshot <- emptyRef.get()
        } yield snapshot
        result map { snapshot =>
          assert(snapshot.getValue.isEmpty)
        }
      }
    }
  }

  "successfully set values at its location" when {

    "provided with primitive-like values" in {

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

      results.map(resultsList => assert(resultsList.forall(r => r._1.getValue.contains(r._2))))
    }

    "provided with a map whose values are primitive-like values" in {

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

    "provided with a list whose values are primitive-like values" in {

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
