package org.scaladus.http4shandson.web

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.headers.Location
import org.http4s.{HttpService, Uri}
import org.http4s.dsl.io._
import org.scaladus.http4shandson.persistence.UserRepository

object UserService {

  case class CreateUserRequest(name: String)

  case class UserResponse(name: String, _links: Map[String, Href])

  object UserResponse {
    def apply(id: Int, name: String): UserResponse =
      UserResponse(name,
        Map("self" -> Href(s"/users/$id"),
            "tweets" -> Href(s"/users/$id/tweets"),
            "followers" -> Href(s"/users/$id/followers"),
            "followings" -> Href(s"/users/$id/followings")))
  }

  val service: HttpService[IO] = {
    HttpService[IO] {
      case GET -> Root / "users" =>
        Ok(UserRepository.allUsers.map(_.map(user => UserResponse(user.id, user.name))))

      case req @ POST -> Root / "users" => for {
        createUser <- req.as[CreateUserRequest]
        user <- UserRepository.saveUser(createUser.name)
        result <- Created(UserResponse(user.id, user.name), Location(Uri.unsafeFromString(s"/users/${user.id}")))
      } yield result
    }
  }
}
