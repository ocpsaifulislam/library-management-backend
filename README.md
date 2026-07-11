# Library Management System

A production-ready Library Management System built with Spring Boot, Oracle Database, Flyway, Spring Security, MapStruct, and Gradle.

---

# Read Me First

The following was discovered as part of building this project:

- The original package name `dev.shoaibsuad.library-management` is invalid because Java package names cannot contain hyphens (`-`).
- This project uses the valid package name:

```text
dev.shoaibsuad.library_management
```

---

# Technology Stack

| Technology | Version |
|------------|---------|
| Java | 25 |
| Spring Boot | 4.1.0 |
| Gradle | Latest |
| Oracle Database | 19c / 21c |
| Spring Security | ✓ |
| Spring Data JPA | ✓ |
| Flyway | ✓ |
| MapStruct | 1.6.3 |
| Lombok | ✓ |
| HikariCP | ✓ |

---

# Project Structure

```text
dev.shoaibsuad.library_management
│
├── auth
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── mapper
│   ├── dto
│   └── config
│
├── book
├── author
├── category
├── borrow
├── reservation
│
├── common
│   ├── config
│   ├── exception
│   ├── security
│   ├── util
│   └── constant
│
└── LibraryManagementApplication
```

---

# Getting Started

## Clone Repository

```bash
git clone <repository-url>
```

## Build

```bash
./gradlew clean build
```

## Run

```bash
./gradlew bootRun
```

---

# Profiles

| Profile | Description |
|----------|-------------|
| dev | Development Environment |
| prod | Production Environment |

Default profile:

```yaml
spring:
  profiles:
    active: dev
```

---

# Database

Oracle Database

```
jdbc:oracle:thin:@//host:1521/librarypdb
```

---

# Flyway

Migration scripts location

```
src/main/resources/db/migration
```

Example

```
V1__initial_schema.sql
V2__create_user_table.sql
V3__insert_default_roles.sql
```

---

# Reference Documentation

For further reference, please consider the following sections:

- Official Gradle documentation
- Spring Boot Gradle Plugin Reference Guide
- Create an OCI Image

---

# Additional Links

- Gradle Build Scans – insights for your project's build

---

# Author

**Shoaib Suad**

Library Management System