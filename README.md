# Library Management System

A production-ready **Library Management System** built with **Spring Boot**, **Oracle Database**, **Spring Security**, **Flyway**, **MapStruct**, and **Gradle** following modern backend development best practices.

---

## Technology Stack

| Technology                     | Version   |
|--------------------------------|-----------|
| Java                           | 25        |
| Spring Boot                    | 4.1.0     |
| Gradle                         | Latest    |
| Oracle Database                | 19c / 21c |
| Spring Data JPA                | ✓         |
| Spring Security                | ✓         |
| Flyway                         | ✓         |
| Springdoc OpenAPI (Swagger UI) | 2.x       |
| MapStruct                      | 1.6.3     |
| Lombok                         | ✓         |
| HikariCP                       | ✓         |

---

## Features

- User Authentication & Authorization
- RESTful APIs
- Spring Security Integration
- Oracle Database Integration
- Database Versioning with Flyway
- Interactive API Documentation (Swagger UI)
- DTO Mapping using MapStruct
- Bean Validation
- Global Exception Handling
- Connection Pooling with HikariCP
- Environment-based Configuration
- Clean Layered Architecture

---

## Project Structure

```text
src
└── main
    ├── java
    │   └── dev.shoaibsuad.library_management
    │       ├── auth
    │       │   ├── controller
    │       │   ├── service
    │       │   ├── repository
    │       │   ├── entity
    │       │   ├── dto
    │       │   ├── mapper
    │       │   └── config
    │       │
    │       ├── author
    │       ├── book
    │       ├── category
    │       ├── borrow
    │       ├── reservation
    │       │
    │       ├── common
    │       │   ├── config
    │       │   ├── constant
    │       │   ├── exception
    │       │   ├── security
    │       │   └── util
    │       │
    │       └── LibraryManagementApplication
    │
    └── resources
        ├── db
        │   └── migration
        ├── application.yml
        ├── application-dev.yml
        └── application-prod.yml
```

---

## Prerequisites

Before running the project, ensure the following software is installed:

- Java 25
- Gradle
- Oracle Database 19c or later
- Git

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/ocpsaifulislam/library-management-backend.git
cd library-management-backend
```

### Build the Project

```bash
./gradlew clean build
```

### Run the Application

```bash
./gradlew bootRun
```

---

## Configuration

Configure your Oracle database connection in:

```text
src/main/resources/application-dev.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@//localhost:1521/librarypdb
    username: lms
    password: lms
```

---

## Active Profiles

| Profile | Description             |
|---------|-------------------------|
| dev     | Development Environment |
| prod    | Production Environment  |

Default:

```yaml
spring:
  profiles:
    active: dev
```

---

## Database Migration

Flyway manages all database schema changes.

Migration directory:

```text
src/main/resources/db/migration
```

Example:

```text
V1__initial_schema.sql
V2__create_user_table.sql
V3__insert_default_roles.sql
```

---

## API Documentation

Swagger UI is available after starting the application.

| Service      | URL                                         |
|--------------|---------------------------------------------|
| Swagger UI   | http://localhost:9990/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:9990/v3/api-docs           |

> Replace the port if your application is configured differently.

---

## Build Commands

```bash
./gradlew clean
./gradlew build
./gradlew test
./gradlew bootRun
```

---

## Architecture

This project follows a layered architecture:

```text
Controller
     │
Service
     │
Repository
     │
Oracle Database
```

Additional components:

- DTO
- Mapper (MapStruct)
- Security
- Validation
- Exception Handling
- Flyway Migration

---

## Package Naming

Java package names cannot contain hyphens (`-`).

This project uses the following valid package structure:

```text
dev.shoaibsuad.library_management
```

---

## Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push your branch.
5. Open a Pull Request.

---

## License

This project is licensed under the MIT License.

---

## Author

**Saiful Islam**

Software Engineer

GitHub: https://github.com/ocpsaifulislam