# Playground Java

![sdk]

This repository contains code written to demonstrate libraries and frameworks targeting Java.

## How to run

| Description | Command |
|:---|:---|
| Run tests of all projects | `./mvnw test` |
| Run tests of one project | `./mvnw -pl :project test` |
| List all projects | `./mvnw help:evaluate -Dexpression=project.modules` |
| Update dependencies | `./mvnw versions:use-latest-releases` |

[sdk]: https://img.shields.io/badge/JDK-14-5481A0.svg "Java Development Kit 14"
