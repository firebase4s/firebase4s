package com.firebase4s.test.database

import com.firebase4s.database.DatabaseReference
import com.firebase4s.test.Test.db
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpecLike

class OptionValuesSpec extends AsyncWordSpecLike with Matchers {

  "DatabaseReference" should {
    "successfully set a value at its location" when {
      "provided with an Some(value)" in {

        val optionValue = Some("option")
        val optionRef: DatabaseReference = db.ref("test/someString")

        optionRef
          .set(optionValue)
          .flatMap(_ => optionRef.get())
          .map(snapshot => assert(snapshot.getValue == optionValue))

      }
    }

    "successfully set null at its location" when {
      "provided with a None" in {

        val noneRef: DatabaseReference = db.ref("test/none")

        noneRef
          .set(None)
          .flatMap(_ => noneRef.get())
          .map(snapshot => assert(!snapshot.exists))
      }
    }
  }
}
