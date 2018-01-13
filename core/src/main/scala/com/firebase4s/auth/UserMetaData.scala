package com.firebase4s.auth

import com.google.firebase.auth

case class UserMetaData(data: auth.UserMetadata) {
  val creationTimeStamp: Long = data.getCreationTimestamp
  val lastSignInTimestamp: Long = data.getLastSignInTimestamp
}
