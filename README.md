#  CodeName - Duet
## Game Session Service
 

![Master Branch](https://github.com/codename-duet/game-session-service/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=codename-duet_game-session-service&metric=coverage)](https://sonarcloud.io/dashboard?id=codename-duet_game-session-service) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=codename-duet_game-session-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=codename-duet_game-session-service)


This repository contains the CodeName-Duet Player Session Manager Service.



TODO: Add links to: 
sonar
restdocs
where is exposed

Build with Integration tests

-Pintegration-test option was added to Maven CLI command mvn -B clean verify -Pintegration-test.
Basically it tells Maven to run the same mvn verify command but with additional Maven profile that has an additional dependency. 
And therefore, in order to make it running correctly, we need to add an integration-test profile to the pom.xml file.

This profile does two things — with build-helper-maven-plugin it enables Maven to find integration test classes located in a 
seperate directory — src/integration-test/java. And with maven-failsafe-plugin it enables to run integration tests.