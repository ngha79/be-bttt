# Architecture & Design Patterns Guide

## 🏗️ Project Architecture

This Task Management System follows a **layered architecture** pattern with clear separation of concerns.

### Architecture Layers

```
┌─────────────────────────────────────┐
│      REST API Controllers           │  (Request/Response)
├─────────────────────────────────────┤
│    Business Logic Services          │  (Domain Logic)
├─────────────────────────────────────┤
│    Data Access Repositories         │  (Database Queries)
├─────────────────────────────────────┤
│      Database (MySQL)               │  (Data Storage)
└─────────────────────────────────────┘
```

---

## 📊 Design Patterns Used

### 1. **MVC (Model-View-Controller)**

- **Model**: JPA Entities (User, Project, Task)
- **View**: DTOs and API Responses
- **Controller**: REST Controllers handling HTTP requests

### 2. **Repository Pattern**

- Abstracts database operations
- Provides data access layer
- Uses Spring Data JPA interfaces

```
UserRepository extends JpaRepository<User, Long>
```

### 3. **Service/Business Logic Pattern**

- Encapsulates business logic
- Validates data before persistence
- Manages business rules

```
UserService → UserRepository → Database
```

### 4. **DTO (Data Transfer Object) Pattern**

- Separates API contracts from entities
- Protects internal domain model
- Controls what data is exposed
- Enables data transformation

```
User (Entity) ↔ UserResponse (DTO)
```

### 5. **Dependency Injection**

- Spring manages bean lifecycle
- Loose coupling between components
- Easier testing and maintenance

```java
@Autowired
private UserService userService;
```

### 6. **Global Exception Handling**

- Centralized error handling
- Consistent error response format
- Reduces code duplication

```
@RestControllerAdvice
GlobalExceptionHandler
```

### 7. **Token-Based Authentication (JWT)**

- Stateless authentication
- Secure token validation
- User information embedded in token

```
Token = {header.payload.signature}
```

### 8. **Validation Pattern**

- Input validation at DTO level
- Business rule validation in service
- Prevents invalid data in database

### 9. **Factory Pattern**

- ApiResponse factory methods
- Creates consistent responses

```java
ApiResponse.success(data)
ApiResponse.error(code, message)
```

### 10. **Strategy Pattern**

- Different task status transitions
- Flexible validation rules

---

## 📁 Layered Structure Breakdown

### Controller Layer (`controller/`)

**Responsibility**: Handle HTTP requests and responses

```
AuthController
├── POST /auth/register
├── POST /auth/login

UserController
├── GET /users
├── GET /users/{id}
├── GET /users/username/{username}

ProjectController
├── POST /projects (MANAGER)
├── GET /projects
├── PUT /projects/{id} (MANAGER)
├── DELETE /projects/{id} (MANAGER)

TaskController
├── POST /tasks
├── GET /tasks
├── PATCH /tasks/{id}/status/{status}
├── PUT /tasks/{id}/assign/{userId}
└── ... more endpoints
```

### Service Layer (`service/`)

**Responsibility**: Business logic and validation

```
AuthService
├── register(RegisterRequest)
├── login(LoginRequest)

UserService
├── getUserById(Long)
├── getAllUsers()

ProjectService
├── createProject(ProjectRequest)
├── getProjectById(Long)
├── updateProject(Long, ProjectRequest)
├── deleteProject(Long)

TaskService
├── createTask(TaskRequest)
├── getTasksByUserId(Long)
├── updateTaskStatus(Long, TaskStatus)
├── validateStatusTransition()
├── validateDeadline()
```

### Repository Layer (`repository/`)

**Responsibility**: Data access operations

```
UserRepository
├── findByUsername(String)
├── existsByUsername(String)

ProjectRepository
├── findAll()
├── findById(Long)

TaskRepository
├── findByUserId(Long)
├── findByProjectId(Long)
├── findByStatus(TaskStatus)
```

### Entity Layer (`entity/`)

**Responsibility**: Domain models

```
User
├── id
├── username
├── password
├── role
├── createdAt
└── updatedAt

Project
├── id
├── name
├── description
└── Tasks (1-to-Many)

Task
├── id
├── title
├── description
├── status
├── deadline
├── User (Many-to-1)
└── Project (Many-to-1)
```

### DTO Layer (`dto/`)

**Responsibility**: API data transfer

```
Request DTOs:
├── RegisterRequest
├── LoginRequest
├── ProjectRequest
├── TaskRequest

Response DTOs:
├── AuthResponse
├── UserResponse
├── ProjectResponse
├── TaskResponse
└── ApiResponse<T> (Standard wrapper)
```

