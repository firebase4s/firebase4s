package com.firebase4s.database

import com.google.firebase.database

case class DataSnapshot[A](private val snapshot: database.DataSnapshot) {
  def getValue: Option[AnyRef] = {
    if (snapshot.exists) Some(snapshot.getValue()) else None
  }
}