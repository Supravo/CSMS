# Spring Boot Multi-Module Project for Charging Station Management System (CSMS)

This project is a Spring Boot multi-module setup that includes different modules:
- `csms-commons`: Holds all the common classes like models, utils etc.
- `authentication-service`: Service which owns the complex authentication logic.
- `transaction-service`: 


`transaction-service` will asynchronously connect to `authentication-service` through kafka to take the authentications.

The modules are built with Gradle and utilize Docker compose for kafka.

## Prerequisites

Ensure the following are installed on your system:
- [Docker](https://www.docker.com/get-started) (for running Kafka, Zookeeper, and Kafka UI)
- [Gradle](https://gradle.org/install/) (for building and running the Spring Boot application)

## Running Kafka, Zookeeper, and Kafka UI

To get the necessary services up and running, you can use Docker Compose. Follow the steps below:

1. **Start the services using Docker Compose:**

   ```bash
   docker-compose up
   ```
2. **Once docker containers are up, start the services by executing the bellow commands:**

   ```bash
   ./gradlew :authentication-service:bootRun
   ```
   ```bash
   ./gradlew :transaction-service:bootRun
   ```
3. **Once services are running, start executing authentication requests through the postman collection `CSMS API Postman Collection`.**

   