### Security Layer (`security/`)

**Responsibility**: Authentication and authorization

```
SecurityConfig
├── Configure HTTP security
├── Set up JWT filter
├── Enable method-level security

JwtAuthenticationFilter
├── Extract token from header
├── Validate token
├── Set authentication in context

CustomUserDetailsService
├── Load user details
├── Set user authorities
```

### Exception Handling (`exception/`)

**Responsibility**: Error handling and responses

```
GlobalExceptionHandler
├── Handle CustomException
├── Handle ResourceNotFoundException
├── Handle ValidationException
├── Handle AuthenticationException
└── Respond with standard format
```

---

## 🔄 Data Flow Examples

### User Registration Flow

```
1. Client sends POST /api/auth/register
   {username, password, role}
   ↓
2. AuthController.register()
   ↓
3. AuthService.register()
   ├── Check username not exists
   ├── Validate role
   ├── Encode password
   ├── Create User entity
   └── Save to database
   ↓
4. Generate JWT token
   ↓
5. Return AuthResponse with token
   ↓
6. Client receives response
```

### Create Task Flow

```
1. Client sends POST /api/tasks
   {title, projectId, userId, deadline}
   ↓
2. TaskController.createTask()
   ├── Validate input (JSR-380)
   ↓
3. TaskService.createTask()
   ├── Find Project (or throw 404)
   ├── Find User (or throw 404)
   ├── Validate deadline > now
   ├── Create Task entity
   ├── Set status = TODO
   └── Save to database
   ↓
4. Map Task → TaskResponse
   ↓
5. Return ApiResponse<TaskResponse>
   ↓
6. Client receives task with ID
```

### Update Task Status Flow

```
1. Client sends PATCH /api/tasks/1/status/IN_PROGRESS
   ↓
2. TaskController.updateTaskStatus(1, IN_PROGRESS)
   ↓
3. TaskService.updateTaskStatus(1, IN_PROGRESS)
   ├── Find Task with ID 1 (or throw 404)
   ├── Check status != DONE (or throw 400)
   ├── Validate transition TODO → IN_PROGRESS ✓
   ├── Set status = IN_PROGRESS
   ├── Update updatedAt timestamp
   └── Save to database
   ↓
4. Map Task → TaskResponse
   ↓
5. Return updated task
   ↓
6. Client receives updated task
```

---

## 🔐 Security Architecture

### Authentication Flow

```
User Credentials
       ↓
AuthenticationManager
       ↓
CustomUserDetailsService
       ↓
User authenticated ✓
       ↓
Generate JWT token
       ↓
Return token to client
```

### Authorization Flow

```
Client Request with JWT
       ↓
JwtAuthenticationFilter
├── Extract token
├── Validate signature
├── Check expiration
└── Extract username
       ↓
CustomUserDetailsService
└── Load user details (username, role)
       ↓
Set SecurityContext
       ↓
Check @PreAuthorize("hasRole('MANAGER')")
       ↓
If authorized → Process request
If denied → Return 403 Forbidden
```

### JWT Token Structure

```
JWT = Header.Payload.Signature

Header: {"alg":"HS512","typ":"JWT"}

Payload: {
  "sub":"john_doe",
  "iat":1630000000,
  "exp":1630086400
}

Signature: HMACSHA512(Header.Payload, secret)
```

---

## 💾 Database Schema Design

### Relationship Diagram

```
Users (1) ──── (N) Tasks
  │ id            │ id
  │ username      │ title
  │ password      │ status
  │ role          │ deadline
                  │ user_id (FK)
                  │ project_id (FK)

Projects (1) ──── (N) Tasks
  │ id               │ id
  │ name             │ title
  │ description      │ status
                     │ deadline
                     │ project_id (FK)
                     │ user_id (FK)
```

### Key Design Decisions

1. **Surrogate Keys (Long IDs)**
   - Auto-generated primary keys
   - Better performance than natural keys
   - Allows entity modification

2. **Foreign Keys with Cascade**
   - Maintain referential integrity
   - Cascade delete for cleanup
   - Prevent orphaned records

3. **Timestamps (milliseconds)**
   - createdAt: Immutable
   - updatedAt: Updates on modifications
   - Unix timestamps for consistency

4. **Indexes**
   - On user_id, project_id
   - On status, deadline
   - Improves query performance

---

## 🧪 Testing Strategy

