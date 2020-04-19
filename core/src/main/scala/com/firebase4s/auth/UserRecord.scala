package com.firebase4s.auth

import com.google.firebase.auth

sealed trait UserProps {
  def email: Option[String]

  def emailVerified: Option[Boolean]

  def password: Option[String]

  def phoneNumber: Option[String]

  def displayName: Option[String]

  def photoUrl: Option[String]

  def disabled: Option[Boolean]
}

/**
 * Properties used to create a new user record
 *
 * @param email
 * @param emailVerified
 * @param password
 * @param phoneNumber
 * @param displayName
 * @param photoUrl
 * @param disabled
 * @param uid
 */
final case class UserCreationProps(
                                    email: Option[String] = None,
                                    emailVerified: Option[Boolean] = None,
                                    password: Option[String] = None,
                                    phoneNumber: Option[String] = None,
                                    displayName: Option[String] = None,
                                    photoUrl: Option[String] = None,
                                    disabled: Option[Boolean] = None,
                                    uid: Option[String] = None
                                  ) extends UserProps

/**
 * Properties used to create a new user record
 *
 * @param email
 * @param emailVerified
 * @param password
 * @param phoneNumber
 * @param displayName
 * @param photoUrl
 * @param disabled
 */
final case class UserUpdateProps(
                                  email: Option[String] = None,
                                  emailVerified: Option[Boolean] = None,
                                  password: Option[String] = None,
                                  phoneNumber: Option[String] = None,
                                  displayName: Option[String] = None,
                                  photoUrl: Option[String] = None,
                                  disabled: Option[Boolean] = None
                                ) extends UserProps {

}

final case class UserRecord(private val user: auth.UserRecord) {
  val uid: String = user.getUid
  val providerId: String = user.getProviderId
  val email: Option[String] = Option(user.getEmail)
  val emailVerified: Boolean = user.isEmailVerified
  val phoneNumber: Option[String] = Option(user.getPhoneNumber)
  val displayName: Option[String] = Option(user.getDisplayName)
  val photoUrl: Option[String] = Option(user.getPhotoUrl)
  val isDisabled: Boolean = user.isDisabled
  val metaData: UserMetaData = UserMetaData(user.getUserMetadata)
}
