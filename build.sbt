import Dependencies._
import sbt.Keys.publishMavenStyle
import ReleaseTransformations._

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
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // For non cross-build projects, use releaseStepCommand("publishSigned")
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

lazy val root = (project in file("."))
  .settings(
    name := "aws-presigned-scala",
    publishTo := sonatypePublishToBundle.value,
    publishMavenStyle := true,
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
