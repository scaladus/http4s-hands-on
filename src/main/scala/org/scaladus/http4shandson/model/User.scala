package org.scaladus.http4shandson.model

import java.util.UUID

case class User(id: UUID, followers: List[User], tweets: List[Tweet])
