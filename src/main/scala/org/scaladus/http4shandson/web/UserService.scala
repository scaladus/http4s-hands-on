package org.scaladus.http4shandson.web

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.headers.Location
import org.http4s.{HttpService, Uri}
import org.http4s.dsl.io._
import org.scaladus.http4shandson.persistence.UserRepository

object UserService {

  case class CreateUserRequest(name: String)

  case class UserResponse(name: String, _links: Map[String, Href])

  object UserResponse {
    def apply(name: String): UserResponse =
      UserResponse(name,
        Map("self" -> Href(s"/users/$name"),
            "tweets" -> Href(s"/users/$name/tweets"),
            "followers" -> Href(s"/users/$name/followers"),
            "followings" -> Href(s"/users/$name/followings")))
  }

  val service: HttpService[IO] = {
    HttpService[IO] {
      case GET -> Root / "users" =>
        Ok(UserRepository.allUsers.map(_.map(user => UserResponse(user.name))))

      case GET -> Root / "users" / name =>
        UserRepository.queryUser(name).flatMap {
          case Some(user) => Ok(user)
          case None => NotFound(s"No user found: $name".asJson)
        }

      case req @ POST -> Root / "users" => for {
        createUser <- req.as[CreateUserRequest]
        user <- UserRepository.saveUser(createUser.name)
        result <- user match {
          case Some(u) => Created(UserResponse(u.name), Location(Uri.unsafeFromString(s"/users/${u.name}")))
          case None => BadRequest("User already exists".asJson)
        }
      } yield result
    }
  }
}
