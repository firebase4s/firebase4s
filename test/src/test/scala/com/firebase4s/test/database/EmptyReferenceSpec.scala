package com.firebase4s.test.database

import org.scalatest._
import com.firebase4s.database.DatabaseReference

class EmptyReferenceSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  val emptyRef: DatabaseReference = db.ref("test/empty")

  "DatabaseReference" should {
    "return an empty snapshot" when {
      "no value exists at the its location" in {
        val result = for {
          _ <- emptyRef.set(None)
          snapshot <- emptyRef.get()
        } yield snapshot
        result map { snapshot =>
          assert(snapshot.getValue.isEmpty)
        }
      }
    }
  }
}
