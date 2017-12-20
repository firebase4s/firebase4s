package com.firebase4s

import java.io.InputStream
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.{FirebaseApp, FirebaseOptions}


/**
  * Represents the entry point to a Firebase application.  Must be initialzed before
  * accessing the database or any other modules.
  */
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
