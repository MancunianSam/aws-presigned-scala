package dev.sampalmer.presigned.s3

import cats.Applicative
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest

import java.net.URL
import java.time.Duration

trait S3[F[_]] {
  def getPresignedUploadUrl(bucket: String, key: String, duration: Duration): F[URL]
}
object S3 {
  def apply[F[_] : Applicative](): S3[F] = (bucket: String, key: String, duration: Duration) => {
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
