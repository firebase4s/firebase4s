package com.firebase4s.auth

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.google.firebase.auth
import com.firebase4s.util.FutureConverters.scalaFutureFromApiFuture

/**
  * Properties used to create a new user record
  * @param email
  * @param emailVerified
  * @param password
  * @param phoneNumber
  * @param displayName
  * @param photoUrl
  * @param disabled
  * @param uid
  */
case class UserCreationProps(
                              email: Option[String] = None,
                              emailVerified: Option[Boolean] = None,
                              password: Option[String] = None,
                              phoneNumber: Option[String] = None,
                              displayName: Option[String] = None,
                              photoUrl: Option[String] = None,
                              disabled: Option[Boolean] = None,
                              uid: Option[String] = None
                            )

/**
  * Represents an Auth instance
  * @param authentication
  */
class Auth(private val authentication: auth.FirebaseAuth) {

  /**
    * Build a CreateRequest from UserCreationProps
    * @param props
    * @return
    */
  private def createRequest(props: UserCreationProps): auth.UserRecord.CreateRequest = {
    val request = new CreateRequest()
    props.email.foreach(request.setEmail)
    props.emailVerified.foreach(request.setEmailVerified)
    props.password.foreach(request.setPassword)
    props.phoneNumber.foreach(request.setPhoneNumber)
    props.photoUrl.foreach(request.setPhotoUrl)
    props.disabled.foreach(request.setDisabled)
    props.uid.foreach(request.setUid)
    request
  }

  /**
    * Creates a new user record based on the specified properties.  If no properties are set
    * in the UserCreationProps, no record will be created.
    * @param props
    * @return
    */
  def createUser(props: UserCreationProps): Future[UserRecord] = {
    scalaFutureFromApiFuture(authentication.createUserAsync(createRequest(props))).map(UserRecord)
  }

  /**
    * Get the user record corresponding to the specified uid
    * @param uid
    * @return
    */
  def getUser(uid: String): Future[UserRecord] = {
    scalaFutureFromApiFuture(authentication.getUserAsync(uid)).map(UserRecord)
  }

  /**
    * Get the user record corresponding to the specified email
    * @param email
    * @return
    */
  def getUserByEmail(email: String): Future[UserRecord] = {
    scalaFutureFromApiFuture(authentication.getUserByEmailAsync(email)).map(UserRecord)
  }

  /**
    * Get the user record corresponding to the specified phone number
    * @param phone
    * @return
    */
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
