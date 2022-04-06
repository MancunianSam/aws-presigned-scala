import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "dev.sampalmer"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/MancunianSam/aws-presigned-scala"),
    "git@github.com:MancunianSam/aws-presigned-scala.git"
  )
)
developers := List(
  Developer(
    id    = "MancunianSam",
    name  = "Sam Palmer",
    email = "github@sampalmer.dev",
    url   = url("https://github.com/MancunianSam/aws-presigned-scala")
  )
)

ThisBuild / description := "A library for presigning S3 and Cloudfront URLS"
ThisBuild / licenses := List("MIT" -> new URL("https://choosealicense.com/licenses/mit/"))
ThisBuild / homepage := Some(url("https://github.com/MancunianSam/aws-presigned-scala"))


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
