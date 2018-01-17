package com.firebase4s.test

import java.io.InputStream

import com.firebase4s.App
import com.firebase4s.auth.Auth
import com.firebase4s.database.{Database, DatabaseReference}

object Test {
  private val serviceAccount: InputStream =
    getClass.getResourceAsStream("/firebase-service-account-key.json")
  App.initialize(serviceAccount, "https://fir-4s.firebaseio.com")
  val db: Database = Database.getInstance()
  val auth: Auth = Auth.getInstance()
}
