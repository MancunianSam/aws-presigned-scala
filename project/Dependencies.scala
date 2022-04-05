import sbt._

object Dependencies {
  lazy val cats = "org.typelevel" %% "cats-core" % "2.7.0"
  lazy val s3 = "software.amazon.awssdk" % "s3" % "2.17.162"
  lazy val cloudfront = "software.amazon.awssdk" % "cloudfront" % "2.17.159"
  lazy val ciris = "is.cir" %% "ciris" % "2.3.2"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11"
  lazy val circe = "io.circe" %% "circe-core" % "0.14.1"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.14.1"
}
