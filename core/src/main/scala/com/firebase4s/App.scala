package com.firebase4s

import java.io.InputStream
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.database.{FirebaseDatabase, DatabaseReference => FirebaseDatabaseReference}
import com.google.firebase.{FirebaseApp, FirebaseOptions}


class DatabaseReference(private val path: String, private val ref: FirebaseDatabaseReference){

}

class Database(private val db: FirebaseDatabase) {
  def ref(path: String): DatabaseReference = new DatabaseReference(path, db.getReference(path))
}


object Database {
  def getInstance(): Database = {
    require(App.initialized, "Firebase4S App must be initialized before accessing Database")
    new Database(FirebaseDatabase.getInstance())
  }
}



object App {
  protected[firebase4s] var initialized: Boolean = false
  def initialize(serviceAccountCredentials: InputStream, databaseUrl: String): Unit = {
    val options = new FirebaseOptions.Builder()
      .setCredentials(GoogleCredentials.fromStream(serviceAccountCredentials))
      .setDatabaseUrl(databaseUrl)
      .build()
    FirebaseApp.initializeApp(options)
    initialized = true
  }
}
