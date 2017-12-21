package com.firebase4s

import scala.concurrent.{Future, Promise}
import com.google.firebase.database.{
  DatabaseError => FirebaseDatabaseError,
  DatabaseReference => FirebaseDatabaseReference}


/**
  * Represents an instance of a DatabaseReference
  * @param path
  * @param ref
  */
class DatabaseReference(private val path: String, private val ref: FirebaseDatabaseReference) {
  def setValue[A](a: A): Future[A] = {
    val p = Promise[A]()
    ref.setValue(a, new FirebaseDatabaseReference.CompletionListener {
      override def onComplete(error: FirebaseDatabaseError, ref: FirebaseDatabaseReference): Unit = {
        if (error != null) {
          p.failure(new Exception(error.getMessage))
        } else {
          p.success(a)
        }
      }
    })
    p.future
  }
}
