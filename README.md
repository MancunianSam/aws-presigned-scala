# AWS Presigned Scala
aws-presigned-scala is published to Maven Central and is built for Scala 2.13
It provides methods for producing [signed urls for AWS S3] and [Cloudfront]
## Quick Start

`"dev.sampalmer" %% "aws-presigned-scala" % "0.1.0"`

```scala
import dev.sampalmer.presigned.s3.S3._
import dev.sampalmer.presigned.cloudfront.Cloudfront.getSignedUrl
import java.time.Duration
import cats.effect.IO
    
getSignedUrl[IO]("https://cloudfront-url-to-sign", 3600)
getPresignedUploadUrl[IO]("bucket", "key", Duration.ofMinutes(10))
```

[signed urls for AWS S3]: https://docs.aws.amazon.com/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html
[Cloudfront]: https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-signed-urls.html