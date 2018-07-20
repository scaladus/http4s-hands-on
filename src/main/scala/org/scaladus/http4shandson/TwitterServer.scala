package org.scaladus.http4shandson

import cats.effect.IO
import fs2.{Stream, StreamApp}
import cats.syntax.all._
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object TwitterServer extends StreamApp[IO] {
  import scala.concurrent.ExecutionContext.Implicits.global

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    Stream.eval(TwitterRepository.schemaDefinition) *>
      BlazeBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .mountService(TwitterService.service, "/")
        .serve

}
