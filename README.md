# Task Management System - Spring Boot REST API

A complete Spring Boot RESTful API for a Task Management System with User authentication, Project management, and Task assignment features.

## 🎯 Features

- **User Authentication**: JWT-based authentication with role-based authorization
- **User Management**: Register, login, and user profile management
- **Project Management**: Create, read, update, and delete projects (MANAGER role only)
- **Task Management**: Create, read, update, and delete tasks with status tracking
- **Task Assignment**: Assign tasks to users and track their progress
- **Status Workflow**: TODO → IN_PROGRESS → DONE status transitions
- **Input Validation**: Comprehensive validation with custom error messages
- **Exception Handling**: Global exception handling with standardized API responses
- **Security**: Spring Security with JWT tokens and role-based access control

## 📋 Tech Stack

- **Java 17**
- **Spring Boot 3.5.12**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Token)**
- **MySQL**
- **Lombok**
- **ModelMapper**
- **Maven**

## 🏗️ Project Structure

```
bttt/
├── src/main/java/com/example/bttt/
│   ├── config/           # Application configuration
│   │   └── AppConfig.java
│   ├── controller/       # REST API controllers
│   │   ├── AuthController.java
│   │   ├── ProjectController.java
│   │   ├── TaskController.java
│   │   └── UserController.java
│   ├── dto/             # Data Transfer Objects
│   │   ├── ApiResponse.java
│   │   ├── AuthResponse.java
│   │   ├── LoginRequest.java
│   │   ├── ProjectRequest.java
│   │   ├── ProjectResponse.java
│   │   ├── RegisterRequest.java
│   │   ├── TaskRequest.java
│   │   ├── TaskResponse.java
│   │   └── UserResponse.java
│   ├── entity/          # JPA Entities
│   │   ├── Project.java
│   │   ├── Task.java
│   │   └── User.java
│   ├── enums/           # Enum types
│   │   ├── Role.java
│   │   └── TaskStatus.java
│   ├── exception/       # Custom exceptions
│   │   ├── BadRequestException.java
│   │   ├── CustomException.java
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── UnAuthorizedException.java
│   │   └── UnAuthorizationException.java
│   ├── repository/      # JPA Repositories
│   │   ├── ProjectRepository.java
│   │   ├── TaskRepository.java
│   │   └── UserRepository.java
│   ├── security/        # Security configuration
│   │   ├── CustomUserDetailsService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── SecurityConfig.java
│   ├── service/         # Business logic
│   │   ├── AuthService.java
│   │   ├── ProjectService.java
│   │   ├── TaskService.java
│   │   └── UserService.java
│   ├── util/            # Utility classes
│   │   └── JwtTokenProvider.java
│   └── BtttApplication.java
├── src/main/resources/
│   ├── application.yml
│   └── static/
├── pom.xml
└── schema.sql
```

## 📊 Database Schema

### Users Table

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);
```

### Projects Table

```sql
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);
```

### Tasks Table

```sql
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    deadline BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);
```

## 🚀 Setup & Installation

### Prerequisites

- **Java 17 or higher**
- **Maven 3.6+**
- **MS SQL **
- **Git**

### Step 1: Clone Repository

```bash
cd c:\Users\nguye\Desktop\bttt
```

### Step 2: Create Database

```sql
CREATE DATABASE task_management_db;
USE task_management_db;
```

### Step 3: Run Database Schema

```bash
mysql -u root -p task_management_db < schema.sql
```

### Step 4: Configure Database Connection

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=bttt;trustServerCertificate=true
    username: your_username
    password: your_password
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
```

### Step 5: Build Project

```bash
mvn clean install
```

### Step 6: Run Application

```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

## 📝 API Endpoints

### Authentication

#### Register User

- **POST** `/api/auth/register`
- **Request:**

```json
{
  "username": "john_doe",
  "password": "password123",
  "role": "USER"
}
```

- **Response (201):**

```json
{
  "code": 201,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "role": "USER"
  }
}
```

#### Login User

- **POST** `/api/auth/login`
- **Request:**

```json
{
  "username": "john_doe",
  "password": "password123"
}
```

- **Response (200):**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "role": "USER"
  }
}
```

### Projects (MANAGER Only)

#### Create Project

- **POST** `/api/projects`
- **Auth:** Bearer Token
- **Role:** MANAGER
- **Request:**

```json
{
  "name": "Website Redesign",
  "description": "Complete redesign of the company website"
}
```

- **Response (201):**

