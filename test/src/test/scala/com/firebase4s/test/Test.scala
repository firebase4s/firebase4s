package com.firebase4s.test

import java.io.{ByteArrayInputStream, InputStream}

import com.firebase4s.App
import com.firebase4s.auth.Auth
import com.firebase4s.database.Database

import scala.util.Random

object Test {

  private val serviceAccount: InputStream =
    Option(System.getenv("FIREBASE4S_TEST_SERVICE_ACCOUNT"))
      .map(_.getBytes)
      .map(new ByteArrayInputStream(_))
      .getOrElse(getClass.getClassLoader.getResourceAsStream("firebase-service-account-key.json"))

  App.initialize(serviceAccount, "https://fir-4s-b565b.firebaseio.com")

  val randomEmail: () => String = () => s"${Random.alphanumeric.take(10).mkString}@firebase4s.com".toLowerCase
  val randomPhone: () => String = () => s"+1${(for (i <- 1 to 10) yield Random.nextInt(9) + 1).mkString}"

  val db: Database = Database.getInstance()
  val auth: Auth = Auth.getInstance()
}
