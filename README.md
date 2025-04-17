# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.
[Watch the gameplay demo](https://youtu.be/PcTNly5etfk)

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[Chess Server Design (Sequence Diagram)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1TlHUoPUKHxgVKw4C+1LGiWmrWs06WmDW20+v0OsHq3KnQEXMoRKFIqCRVRKrCp4EywpHS4wa4dO6TcqrJ6Bv31CAAa3QVam+0dlCL8GQ5nKACYnE5uuWhmKxjBq48pnXUg3m2hW6sDugOKYvL5-AFoOxyTAADIQaJJAJpDJZHt5YvFUvVOpNVoGdQJNDDqai0a7L4vN4fVYwA4liChQFqWPRvhWY6fk835LJ+-6YCBQFFGqKDlAgh48rCB5Hqi6KxNiiaGC6YZuh6uqZAatIQaMJpEuGFocjA3K8nGwbaEKIrUWIrrSsmV4oeUrEJnKREEiRLJkV6mSsQGQYhjx5qRoxMbRnJ2iOs6KbnMC5TYTyOZ5gh2mdnxyElFc4Gjh+ny1kGc4tp88GAV22S9jAA5Dr0lm3JBNnTnZTYOX+y6cGu3h+IEXgoOg+6Hr4zAnukmSYK5l5mTe0gAKJ7pl9SZc0LSPqoz7dDO9loB2SGIeUZWBRViFsoRaFxb6WEtWAuEYgRIkauJpIwOSVI0u+KC0UyboMeUzGxmp8gcTAI2hnRE2mU1qnxuphG9ctElGCg3DSUGsJCdoY1mhGhRRtI+0UoYJ3yBpSZaUCpbYfFBkIPmxkgrK1CgQB14-YUqVgP2g7DgcpgrmFG6BJCtp7tCMAAOJjqyiVnilF7ML95mVMjuUFfYY6lQF86VWy1UwLV5MNbj4IwMgsSo6MqiwizKCI7EnX4Y9okyH17qM9CHNs2d9FKVNPK2hzDoKRG9PyijY7CShXZUxzXMdWohl07joFTMTrPjJU-RGygACS0gmwAjH2ADMAAsTynhRXFQTA3R9DoCCgI2BqVn5fTmwAcmO-wwI0AN-UD3Y5KD7ng15wdoybFRm2OVu2w7ztTK7+ru35Xs+37Ae+TWhtjmHowR1HUOhZ44WbtgPhQNg3DwORhgcykSXnvHjWA+Ut4NETJPBGT6DDqHY7R8cz1pjVk8vr0M+jAC32DwJwvM2j7NjnAXc81ifPbeNu2DRzsJr6NS3n4pl2MdNyujHLgtdmtirKqfxE7f1np6ivjfcWK1H6CRtC-FAb8-4K34k6JWstNo9VMhrA+R8dafSMi9WOgELIp1GFncodsnZOUBi5bGYNPJgXwZba2RCc7wXrquRusMAiWH2uhZIMAABSEAeSQMCCXEAjYsYD31umaolJ7wtHNqTesdVhzt2AOwqAcAIDoSgLMc2Vs54mWAt9Je8j5yKN9iotRGitGZ2kBvbBW94GoRgAAKz4WgK+HNeH6RQGiLqP8xIwLJBSIBVcxwgN4mApi0tIHQPvrAsyDNEEPS2r-GJ5Q-BaEyEEgh0hZhKLMeo6AoSH7snKJSbA6Tu4qyQWrFBBjIEeLQB9L6tjVpDzLLo2OINKEQyYTDCKAQvDKO7F6WAwBsDt0IPERIvdMYgzsRlbKuV8qtGMBTBeOk9oHTEHTOBDMQDcDwIyPQBhYQcBupkY+3Vql+JSTAPZwzDn6BQLCQpF1ikyDOYYYAyoBaJOQWs0siB7naCOSgRpWC0wf1aT0dp5D45dN6CFVcQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
