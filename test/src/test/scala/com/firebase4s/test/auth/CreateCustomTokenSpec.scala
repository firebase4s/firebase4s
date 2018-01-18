package com.firebase4s.test.auth

import org.scalatest.{ AsyncWordSpecLike, Matchers }

class CreateCustomTokenSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.auth

  "Auth" should {
    "successfully create a custom token" when {
      "provided with additional claims" in {

        auth
          .createUser()
          .map(_.uid)
          .flatMap(uid => auth.createCustomToken(uid, Some(Map("admin" -> true))))
          .map(token => assert(token.nonEmpty))
      }
    }
  }
}
