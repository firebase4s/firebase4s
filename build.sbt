import sbt.Keys.{developers, homepage, licenses, scmInfo, _}
import xerial.sbt.Sonatype.autoImport.sonatypeProfileName

lazy val paradiseVersion = "2.1.1"
lazy val googleApiVersion = "1.30.9"
lazy val guavaVersion = "29.0-jre"
lazy val firebaseVersion = "6.12.2"
lazy val circeVersion = "0.9.3"

lazy val commonSettings = Seq(
  organization := "com.github.firebase4s",
  version := "0.0.4",
  scalaVersion := "2.13.1",
  crossScalaVersions := Seq(
    "2.11.12",
    "2.12.11"
  )
)

lazy val core = (project in file("core"))
  .dependsOn(macros)
  .settings(
    commonSettings,
    name := "firebase4s",
    publishArtifact in Test := false,
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.google.api-client" % "google-api-client" % googleApiVersion exclude("com.google.guava", "guava-jdk5"),
      "com.google.guava" % "guava" % guavaVersion,
      "com.google.firebase" % "firebase-admin" % firebaseVersion exclude("com.google.guava", "guava"),
      "com.chuusai" %% "shapeless" % "2.3.3"
    ),
    publishTo := Some(
      if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
    ),
    homepage := Some(url("https://github.com/firebase4s/firebase4s")),
    scmInfo := Some(
      ScmInfo(url("https://github.com/firebase4s/firebase4s"), "git@github.com:firebase4s/firebase4s.git")
    ),
    developers := List(Developer("username", "Aaron Rice", "adrice727@gmail.com", url("https://github.com/adrice727"))),
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    sonatypeProfileName := "com.github.firebase4s",
    publishMavenStyle := true,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    )
  )

lazy val macros = (project in file("macros"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    ),
    scalacOptions += "-language:experimental.macros"
  )

lazy val test = (project in file("test"))
  .dependsOn(macros % Test)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.1.1",
      "org.scalatest" %% "scalatest" % "3.1.1" % "test",
      "com.chuusai" %% "shapeless" % "2.3.3"
    ),
    publish := {},
    publishLocal := {},
    publishArtifact := false
  ) dependsOn core
