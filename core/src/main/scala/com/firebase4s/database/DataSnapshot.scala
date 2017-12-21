package com.firebase4s.database

import com.google.firebase.database.{DataSnapshot => FirebaseDataSnapshot}

case class DataSnapshot[A](private val snapshot: FirebaseDataSnapshot) {
  def getValue: Option[AnyRef] = {
    if (snapshot.exists) Some(snapshot.getValue()) else None
  }
}