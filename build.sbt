ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.0"

lazy val root = (project in file("."))
  .settings(
    name := "Iris"
  )
Compile / unmanagedSourceDirectories += baseDirectory.value / "venv"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.4.0",
  "org.apache.spark" %% "spark-sql" % "3.4.0",
  "org.apache.spark" %% "spark-mllib" % "3.4.0",

  "org.scalanlp" %% "breeze" % "2.1.0",

  "org.scalanlp" %% "breeze-natives" % "2.1.0",

  "org.scalanlp" %% "breeze-viz" % "2.1.0",
  "joda-time" % "joda-time" % "2.12.5",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.slf4j" % "slf4j-api" % "2.0.5",
  "org.slf4j" % "slf4j-simple" % "2.0.5",
)

