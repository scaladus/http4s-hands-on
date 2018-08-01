package org.scaladus.http4shandson.persistence

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor

object Database {

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.h2.Driver", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", ""
  )

  val schemaDefinition: IO[Unit] = List(
    sql"""
      CREATE TABLE user (
        id   INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR
      )
    """.update.run,
    sql"""
      CREATE TABLE tweet (
        id      INT AUTO_INCREMENT PRIMARY KEY,
        text    VARCHAR,
        user_id INT,
        FOREIGN KEY (user_id) REFERENCES user(id)
      )
    """.update.run,
    sql"""
      CREATE TABLE followers (
        user_id     INT,
        follower_id INT,
        FOREIGN KEY (user_id) REFERENCES user(id),
        FOREIGN KEY (follower_id) REFERENCES user(id),
        PRIMARY KEY (user_id, follower_id)
      )
    """.update.run
  ).traverse_(_.transact(xa))

}
