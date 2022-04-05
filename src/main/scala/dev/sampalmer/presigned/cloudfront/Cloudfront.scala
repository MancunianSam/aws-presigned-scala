package dev.sampalmer.presigned.cloudfront

import cats.Applicative
import cats.effect.kernel.{Async, Sync}
import cats.implicits._
import ciris._
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.syntax._

import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey, SecureRandom, Signature}
import java.util.Base64

object Cloudfront {

  private case class Config(key: String, keyId: String)

  private case class Time(`AWS:EpochTime`: Long)

  private case class Condition(DateLessThan: Time)

  private case class Statement(Resource: String, Condition: Condition)

  private case class Policy(Statement: List[Statement])

  private def config[F[_] : Async]: ConfigValue[F, Config] = (env("PRIVATE_KEY").as[String], env("KEY_ID").as[String]).parMapN({
    (key, keyId) => Config(key, keyId)
  })

  /**
   * @param url The cloudfront url including path parameters
   * @param expiryTime The expiry time for the signed url
   * @tparam F[_]
   * @return F[URL]
   * @example val url = getSignedUrl[IO]("https://cloudfront", 3600)
   */
  def getSignedUrl[F[_] : Async](url: String, expiryTime: Long): F[String] =
    for {
      config <- config.load[F]
      expiry <- Sync[F].pure((System.currentTimeMillis / 1000) + expiryTime)
      signature <- getSignature[F](config.key, url, expiry)
      url <- constructUrl[F](url, config.keyId, signature, expiry)
    }
    yield url

  private def constructUrl[F[_] : Applicative](baseUrl: String, keyId: String, signature: String, expiry: Long): F[String] =
    Applicative[F].pure(s"$baseUrl?Expires=$expiry&Signature=$signature&Key-Pair-Id=$keyId")

  private def getSignature[F[_] : Applicative](key: String, url: String, expiry: Long): F[String] = Applicative[F].pure {
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
}
