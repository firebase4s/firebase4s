package com.firebase4s.test.database

import org.scalatest._
import com.firebase4s.database.DatabaseReference

class DeleteReferenceSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  val deleteRef: DatabaseReference = db.ref("test/delete")

  "DatabaseReference" should {
    "delete the value at its location" when {
      "setting Option.None as a value" in {
        val result = for {
          _ <- deleteRef.set("value")
          _ <- deleteRef.set(None)
          snapshot <- deleteRef.get()
        } yield snapshot
        result map { snapshot =>
          assert(snapshot.getValue.isEmpty)
        }
      }
    }
  }
}


