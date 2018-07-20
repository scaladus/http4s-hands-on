package org.scaladus.http4shandson

import java.util.UUID
import cats.effect.IO
import cats.implicits._
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres.implicits._
import org.scaladus.http4shandson.model.{Tweet, User}

object TwitterRepository {

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:world", "postgres", ""
  )

  val schemaDefinition: IO[Unit] = List(
    sql"""
      CREATE TABLE user (
        userid uuid PRIMARY KEY,
        name VARCHAR
      )
    """.update.run,
    sql"""
      CREATE TABLE tweet (
        tweetid uuid PRIMARY KEY,
        message VARCHAR,
        userid uuid,
        FOREIGN KEY (userid) REFERENCES user(userid)
      )
    """.update.run,
    sql"""
      CREATE TABLE followers (
        followerid uuid PRIMARY KEY,
        follower uuid,
        followee uuid,
        FOREIGN KEY (follower) REFERENCES user(userid),
        FOREIGN KEY (followee) REFERENCES user(userid)
      )
    """.update.run
  ).traverse_(_.transact(xa))

  def allTweets: IO[List[Tweet]] =
    sql"select * from tweet".query[Tweet].to[List].transact(xa)


  def saveTweet(tweet: Tweet, userId: UUID): IO[Unit] =
    sql"insert into tweet (message, tweetid, userid) values (${tweet.text}, ${tweet.id}, $userId)"
      .update.run.transact(xa).void
}
