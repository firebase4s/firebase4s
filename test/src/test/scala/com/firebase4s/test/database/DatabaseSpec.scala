package com.firebase4s.test.database

import com.firebase4s.database.DataSnapshot

import scala.concurrent.Future
import org.scalatest._

class DatabaseSpec extends AsyncFlatSpec {

  import com.firebase4s.test.Test.db

  private val fooRef = db.ref("test/foo")

  def setFoo(value: String): Future[String] = fooRef.set(value)
  def getFoo: Future[DataSnapshot] = fooRef.get()

  behavior of "setFoo"

  it should "set the value of the foo database reference" in {
    setFoo("bar") map { value => assert(value == "bar")}
  }

  behavior of "getFoo"

  it should "get the value of the foo database reference" in {
    getFoo map {snapshot => assert(snapshot.getValue.contains("bar")) }
  }
}
