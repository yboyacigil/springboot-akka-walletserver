SpringBoot & Akka WalletServer
===

An experiment to show how to implement a wallet server using springboot together with akka.

Used gradle as the build tool. It nicely handles polyglot programming with the help of plugins for different languages 
and better & faster than maven.

To generate gradle wrapper:

```
gradle wrapper --gradle-version 4.10.2
``` 


## Why actors?

Each call to wallet server is round of a game play per player so we can address interactions of a
player in `GamePlayActor` instance which has its own mailbox for processing play events in the order of its
occurence.

There we need a `GamePlaySupervisor` actor which is the main contact for endpoints and forwards requests of
specific players to its corresponding actor.

Akka actor are quite scalable and in a single service instance we can have hundreds thousands of actor.

## Spock Testing Framework

Along with JUnit, I used [Spock](http://spockframework.org) to demonstrate how to write a SpringBoot integration test.
Spock has nice features that reserves to take a look!