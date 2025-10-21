ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.4"

libraryDependencies ++= Seq(
  "io.joern" %% "joern-cli" % "4.0.436",
  "io.joern" %% "jssrc2cpg" % "4.0.436",
  "com.google.guava" % "guava" % "33.0.0-jre",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)