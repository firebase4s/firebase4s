package com.firebase4s.test

import java.io.InputStream

import com.firebase4s.App
import com.firebase4s.auth.Auth
import com.firebase4s.database.Database
import scala.util.Random

object Test {
  private val serviceAccount: InputStream =
    getClass.getResourceAsStream("/firebase-service-account-key.json")
  App.initialize(serviceAccount, "https://fir-4s.firebaseio.com")

  val randomEmail: () => String = () => s"${Random.alphanumeric.take(10).mkString}@firebase4s.com".toLowerCase
  val randomPhone: () => String = () => s"+1${(for (i <- 1 to 10) yield Random.nextInt(9) + 1).mkString}"
  val db: Database = Database.getInstance()
  val auth: Auth = Auth.getInstance()
}
