package org.scaladus.http4shandson.persistence

import cats.effect.Effect
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.scaladus.http4shandson.model.User

class UserRepository[F[_]: Effect](private val xa: Transactor[F]) {

  def saveUser(name: String): F[User] = for {
    id <- sql"insert into user (name) values ($name)"
         .update
         .withUniqueGeneratedKeys[Int]("id")
         .transact(xa)
  } yield User(id, name)

  def allUsers: F[List[User]] =
    sql"select * from user".query[User].to[List].transact(xa)
}
