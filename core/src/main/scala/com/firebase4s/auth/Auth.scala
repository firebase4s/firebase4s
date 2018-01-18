package com.firebase4s.auth

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import com.google.firebase.auth
import com.firebase4s.util.FutureConverters.scalaFutureFromApiFuture

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
    val request = new auth.UserRecord.CreateRequest()
    props.email.foreach(request.setEmail)
    props.emailVerified.foreach(request.setEmailVerified)
    props.password.foreach(request.setPassword)
    props.phoneNumber.foreach(request.setPhoneNumber)
    props.photoUrl.foreach(request.setPhotoUrl)
    props.disabled.foreach(request.setDisabled)
    props.uid.foreach(request.setUid)
    props.displayName.foreach(request.setDisplayName)
    request
  }

  private def updateRequest(uid: String, props: UserUpdateProps): auth.UserRecord.UpdateRequest = {
    val request = new auth.UserRecord.UpdateRequest(uid)
    props.email.foreach(request.setEmail)
    props.emailVerified.foreach(request.setEmailVerified)
    props.password.foreach(request.setPassword)
    props.phoneNumber.foreach(request.setPhoneNumber)
    props.photoUrl.foreach(request.setPhotoUrl)
    props.disabled.foreach(request.setDisabled)
    props.displayName.foreach(request.setDisplayName)
    request
  }

  /**
    * Creates a new user record based on the specified properties.  If no properties are set
    * in the UserCreationProps, an anonymous user record will be created.
    * @param props
    * @return
    */
  def createUser(props: UserCreationProps = UserCreationProps()): Future[UserRecord] =
    scalaFutureFromApiFuture(authentication.createUserAsync(createRequest(props))).map(UserRecord)

  /**
    * Deletes the user corresponding to the provided uid
    * @param uid
    * @return
    */
  def deleteUser(uid: String): Future[String] = {
    scalaFutureFromApiFuture(authentication.deleteUserAsync(uid)).map(_ => uid)
  }

  /**
    * Updates a user record based on the specified properties.
    * @param props
    * @return
    */
  def updateUser(uid: String, props: UserUpdateProps): Future[UserRecord] =
    scalaFutureFromApiFuture(authentication.updateUserAsync(updateRequest(uid, props))).map(UserRecord)

  /**
    * Creates a custom token associated with the provided uid
    * https://firebase.google.com/docs/auth/admin/create-custom-tokens
    * @param uid
    * @param claims
    * @return
    */
  def createCustomToken[A](uid: String, claims: Option[Map[String, Any]]): Future[String] =
    claims match {
      case None => scalaFutureFromApiFuture(authentication.createCustomTokenAsync(uid))
      case Some(c) =>
        val claimsAsJavaMap = c.asJava.asInstanceOf[java.util.Map[String, Object]]
        scalaFutureFromApiFuture(authentication.createCustomTokenAsync(uid, claimsAsJavaMap))
    }

  /**
    * Get the user record corresponding to the specified uid
    * @param uid
    * @return
    */
  def getUser(uid: String): Future[UserRecord] =
    scalaFutureFromApiFuture(authentication.getUserAsync(uid)).map(UserRecord)

  /**
    * Get the user record corresponding to the specified email
    * @param email
    * @return
    */
  def getUserByEmail(email: String): Future[UserRecord] =
    scalaFutureFromApiFuture(authentication.getUserByEmailAsync(email)).map(UserRecord)

  /**
    * Get the user record corresponding to the specified phone number
    * @param phone
    * @return
    */
  def getUserByPhoneNumber(phone: String): Future[UserRecord] =
    scalaFutureFromApiFuture(authentication.getUserByPhoneNumberAsync(phone)).map(UserRecord)

  /**
    * Verifies a Firebase ID Token
    * @param idToken
    * @return
    */
  def verifyIdToken(idToken: String):Future[FirebaseToken] = {
    scalaFutureFromApiFuture(authentication.verifyIdTokenAsync(idToken)).map(FirebaseToken)
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
