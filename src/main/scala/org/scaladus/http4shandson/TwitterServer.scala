package org.scaladus.http4shandson

import cats.effect.IO
import fs2.{Stream, StreamApp}
import cats.syntax.all._
import org.http4s.server.blaze.BlazeBuilder
import org.scaladus.http4shandson.persistence.Database
import org.scaladus.http4shandson.web.UserService

import scala.concurrent.ExecutionContext

object TwitterServer extends StreamApp[IO] {
  import scala.concurrent.ExecutionContext.Implicits.global

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    Stream.eval(Database.schemaDefinition) *>
      BlazeBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .mountService(UserService.service, "/")
        .serve

}
