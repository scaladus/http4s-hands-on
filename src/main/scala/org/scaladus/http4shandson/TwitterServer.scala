package org.scaladus.http4shandson

import cats.effect.{Effect, IO}
import fs2.{Stream, StreamApp}
import cats.syntax.all._
import org.http4s.server.blaze.BlazeBuilder
import org.scaladus.http4shandson.persistence.{Database, UserRepository}
import org.scaladus.http4shandson.web.UserService

import scala.concurrent.ExecutionContext

object TwitterServer extends StreamApp[IO] {
  import scala.concurrent.ExecutionContext.Implicits.global

  def stream(args: List[String], requestShutdown: IO[Unit]) = ServerStream.stream[IO]

}

object ServerStream {
  def database[F[_]: Effect] = new Database[F]

  def userService[F[_]: Effect] = new UserService[F].service(new UserRepository[F](database.xa))

  def stream[F[_]: Effect](implicit ec: ExecutionContext) =
    Stream.eval(database.schemaDefinition) *>
      BlazeBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .mountService(userService, "/")
        .serve
}
