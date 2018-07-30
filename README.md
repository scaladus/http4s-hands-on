# http4s-hands-on

Welcome to http4s-hands-on!

## Prerequisites

 - Scala
 - sbt

## Running the Application

There are several ways to start the application:

 1. From inside the IDE
 2. Using the sbt-revolver plugin
 3. Building an executable jar

### Using the IDE

You can start the application for development by simply staring the main class `TwitterServer` from inside your IDE.

### Using sbt-revolver plugin

The sbt-revolver plugin provides two tasks to start and start the application as a background process:

```
sbt reStart
sbt reStop
```

### Building an executable jar

You can use build an executable jar using the sbt-assembly plugin. The executable jar can then be started using the scala cli:

```
sbt assembly
scala target/scala-2.12/http4s-hands-on-assembly-0.0.1-SNAPSHOT.jar
```

## Assignments

Your assigment is to implement Twitter! :o)

Currently only the user resource is partially implemented:

```
POST http://localhost:8080/users

{
  "name":"BenediktRitter"
}

HTTP/1.1 201 Created
Content-Type: application/json
Location: /users/1
Date: Mon, 30 Jul 2018 06:38:26 GMT
Content-Length: 185

{
  "name": "BenediktRitter",
  "_links": {
    "self": {
      "href": "/users/1"
    },
    "tweets": {
      "href": "/users/1/tweets"
    },
    "followers": {
      "href": "/users/1/followers"
    },
    "followings": {
      "href": "/users/1/followings"
    }
  }
}

Response code: 201 (Created); Time: 507ms; Content length: 185 bytes
```

```
GET http://localhost:8080/users

HTTP/1.1 200 OK
Content-Type: application/json
Date: Mon, 30 Jul 2018 06:39:02 GMT
Content-Length: 187

[
  {
    "name": "BenediktRitter",
    "_links": {
      "self": {
        "href": "/users/1"
      },
      "tweets": {
        "href": "/users/1/tweets"
      },
      "followers": {
        "href": "/users/1/followers"
      },
      "followings": {
        "href": "/users/1/followings"
      }
    }
  }
]
```

### The users resource

 1. Currently the API uses the internal ID to identify users. Change this to make the user name the identifier. Think about the data base schema. Does it have to change as well?
 2. Implement a GET for a single user.

### Followers and following

An integral part of Twitter is following other people.

 1. Implement an end point for following other users. This can be done in several ways, e.g. a POST request to the users followers subresource or as a separate top level resource.
 2. Add the followers and following collections to the user resource. 

### Tweets and Retweets

Now we need tweets...

 1. Add the possibility to post a tweet. A tweet must be at most 140 characters long.
 2. Users should be able to retweet interesting tweets of other users. A tweet can be retweeted several times by different users, but a user can retweet a single tweet only once.

### Timeline and Hashtags

Now that we have followers, tweets and retweets in place we can implement the timeline for each user:

 1. The timeline for a user consists of the tweets of the user and the users they are following as well as all their retweets. The timeline is ordered chronologically.
 2. Implement hashtags! Whenever a user tweets something containing a hashtag, this will create a new timeline for this hashtag.

### User management

Currently anybody can send tweets to any timeline. Implement authentication and authorization so that each user can only sends tweets to their own timeline.

## Contribution policy

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License

This code is open source software licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).

