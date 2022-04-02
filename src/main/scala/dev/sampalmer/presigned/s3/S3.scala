package dev.sampalmer.presigned.s3

import cats.Applicative
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest

import java.net.URL
import java.time.Duration

object S3 {

  def getPresignedUploadUrl[F[_]: Applicative](bucket: String, key: String): F[URL] = {
    val s3Client = S3Presigner.builder.region(Region.EU_WEST_2).build
    val putObjectRequest = PutObjectRequest.builder
      .bucket(bucket)
      .key(key)
      .build
    val request = PutObjectPresignRequest.builder
      .signatureDuration(Duration.ofMinutes(10))
      .putObjectRequest(putObjectRequest)
      .build
    Applicative[F].pure(s3Client.presignPutObject(request).url())
  }
}

