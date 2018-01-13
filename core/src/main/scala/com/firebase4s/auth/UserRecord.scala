package com.firebase4s.auth

import com.google.firebase.auth

case class UserRecord(user: auth.UserRecord){
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