```json
{
  "code": 201,
  "message": "Project created successfully",
  "data": {
    "id": 1,
    "name": "Website Redesign",
    "description": "Complete redesign of the company website",
    "taskCount": 0,
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

#### Get All Projects

- **GET** `/api/projects`
- **Auth:** Bearer Token
- **Response (200):**

```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Website Redesign",
      "description": "Complete redesign of the company website",
      "taskCount": 2,
      "createdAt": 1630000000000,
      "updatedAt": 1630000000000
    }
  ]
}
```

#### Get Project by ID

- **GET** `/api/projects/{id}`
- **Auth:** Bearer Token
- **Response (200):** Same as above

#### Update Project

- **PUT** `/api/projects/{id}`
- **Auth:** Bearer Token
- **Role:** MANAGER
- **Request:** Same as Create
- **Response (200):** Updated project

#### Delete Project

- **DELETE** `/api/projects/{id}`
- **Auth:** Bearer Token
- **Role:** MANAGER
- **Response (200):**

```json
{
  "code": 200,
  "message": "Project deleted successfully"
}
```

### Tasks

#### Create Task

- **POST** `/api/tasks`
- **Auth:** Bearer Token
- **Request:**

```json
{
  "title": "Design Homepage",
  "description": "Create mockups for the homepage",
  "projectId": 1,
  "userId": 2,
  "deadline": 1630604800000
}
```

- **Response (201):**

```json
{
  "code": 201,
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Design Homepage",
    "description": "Create mockups for the homepage",
    "status": "TODO",
    "deadline": 1630604800000,
    "userId": 2,
    "username": "john_doe",
    "projectId": 1,
    "projectName": "Website Redesign",
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

#### Get All Tasks

- **GET** `/api/tasks`
- **Auth:** Bearer Token
- **Response (200):** Array of tasks

#### Get Task by ID

- **GET** `/api/tasks/{id}`
- **Auth:** Bearer Token
- **Response (200):** Single task

#### Get Tasks by User

- **GET** `/api/tasks/user/{userId}`
- **Auth:** Bearer Token
- **Response (200):** Array of user's tasks

#### Get Tasks by Project

- **GET** `/api/tasks/project/{projectId}`
- **Auth:** Bearer Token
- **Response (200):** Array of project's tasks

#### Get Tasks by Status

- **GET** `/api/tasks/status/{status}`
- **Auth:** Bearer Token
- **Status Values:** TODO, IN_PROGRESS, DONE
- **Response (200):** Array of tasks with given status

#### Update Task

- **PUT** `/api/tasks/{id}`
- **Auth:** Bearer Token
- **Request:**

```json
{
  "title": "Updated Title",
  "description": "Updated description",
  "projectId": 1,
  "userId": 2,
  "deadline": 1630604800000
}
```

- **Response (200):** Updated task

#### Update Task Status

- **PATCH** `/api/tasks/{id}/status/{status}`
- **Auth:** Bearer Token
- **Status Values:** TODO, IN_PROGRESS, DONE
- **Response (200):** Updated task
- **Notes:** Status can only transition: TODO → IN_PROGRESS → DONE

#### Assign Task to User

- **PUT** `/api/tasks/{taskId}/assign/{userId}`
- **Auth:** Bearer Token
- **Response (200):** Updated task with new user assignment

#### Delete Task

- **DELETE** `/api/tasks/{id}`
- **Auth:** Bearer Token
- **Response (200):** Deletion success message

### Users

#### Get User by ID

- **GET** `/api/users/{id}`
- **Auth:** Bearer Token
- **Response (200):**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "username": "john_doe",
    "role": "USER",
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

#### Get User by Username

- **GET** `/api/users/username/{username}`
- **Auth:** Bearer Token
- **Response (200):** User object

#### Get All Users

- **GET** `/api/users`
- **Auth:** Bearer Token
- **Response (200):** Array of all users

## 🔐 Authentication

The API uses JWT (JSON Web Token) for authentication.

### Getting a Token

1. Register or login to get a token
2. Include the token in the `Authorization` header for all protected endpoints

### Header Format

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTYzMDAwMDAwMCwiZXhwIjoxNjMwMDg2NDAwfQ...
```

## 👥 Roles & Permissions

| Role        | Permissions                                       |
| ----------- | ------------------------------------------------- |
| **MANAGER** | Create, read, update, delete projects             |
| **USER**    | View own tasks, update task status, view projects |

## ✅ Validation Rules

- **Username:** 3-50 characters, must be unique
- **Password:** 6-100 characters
- **Project Name:** 1-100 characters
- **Task Title:** 1-100 characters
- **Task Deadline:** Must be in the future
- **Task Status:** Valid transitions: TODO → IN_PROGRESS → DONE
- **Cannot Update DONE Tasks:** A task with DONE status cannot be updated

## 🐛 Error Handling

All errors follow a standard response format:

```json
{
  "code": 400,
  "message": "Error description",
  "data": null
}
```

### Common Error Codes

- **400:** Bad Request (Validation failed)
- **401:** Unauthorized (Invalid token/credentials)
- **403:** Forbidden (Insufficient permissions)
- **404:** Not Found (Resource doesn't exist)
- **500:** Internal Server Error

## 🧪 Sample Test Data

Included in `schema.sql`:

**Manager User:**

- Username: `manager_user`
- Password: `password123`
- Role: MANAGER

**Regular User:**

- Username: `regular_user`
- Password: `password456`
- Role: USER

**Sample Projects:**

- Website Redesign
- Mobile App Development

**Sample Tasks:**

- Design Homepage (TODO)
- Develop Backend API (IN_PROGRESS)

## 🔧 Configuration

Edit `src/main/resources/application.yml` to customize:

```yaml
# Database
spring.datasource.url: jdbc:sqlserver://[host]:[port]/[database]
spring.datasource.username: [username]
spring.datasource.password: [password]

# JWT
app.jwtSecret: your-secret-key
app.jwtExpiration: 86400000 # Milliseconds (24 hours)

# Server
server.port: 8080
server.servlet.context-path: /
```

## 📦 Build & Deployment

### Maven Build

```bash
mvn clean package
```

### Run JAR

```bash
java -jar target/bttt-0.0.1-SNAPSHOT.jar
```

### Docker Setup (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/bttt-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:

```bash
docker build -t task-management-api .
docker run -p 8080:8080 task-management-api
```

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT Introduction](https://jwt.io)
- [MS SQL Documentation](https://learn.microsoft.com/en-us/sql/sql-server/?view=sql-server-ver17)

## 📝 License

This project is open source and available under the MIT License.

## 👨‍💻 Development Notes

- **Logging:** Uses SLF4J with Logback. Configure in `application.yml`
- **Pagination:** Not implemented in current version (can be added using Spring Data JPA Page)
- **Caching:** Not implemented (can be added using Spring Cache with Redis)
- **API Documentation:** Can be added using Springdoc OpenAPI (Swagger)
- **Unit Tests:** Test classes are ready to be extended

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

**For questions or support, please create an issue in the repository.**
