package com.firebase4s.test.auth

import com.firebase4s.auth.{ UserCreationProps, UserRecord, UserUpdateProps }
import scala.concurrent.Future
import org.scalatest._

class UpdateUserSpec extends AsyncWordSpecLike with BeforeAndAfter with Matchers {

  import com.firebase4s.test.Test._

  "Auth" should {
    "successfully update a user" when {
      "provided with non-empty UserCreationProps" in {

        val creationProps: UserCreationProps = UserCreationProps(
          email = Some(randomEmail()),
          emailVerified = Some(true),
          displayName = Some("testUser"),
          phoneNumber = Some(randomPhone()),
          photoUrl = Some("http://firebase4s.com/photo.jpeg")
        )

        val updateProps: UserUpdateProps = UserUpdateProps(
          email = Some(randomEmail()),
          displayName = Some("updatedTestUser")
        )

        def getOrCreateUser(email: String): Future[UserRecord] =
          auth
            .getUserByEmail(email)
            .recoverWith { case _: Throwable => auth.createUser(creationProps) }

        getOrCreateUser(creationProps.email.get)
          .flatMap(user => auth.updateUser(user.uid, updateProps))
          .map(user => {
            assert(user.email == updateProps.email)
            assert(user.displayName == updateProps.displayName)
          })

      }
    }
  }
}
