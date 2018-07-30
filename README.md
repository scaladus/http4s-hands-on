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

## Contribution policy

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License

This code is open source software licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).

