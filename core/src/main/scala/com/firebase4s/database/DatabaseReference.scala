package com.firebase4s.database

import scala.concurrent.{Future, Promise}
import com.google.firebase.database.{
  DataSnapshot => FirebaseDataSnapshot,
  ValueEventListener,
  DatabaseError => FirebaseDatabaseError,
  DatabaseReference => FirebaseDatabaseReference
}

/**
  * Represents an instance of a DatabaseReference
  * @param path
  * @param ref
  */
class DatabaseReference(private val path: String, private val ref: FirebaseDatabaseReference) {

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
      new FirebaseDatabaseReference.CompletionListener {
        override def onComplete(error: FirebaseDatabaseError, ref: FirebaseDatabaseReference): Unit = {
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
  def get[A](): Future[DataSnapshot[A]] = {
    val p = Promise[DataSnapshot[A]]()
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
      override def onDataChange(snapshot: FirebaseDataSnapshot): Unit = {
          p.success(DataSnapshot[A](snapshot))
      }
      override def onCancelled(error: FirebaseDatabaseError): Unit = {
        p.failure(new Exception(error.getMessage))
      }
    })
    p.future
  }

}
