import sbt._

object Dependencies {
  lazy val cats = "org.typelevel" %% "cats-core" % "2.7.0"
  lazy val s3 = "software.amazon.awssdk" % "s3" % "2.17.159"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11"
}
