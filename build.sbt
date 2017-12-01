organization := "com.github.adrice727"
name := "firebase4s"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.2"

val googleApiVersion = "1.23.0"
val guavaVersion = "20.0"
val firebaseVersion = "5.4.0"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.google.api-client" % "google-api-client" % googleApiVersion exclude("com.google.guava", "guava-jdk5"),
  "com.google.guava" % "guava" % guavaVersion,
  "com.google.firebase" % "firebase-admin" % firebaseVersion exclude("com.google.guava", "guava")
)

