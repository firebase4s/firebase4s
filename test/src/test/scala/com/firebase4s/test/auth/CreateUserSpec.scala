package com.firebase4s.test.auth

import scala.concurrent.Future
import com.firebase4s.auth.UserCreationProps
import org.scalatest._

class CreateUserSpec extends AsyncWordSpecLike with BeforeAndAfter with Matchers {

  import com.firebase4s.test.Test._

  def deleteUserByEmail(email: String): Future[String] = {

    val onError: PartialFunction[Throwable, String] = {
      case _: Throwable => ""
    }

    auth.getUserByEmail(email)
      .flatMap(user => auth.deleteUser(user.uid))
        .recover(onError)

  }

  "Auth" should {
    "successfully create a user" when {
      "provided with non-empty UserCreationProps" in {

        val props = UserCreationProps(
          email = Some(randomEmail()),
          emailVerified = Some(true),
          displayName = Some("testUser"),
          phoneNumber = Some(randomPhone()),
          photoUrl = Some("http://testing.com/photo.jpeg")
        )

        deleteUserByEmail(props.email.get)
          .flatMap(_ => auth.createUser(props))
          .map(user => {
            assert(user.email == props.email)
            assert(user.emailVerified == props.emailVerified.get)
            assert(user.displayName == props.displayName)
            assert(user.phoneNumber == props.phoneNumber)
            assert(user.photoUrl == props.photoUrl)

          })
      }
    }
    "successfully create an anonymous user" when {
      "not provided with UserCreationProps" in {
        auth
          .createUser()
          .map(user => assert(user.uid.nonEmpty))
      }
    }
  }
}
