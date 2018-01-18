package com.firebase4s.test.database

import org.scalatest._
import com.firebase4s.database.DatabaseReference

import scala.beans.BeanProperty

class Name() {
  @BeanProperty var first: String = _
  @BeanProperty var last: String = _
  @BeanProperty var middle: String = _
}


class User() {
  @BeanProperty var name: Name = _
  @BeanProperty var email: String = _
}



class JavaClassSpec extends AsyncWordSpecLike with Matchers {

  import com.firebase4s.test.Test.db

  val userData = new User()
  userData.email = "tim@firebase4s.com"
  val name = new Name()
  name.first = "tim"
  name.last =  "pike"
  name.middle = "d"
  userData.name = name

  val classRef: DatabaseReference = db.ref(s"test/${userData.name.first}")

  "DatabaseReference" should {
    "successfully set a value at its location" when {
      "provided with an instance of a JavaBean class" in {
        classRef
          .set(userData)
          .map(user => {
            assert(user.name.first == userData.name.first)
            assert(user.name.last == userData.name.last)
            assert(user.name.middle == userData.name.middle)
            assert(user.email == userData.email)
          })
      }
    }

    "return a snapshot containing a class instance" when {
      "provided with the class corresponding to the data at its location" in {
        classRef
          .get()
          .map(snapshot => snapshot.getValue(classOf[User]))
            .map(maybeUser => {
              assert(maybeUser.nonEmpty)
              val user = maybeUser.get
              assert(user.getClass == classOf[User])
              assert(user.name.getClass == classOf[Name])
              assert(user.name.first == userData.name.first)
              assert(user.name.last == userData.name.last)
              assert(user.name.middle == userData.name.middle)
              assert(user.email == userData.email)
            })
      }
    }
  }
}
