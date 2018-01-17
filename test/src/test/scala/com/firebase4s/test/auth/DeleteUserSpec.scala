package com.firebase4s.test.auth

import org.scalatest._

class DeleteUserSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.auth

  "Auth" should {
    "successfully delete a user" when {
      "provided with the uid of an existing user" in {

        val findDeletedUser = auth
          .createUser()
          .flatMap(user => auth.deleteUser(user.uid).recover { case _: Throwable => user.uid })
          .flatMap(uid => auth.getUser(uid))
          .recover { case e: Throwable => throw new Exception(e.getCause) }

        recoverToExceptionIf[Exception] {
          findDeletedUser
        }.map { e =>
          e.getCause.getMessage should startWith ("No user record found for the provided user ID")
        }

      }
    }
  }
}
