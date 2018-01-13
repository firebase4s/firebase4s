package com.firebase4s.auth

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.google.firebase.auth
import com.firebase4s.util.FutureConverters.scalaFutureFromApiFuture

/**
  * Represents an Auth instance
  * @param authentication
  */
class Auth(private val authentication: auth.FirebaseAuth) {

  def getUser(uid: String): Future[UserRecord] = {
    scalaFutureFromApiFuture(authentication.getUserAsync(uid)).map(UserRecord)
  }

  def getUserByEmail(email: String): Future[UserRecord] = {
    scalaFutureFromApiFuture(authentication.getUserByEmailAsync(email)).map(UserRecord)
  }

  def getUserByPhoneNumber(phone: String): Future[UserRecord] = {
    scalaFutureFromApiFuture(authentication.getUserByPhoneNumberAsync(phone)).map(UserRecord)
  }
}

/**
  * Provides access to an instance of Firebase.Auth
  */
object Auth {
  import com.firebase4s.App
  def getInstance(): Auth = {
    require(App.initialized, "Firebase4S App must be initialized before accessing Auth")
    new Auth(auth.FirebaseAuth.getInstance())
  }
}
