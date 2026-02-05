# Blog Manager


***


[![Java CI with Maven](https://github.com/danyalrahujo/blogmanager/actions/workflows/maven.yml/badge.svg)](https://github.com/danyalrahujo/blogmanager/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Coverage Status](https://coveralls.io/repos/github/danyalrahujo/blogmanager/badge.svg?branch=master)](https://coveralls.io/github/danyalrahujo/blogmanager?branch=master)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=bugs)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=coverage)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=danyalrahujo_blogmanager&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=danyalrahujo_blogmanager)

***



A Java-based blog management system built with Apache Maven and MongoDB, supporting command-line and Swing-based interaction.  
This project strongly emphasizes software quality, using TDD, integration & E2E testing, mutation testing, and static analysis with automated CI.

##  Key Features

-  CRUD operations for blog posts
-  Command-line interface using Picocli
-  Swing UI for desktop interaction
-  MongoDB integration (real + embedded for tests)
-  Unit, Integration, and End-to-End tests
-  Mutation testing with PIT
-  Code coverage enforcement with JaCoCo
-  Static code analysis with Sonar
-  CI pipeline using GitHub Actions (Maven)

##  Tech Stack

| Category | Technology |
|---------|------------|
| Language | Java |
| Build Tool | Maven |
| Database | MongoDB |
| CLI | Picocli |
| UI | Java Swing |
| Unit Testing | JUnit 4 |
| Mocking | Mockito |
| UI Testing | AssertJ Swing |
| Integration Testing | Testcontainers |
| Async Testing | Awaitility |
| Coverage | JaCoCo |
| Mutation Testing | PIT |
| CI | GitHub Actions |
| Static Analysis | Sonar |
| Containers (Tests) | Docker (via Maven plugin) |

##  Testing Strategy

This project follows a multi-layer testing approach:

- Unit Tests – Core logic validation
- Integration Tests (IT) – MongoDB-backed tests
- End-to-End Tests (E2E) – Full workflow validation
- Mutation Tests – Ensure tests actually catch defects

Test sources are organized as:

    src/test/java
    src/it/java
    src/e2e/java

##  MongoDB for Tests

MongoDB is provided in different ways depending on the test type:

- Embedded MongoDB for lightweight tests
- Testcontainers for realistic integration tests
- Docker (via Maven) automatically starts MongoDB before integration tests and stops it afterward

No manual database setup is required.

##  Getting Started

### Prerequisites

- Java 11+
- Maven
- Docker (required for integration & E2E tests)

### Clone the Repository

    git clone https://github.com/danyalrahujo/blogmanager.git
    cd blogmanager

### Build the Project

    mvn clean install

### Run Tests Only

    mvn test

### Run Integration & E2E Tests

    mvn verify
    
### Run Tests with JaCoCo

    mvn verify -Pjacoco

##  Code Quality & Coverage

- JaCoCo enforces 100% line coverage
- Sonar rules are customized to ignore selected Swing-related rules

##  Continuous Integration

The GitHub Actions workflow located at `.github/workflows/maven.yml` automatically:

- Builds the project
- Runs all test phases
- Fails the build if quality gates are not met

This ensures consistent quality on every push and pull request.

##  Acknowledgments

- JUnit, Mockito, AssertJ – Testing frameworks
- Testcontainers – Realistic integration testing
- PIT – Mutation testing
- JaCoCo & Sonar – Code quality and coverage tools
- MongoDB – Data persistence
