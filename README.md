#  CodeName - Duet
## Game Session Service
 

![Master Branch](https://github.com/codename-duet/game-session-service/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=codename-duet_game-session-service&metric=coverage)](https://sonarcloud.io/dashboard?id=codename-duet_game-session-service) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=codename-duet_game-session-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=codename-duet_game-session-service)


This repository contains the CodeName-Duet Player Session Manager Service.

Manages the game session of a CodeName-Duet game:
- Creates Game Sessions
- Attaches a second player to a existing Game Session
- Filter existing game sessions


## Usage

An application expose 4 REST endpoints. 

* **GET** `{baseURL}/gamesession/{sessionId}` - gets single *GameSession* details (as Json) by its `{sessionId}`
* **POST** `{baseURL}/gamesession/` - creates a new *GameSession* based on the request of the id of the first player, which is passed in the BODY of the request
* **PUT** `{baseURL}/gamesession/{sessionId}` - updates an existing *GameSession* with  the id of the second player which is passed in the body of the request
* **GET** `{baseURL}/gamesession?playerId={playerId}&filter={filter}` - gets all *GameSession* (as Json array) associated to a {playerId}, with an optional filter

If you run this application locally the `{baseUrl}` would be `http://localhost:8071`. 

All available endpoints are listed on *RestDocs UI* page which can be entered, when application is running, under *http://localhost:8071/docs/index.html* URL.

## Installation

### Run

#### Using H2 database

In order to run it use following command:

```shell script
mvn clean spring-boot:run  -Dspring-boot.run.profiles=h2
```

#### Using MySql database

Before running the application make sure that you are running MySQL database on your local machine.

In order to run it use following command:

```shell script
mvn clean spring-boot:run
```


#### Integration tests

In this project there are located several integration tests for REST endpoints during which H2 database is used. To run those tests activate Mavan `-P integration-test` profile:

```shell script
mvn clean verify -P integration-test -Dspring-boot.run.profiles=h2
```
This profile does two things — with build-helper-maven-plugin it enables Maven to find  And with maven-failsafe-plugin it enables to run integration tests.
integration test classes located in a separate directory — src/integration-test/java.

## Continuous Integration pipeline

CI pipeline with GitHub Actions:

Whenever a push/commit to the master branch, the CI pipeline triggers the following steps:

- compile the code
- test it (both unit & integrations tests)
- run static code analysis (SonarCloud)
- create & publish artifact in the GitHub Packages repository
- create & publish a Docker image with an application on the Docker Hub

In order for the CI pipeline work correctly we need the following `secrets` in GitHub Secrets. Go to GitHub project page, then Settings -> Secrets. There click on Add a new secret and add a new one.
- SONAR_TOKEN
-- Get it from https://sonarcloud.io/ project page. Go to dashboard page for your project,  click on `With other CI tools`. On a new page for a question `What is your build technology?` select `Maven`. You will get a few properties needed along with the 'sonar.login' details.
- DOCKER_USER
-- The dockerhub username
- DOCKER_REPO
-- the reponame where the docker image will be push. It should follow the pattern ``username/project_name``
- DOCKER_PASS
-- The dockerhub password

