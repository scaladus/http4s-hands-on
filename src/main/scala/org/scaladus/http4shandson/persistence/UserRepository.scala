package org.scaladus.http4shandson.persistence

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.scaladus.http4shandson.model.User

object UserRepository {

  import Database.xa

  def saveUser(name: String): IO[User] = for {
    id <- sql"insert into user (name) values ($name)"
         .update
         .withUniqueGeneratedKeys[Int]("id")
         .transact(xa)
  } yield User(id, name)

  def allUsers: IO[List[User]] =
    sql"select * from user".query[User].to[List].transact(xa)
}
