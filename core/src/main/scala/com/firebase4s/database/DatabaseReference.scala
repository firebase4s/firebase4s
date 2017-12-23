package com.firebase4s.database

import scala.concurrent.{Future, Promise}
import com.google.firebase.database
import DataConversions.refValueAsJava

/**
  * Represents an instance of a DatabaseReference
  * @param path
  * @param ref
  */
class DatabaseReference(private val path: String, private val ref: database.DatabaseReference) {

  type EventListenerId = String

  private val eventListeners =
    scala.collection.mutable.Map[EventListenerId, database.ValueEventListener]()

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


  /**
    * Listen for changes in the data at this location. With each change, the listener
    * with Either[Exception, DataSnapshot]
    * @param cb
    * @return
    */
  def addEventListener(cb: Either[Exception, DataSnapshot] => Unit): EventListenerId = {
    val listener = new database.ValueEventListener() {
      override def onDataChange(snapshot: database.DataSnapshot): Unit = {
        cb(Right(DataSnapshot(snapshot)))
      }
      override def onCancelled(error: database.DatabaseError): Unit = {
        cb(Left(new Exception(error.getMessage)))
      }
    }
    val id: EventListenerId = java.util.UUID.randomUUID.toString
    eventListeners += ((id, listener))
    ref.addValueEventListener(listener)
    id
  }


  /**
    * Remove an event listener from the data location
    * @param id
    * @return
    */
  def removeEventListener(id: EventListenerId): Option[EventListenerId] = {
    eventListeners.get(id).map(listener => {
      ref.removeEventListener(listener)
      id
    })
  }

  /**
    * Set a value at the reference location
    * @param value
    * @tparam A
    * @return
    */
  def set[A](value: A): Future[A] = {
    val p = Promise[A]()
    ref.setValue(
      refValueAsJava(value),
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

}
