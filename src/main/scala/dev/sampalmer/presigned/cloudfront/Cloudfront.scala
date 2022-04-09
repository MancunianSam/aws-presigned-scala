package dev.sampalmer.presigned.cloudfront

import cats.Applicative
import cats.effect.Async
import cats.implicits._
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.syntax._

import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey, SecureRandom, Signature}
import java.util.Base64

trait Cloudfront[F[_]] {
  def getSignedUrl(url: String, expiryTime: Long): F[String]
}

object Cloudfront {

  private case class Config(key: String, keyId: String)

  private case class Time(`AWS:EpochTime`: Long)

  private case class Condition(DateLessThan: Time)

  private case class Statement(Resource: String, Condition: Condition)

  private case class Policy(Statement: List[Statement])

  private def constructUrl[F[_]: Async](baseUrl: String, keyId: String, signature: String, expiry: Long): F[String] =
    Applicative[F].pure(s"$baseUrl?Expires=$expiry&Signature=$signature&Key-Pair-Id=$keyId")

  private def getSignature[F[_]: Async](key: String, url: String, expiry: Long): F[String] = Applicative[F].pure {
    val decodedCert: Array[Byte] = Base64.getDecoder.decode(key)
    val keySpec = new PKCS8EncodedKeySpec(decodedCert)
    val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    val privateKey: PrivateKey = keyFactory.generatePrivate(keySpec)
    val plaintext = Policy(Statement(url, Condition(Time(expiry))) :: Nil).asJson.printWith(Printer.noSpaces)
    val signature: Signature = Signature.getInstance("SHA1withRSA")
    signature.initSign(privateKey, new SecureRandom())
    signature.update(plaintext.getBytes())
    val signatureBytes: Array[Byte] = signature.sign
    Base64.getEncoder.encodeToString(signatureBytes)
      .replaceAll("\\+", "-")
      .replaceAll("=", "_")
      .replaceAll("/", "~")
  }

  def apply[F[_] : Async](key: String, keyId: String): Cloudfront[F] = (url: String, expiryTime: Long) => {
    for {
      expiry <- Async[F].pure((System.currentTimeMillis / 1000) + expiryTime)
      signature <- getSignature[F](key, url, expiry)
      url <- constructUrl[F](url, keyId, signature, expiry)
    }
    yield url
  }
}
