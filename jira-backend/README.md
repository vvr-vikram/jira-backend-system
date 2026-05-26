# Jira-Like Task Management Backend System

## Overview
This is a Spring Boot 3 enterprise backend assignment representing a Jira-like task management tool.
It allows users to create projects, manage tasks, assign them, add comments, and track the history/activity of tasks.

## Technology Stack
- **Framework**: Spring Boot 3
- **Language**: Java 17
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Other tools**: Lombok, Maven, Bean Validation

## Architecture
The application follows a standard layered architecture:
- `Controller`: REST endpoints handling HTTP requests and responses.
- `Service`: Business logic encapsulation.
- `Repository`: Database operations using Spring Data JPA.
- `DTO`: Data Transfer Objects for API request/response decoupling from entities.
- `Entity`: JPA Entities mapping to MySQL tables.
- `Exception`: Global exception handling using `@ControllerAdvice`.

## Setup and Execution Steps

### Prerequisites
1. **Java 17** installed.
2. **Maven** installed (or use the provided `mvnw` wrapper).
3. **MySQL** installed and running on default port `3306`.

### Database Configuration
1. Make sure you have a MySQL server running with username `root` and password `1254`.
2. The application will automatically create the database `jira_db` if it doesn't exist.
3. Hibernate will automatically generate the schema tables on startup (`spring.jpa.hibernate.ddl-auto=update`).

### Running the Application
1. Open a terminal or command prompt in the project root directory.
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
   Or using the wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The server will start on `http://localhost:8080`.

### API Documentation
- Access the Swagger UI for interactive API documentation:
  `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON format:
  `http://localhost:8080/v3/api-docs`

### Testing APIs with Postman
A `postman_collection.json` file is included in the project root. You can import this into Postman to quickly test all the available endpoints.
To test a flow:
1. Create a User (POST `/users`)
2. Create a Project using the user ID (POST `/projects`)
3. Create a Task (POST `/tasks`)
4. Update Task Status (PUT `/tasks/{id}`)
5. Get Dashboard Metrics (GET `/tasks/metrics/{projectId}`)
