package com.firebase4s.database

import scala.concurrent.{Future, Promise}
import com.google.firebase.database

/**
  * Represents an instance of a DatabaseReference
  * @param path
  * @param ref
  */
class DatabaseReference(private val path: String, private val ref: database.DatabaseReference) {

  /**
    * Set a value at the reference location
    * @param value
    * @tparam A
    * @return
    */
  def set[A](value: A): Future[A] = {
    val p = Promise[A]()
    ref.setValue(
      value,
      new database.DatabaseReference.CompletionListener {
        override def onComplete(error: database.DatabaseError, ref: database.DatabaseReference): Unit = {
          if (error != null) {
            p.failure(new Exception(error.getMessage))
          } else {
            p.success(value)
          }
        }
      }
    )
    p.future
  }

  /**
    * Get the value at the reference location
    * @tparam A
    * @return
    */
  def get[A](): Future[DataSnapshot] = {
    val p = Promise[DataSnapshot]()
    ref.addListenerForSingleValueEvent(new database.ValueEventListener() {
      override def onDataChange(snapshot: database.DataSnapshot): Unit = {
          p.success(DataSnapshot(snapshot))
      }
      override def onCancelled(error: database.DatabaseError): Unit = {
        p.failure(new Exception(error.getMessage))
      }
    })
    p.future
  }
}
