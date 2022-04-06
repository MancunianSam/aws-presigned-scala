import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "dev.sampalmer"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "aws-presigned-scala",
    libraryDependencies ++= Seq(
      cats,
      circe,
      ciris,
      circeGeneric,
      cloudfront,
      s3,
      scalaTest % Test
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
