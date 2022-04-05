package dev.sampalmer.presigned.s3

import cats.Applicative
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest

import java.net.URL
import java.time.Duration

object S3 {
  /**
   * @param bucket An S3 bucket
   * @param key The object key
   * @tparam F[_]
   * @return F[URL]
   * @example val url = getPresignedUploadUrl[IO]("bucket", "key")
   */
  def getPresignedUploadUrl[F[_] : Applicative](bucket: String, key: String, duration: Duration): F[URL] = {
    val putObjectRequest = PutObjectRequest.builder()
      .bucket(bucket)
      .key(key)
      .build
    val request = PutObjectPresignRequest.builder
      .signatureDuration(duration)
      .putObjectRequest(putObjectRequest)
      .build
    Applicative[F].pure(S3Presigner.create().presignPutObject(request).url())
  }

}

