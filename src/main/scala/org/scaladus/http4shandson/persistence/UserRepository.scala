package org.scaladus.http4shandson.persistence

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.scaladus.http4shandson.model.User

object UserRepository {

  import Database.xa

  def queryUser(name: String): IO[Option[User]] =
    sql"select * from user where name = $name"
      .query[User]
      .option
      .transact(xa)

  def saveUser(name: String): IO[Option[User]] = for {
    id <- sql"insert into user (name) values ($name)"
         .update
         .withUniqueGeneratedKeys[Int]("id")
         .attemptSql
         .map(_.toOption)
         .transact(xa)
  } yield id.map(i => User(i, name))

  def allUsers: IO[List[User]] =
    sql"select * from user".query[User].to[List].transact(xa)
}
