package com.firebase4s.database

import scala.concurrent.{Future, Promise}
import com.google.firebase.database
import DataConversions.{childUpdateAsJava, refValueAsJava}

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

  /**
    * Create a DatabaseReference at an automatically-generated child location.
    * @return
    */
  def push(): DatabaseReference = {
    val childRef: database.DatabaseReference = ref.push()
    DatabaseReference.fromRef(childRef)
  }

  /**
    * Update the specified child keys to the specified values. Option.None values will be
    * converted to null, thereby removing the value at specified location.  Option.Some
    * values will set the underlying value at the specified location.
    *
    * @param update
    * @return
    */
  def updateChildren(update: Map[String, AnyRef]): Future[Any] = {
    val p = Promise[Any]()
    ref.updateChildren(
      childUpdateAsJava(update),
      new database.DatabaseReference.CompletionListener {
        override def onComplete(error: database.DatabaseError, ref: database.DatabaseReference): Unit = {
          if (error != null) {
            p.failure(new Exception(error.getMessage))
          } else {
            p.success(update)
          }
        }
      }
    )
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
    eventListeners
      .get(id)
      .map(listener => {
        ref.removeEventListener(listener)
        id
      })
  }

  /**
    * @return The last token in the location pointed to by this reference
    */
  def getKey: String = ref.getKey

  /**
    *
    * @return The path to the DatabaseReference location
    */
  def getPath: String = ref.getPath.toString

  /**
    * @return Some(DatabaseReference) to the parent location or None if
    *         this DatabaseReference is at the root
    */
  def getParent: Option[DatabaseReference] = {
    val parent: database.DatabaseReference = ref.getParent
    if (parent == null) {
      None
    } else {
      Some(DatabaseReference.fromRef(parent))
    }
  }

  /**
    * @return A DatabaseReference for the root location of this Firebase Database
    */
  def getRoot: DatabaseReference = {
    val root: database.DatabaseReference = ref.getRoot
    DatabaseReference.fromRef(root)
  }
}

object DatabaseReference {
  private[database] def fromRef(ref: database.DatabaseReference): DatabaseReference = {
    new DatabaseReference(ref.getPath.toString, ref)
  }
}
