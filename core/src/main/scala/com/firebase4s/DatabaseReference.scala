package com.firebase4s

import com.google.firebase.database.{DatabaseReference => FirebaseDatabaseReference}


/**
  * Represents an instance of a DatabaseReference
  * @param path
  * @param ref
  */
class DatabaseReference(private val path: String, private val ref: FirebaseDatabaseReference) {}
