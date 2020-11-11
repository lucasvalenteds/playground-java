# Playground Java

![sdk]

This repository contains code written to demonstrate libraries and frameworks targeting Java.

| Description | Command |
|:---|:---|
| Run tests of all projects | `./mvnw test` |
| Run tests of one project | `./mvnw -pl :project test` |
| List all projects | `./mvnw help:evaluate -Dexpression=project.modules` |
| Update dependencies | `./mvnw versions:use-latest-releases` |
| Install browser drivers | `./mvnw -pl :seleniumhq_selenium webdriverextensions:install-drivers` |

[sdk]: https://img.shields.io/badge/jdk-14-5481A0.svg "Java Development Kit 14"
