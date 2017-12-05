lazy val paradiseVersion = "2.1.0"
lazy val googleApiVersion = "1.23.0"
lazy val guavaVersion = "20.0"
lazy val firebaseVersion = "5.4.0"

lazy val common = Seq(
  organization := "com.github.adrice727",
  version := "0.0.1",
  scalaVersion := "2.12.2",
  crossScalaVersions := Seq("2.10.2", "2.10.3", "2.10.4", "2.10.5", "2.10.6", "2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6", "2.11.7", "2.11.8"),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val macros = (project in file("macros"))
  .settings(
    common,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      compilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.patch)
    )
  )

lazy val core = (project in file("core"))
  .settings(
    common,
    name := "firebase4s",
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.google.api-client" % "google-api-client" % googleApiVersion exclude("com.google.guava", "guava-jdk5"),
      "com.google.guava" % "guava" % guavaVersion,
      "com.google.firebase" % "firebase-admin" % firebaseVersion exclude("com.google.guava", "guava"),
      compilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.patch)
    )
  ) dependsOn macros

lazy val root = (project in file("."))
  .settings(
    common,
    run := run in Compile in core
  ) aggregate(macros, core)