### Unit Testing (Recommended)

```java
// Test services in isolation
@SpringBootTest
class AuthServiceTest {
    @MockBean private UserRepository userRepository;
    @InjectMocks private AuthService authService;

    @Test
    void testRegister() { ... }
}
```

### Integration Testing (Recommended)

```java
// Test controllers with full context
@SpringBootTest
class AuthControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void testRegisterEndpoint() { ... }
}
```

### Test Coverage Areas

- [ ] Authentication (register, login)
- [ ] Authorization (role-based access)
- [ ] CRUD operations
- [ ] Validation rules
- [ ] Exception handling
- [ ] Business logic
- [ ] Status transitions

---

## 🚀 Performance Considerations

### Optimizations Implemented

1. **Database Indexes** - Faster queries
2. **Lazy/Eager Loading** - Appropriate fetch strategies
3. **Stateless Sessions** - Better scalability
4. **Connection Pooling** - Efficient resource usage
5. **Error Fast** - Early validation

### Future Optimizations

1. **Pagination** - Handle large datasets
2. **Caching** - Reduce database calls
3. **Query Optimization** - Complex queries
4. **Async Processing** - Long-running tasks
5. **Database Replication** - High availability

---

## 📈 Scalability Approach

### Current State

- Single instance deployment
- Single database instance
- Suitable for small to medium workloads

### Scalability Improvements

1. **Horizontal Scaling**
   - Multiple API instances
   - Load balancer (Nginx, HAProxy)
   - Stateless design ✓ (JWT)

2. **Database Scaling**
   - Read replicas
   - Connection pooling
   - Query optimization

3. **Caching Layer**
   - Redis for session cache
   - Query result cache
   - Token blacklist cache

4. **Message Queue**
   - Async tasks
   - Email notifications
   - Event streaming

---

## 🔄 CI/CD Pipeline Recommendations

```
Code Push
   ↓
Run Tests (Unit + Integration)
   ↓
Code Quality Analysis (SonarQube)
   ↓
Build Docker Image
   ↓
Push to Registry
   ↓
Deploy to Dev Environment
   ↓
Smoke Tests
   ↓
Deploy to Staging
   ↓
Integration Tests
   ↓
Deploy to Production
   ↓
Monitor & Alert
```

---

## 📚 Design Principles Applied

### SOLID Principles

- **S**ingle Responsibility: Each class has one reason to change
- **O**pen/Closed: Open for extension, closed for modification
- **L**iskov Substitution: Interfaces can be substituted
- **I**nterface Segregation: Specific interfaces over general
- **D**ependency Inversion: Depend on abstractions, not concretions

### DRY (Don't Repeat Yourself)

- Common logic in services
- Reusable DTOs
- Centralized exception handling

### KISS (Keep It Simple, Stupid)

- Clear, readable code
- Straightforward business logic
- Minimal dependencies

### YAGNI (You Aren't Gonna Need It)

- No premature optimization
- Only implement required features
- Remove unused code

---

## 🎯 Best Practices Followed

1. **Exception Handling** - Checked and handled appropriately
2. **Validation** - Input and business rule validation
3. **Security** - JWT, password encryption, authorization
4. **Logging** - Appropriate log levels
5. **Documentation** - Code comments and docs
6. **Naming** - Clear, descriptive names
7. **Code Organization** - Logical package structure
8. **Configuration** - Externalized through application.yml
9. **Testing** - Test-driven development ready
10. **Performance** - Efficient queries and caching

---

## 🔧 Extending the Application

### Adding a New Feature

1. **Create Entity** (entity/)
2. **Create Repository** (repository/)
3. **Create DTOs** (dto/)
4. **Create Service** (service/)
5. **Create Controller** (controller/)
6. **Write Tests** (test/)
7. **Update Documentation**

### Example: Adding Comments Feature

```
1. Entity: Comment {id, content, user_id, task_id}
2. Repository: CommentRepository extends JpaRepository
3. DTOs: CreateCommentRequest, CommentResponse
4. Service: CommentService with CRUD operations
5. Controller: CommentController with endpoints
```

---

## 📖 Additional Resources

- [Spring Framework Documentation](https://spring.io/)
- [Design Patterns in Java](https://refactoring.guru/design-patterns)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [JWT Best Practices](https://tools.ietf.org/html/rfc7519)
- [RESTful API Design](https://restfulapi.net/)

---

**This architecture provides a solid foundation for a scalable, maintainable, and secure Task Management System.**
