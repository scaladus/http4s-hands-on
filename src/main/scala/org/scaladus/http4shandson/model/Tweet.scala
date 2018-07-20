package org.scaladus.http4shandson.model

import java.util.UUID
import cats.effect.IO

case class Tweet private(id: UUID, text: String)

object Tweet {
  def apply(text: String): IO[Tweet] =
    IO(UUID.randomUUID()).map(id => new Tweet(id, text))
}
