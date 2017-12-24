package com.firebase4s.database

import com.google.firebase.database
import scala.collection.JavaConverters._
import DataConversions._

case class DataSnapshot(private val snapshot: database.DataSnapshot) {

  /**
    * Get a snapshot at the specified path
    * @param path
    * @return
    */
  def child(path: String): DataSnapshot = DataSnapshot(snapshot.child(path))

  /**
    * Returns true if the snapshot contains a (non-null) value
    * @return
    */
  def exists: Boolean = snapshot.exists()

  /**
    * Get all of the immediate children of the snapshot
    * @return
    */
  def getChildren: Stream[DataSnapshot] = {
    snapshot.getChildren.asScala.toStream.map(DataSnapshot)
  }

  /**
    * Get the number of immediate children of this snapshot
    * @return
    */
  def getChildrenCount: Long = snapshot.getChildrenCount

  /**
    * Get the data contained within the snapshot as an Option
    * @return
    */
  def getValue: Option[Any] = {
    if (snapshot.exists()) {
      Some(DataConversions.snapshotValueAsScala(snapshot.getValue))
    } else {
      None
    }
  }

  /**
    * Does the snapshot have data at a particular path?
    * @param path
    * @return
    */
  def hasChild(path: String): Boolean = snapshot.hasChild(path)

  /**
    * Does the snapshot have any children?
    * @return
    */
  def hasChildren: Boolean = snapshot.hasChildren
}