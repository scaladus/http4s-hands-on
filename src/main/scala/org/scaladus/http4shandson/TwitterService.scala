package org.scaladus.http4shandson

import cats.effect.IO
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._
import java.util.UUID
import org.scaladus.http4shandson.model.{Tweet, User}

object TwitterService {

  val service: HttpService[IO] = {
    HttpService[IO] {
      case GET -> Root / "tweets"  =>
        Ok(TwitterRepository.allTweets)

      case req @ POST -> Root / "users" / userId / "tweets" => for {
        msg <- req.as[String]
        tweet <- Tweet(msg)
        _ <- TwitterRepository.saveTweet(tweet, UUID.fromString(userId))
        result <- Created(tweet)
      } yield result

    }
  }
}
