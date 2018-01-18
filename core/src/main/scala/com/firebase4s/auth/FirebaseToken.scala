package com.firebase4s.auth

import scala.collection.JavaConverters._

case class FirebaseToken(private val token: com.google.firebase.auth.FirebaseToken) {

  /**
    * The uid associated with this token
    * @return
    */
  def uid: String = token.getUid

  /**
    * The issuer for this token
    * @return
    */
  def issuer: String = token.getIssuer

  /**
    * The user's display name associated with this token
    * @return
    */
  def name: Option[String] = Option(token.getName)

  /**
    * The email address of the user associated with this token
    * @return
    */
  def email: Option[String] = Option(token.getEmail)

  /**
    * The status of user email verification for this token
    * @return
    */
  def emailVerified: Boolean = token.isEmailVerified

  /**
    * The url of the photo associated with this user
    * @return
    */
  def photoUrl: Option[String] = Option(token.getPicture)

  /**
    * All claims associated with this token
    * @return
    */
  def claims: Map[String, AnyRef] = token.getClaims.asScala.toMap
}
