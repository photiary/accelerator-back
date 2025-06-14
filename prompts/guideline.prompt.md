# Junie Guidelines for Spring Boot Development

This file contains guidelines for Junie to follow when working on this Spring Boot project.

## Core Technologies & Versions

- **Java:** Use the latest Java 21.
- **Spring Boot:** Always use the latest stable release of Spring Boot 3.x (or the latest major
  stable version).
- **Build Tool:** Use Gradle as the build tool. Ensure the `build.gradle` usees the latest stable
  Spring Boot package.
- **Unit test:** Use junit
- **Log:** Use SLF4J as the logging API

## Project Structure

* **MSA Architecture**

* **Domain Driven Design**

* **Packaging:** Strongly prefer **package-by-feature** structure over package-by-layer.

    * **Why package-by-feature?** It improves modularity, makes navigating the code related to a
      single feature easier, and makes it easier to reuse code across features.

    * **Example:**

      **PREFER THIS (Package-by-Feature):** `{Domain}` is a feature.

      ```
      com.funa
      ├─ {Domain}                         # Feature: Domain
      │  ├─ {Domain}Controller.java       # Controller for Domain
      │  ├─ {Domain}Service.java          # Service logic for Domain
      │  ├─ {Domain}Repository.java       # Data access for Domain
      │  ├─ {Domain}.java                 # Domain/Entity for Domain
      │  └─ dto                           # Data Transfer Objects specific to Domain
      │     ├─ {Domain}Request.java
      │     └─ {Domain}Response.java
      └─ common                           # Optional: Truly shared utilities/conifg
         ├─ exception
         └─ config 
      ```

      **AVOID THIS (Package-by-Layer):**
      ```
      com.funa
      ├─ controller
      │  └─ {Domain}Controller.java
      ├─ service
      │  └─ {Domain}Service.java
      ├─ repository
      │  └─ {Domain}Repository.java
      └─ model (or domain/entity)
         └─ {Domain}.java
      ```

## Data Access

* **Default:** If unsure, lean towards Spring Data JPA for typical applications development.

## Java Language Features

* **Data Carriers:** Use **Lombok** for immutable data transfer objects (DTOs). value objects (VOs).

## Database

* **Table naming rules:** Use **Snake case** and prefix it with 'tb_'.

## Unit Test

## Logs

* Log output as JSON format.
* **Log content:** timestamp, log level, transaction ID(Trace ID), user ID
