# RemindAndInform

_The service provides functionality to register reminders that are sent out at specified times._

## Getting Started

### Prerequisites

- **Java 21 or higher**
- **Maven**
- **MariaDB**
- **Git**

### Installation

1. **Clone the repository:**

```bash
git clone https://github.com/Sundsvallskommun/api-service-remindandinform.git
cd api-service-remindandinform
```

2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   *Messaging*

   - Purpose: Used to send the actual reminders.
   - Repository: https://github.com/Sundsvallskommun/api-service-messaging
   - Setup Instructions: See documentation in repository above for installation and configuration steps.
4. **Build and run the application:**

- Using Maven:

```bash
mvn spring-boot:run
```

- Using Gradle:

```bash
gradle bootRun
```

## API Documentation

Access the API documentation via:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Usage

### API Endpoints

See the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X 'GET' 'https://localhost:8080/2281/reminders/parties/11a8e3cd-89e7-4053-8dd2-a95ffa8b12c1'

```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in
`application.yml`.

### Key Configuration Parameters

- **Server Port:**

```yaml
server:
  port: 8080
```

- **Database Settings**

```yaml
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    username: <db_username>
    password: <db_password>
    url: jdbc:mariadb://<db_host>:<db_port>/<database>
  jpa:
    properties:
      jakarta:
        persistence:
          schema-generation:
            database:
              action: validate
  flyway:
    enabled: false
    baseline-on-migrate: true
```

- **Integration Settings**

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
		  messaging:
		    client-id: <client-id>
		    client-secret: <client-secret>
		    authorization-grant-type: client_credentials
		provider:
		  messaging:
		  	token-uri: <token-uri>
```

## Status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-remindandinform&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-remindandinform)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-remindandinform&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-remindandinform)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-remindandinform&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-remindandinform)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-remindandinform&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-remindandinform)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-remindandinform&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-remindandinform)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-remindandinform&metric=bugs)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-remindandinform)

## 

Copyright (c) 2023 Sundsvalls kommun
