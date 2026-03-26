# Task Management System - Quick Start Guide

## 🚀 Quick Start (5 minutes)

### 1. Prerequisites Check

```bash
# Check Java version (requires 17+)
java -version

# Check Maven installed
mvn -version

# Check MySQL is running
mysql --version
```

### 2. Database Setup

```sql
-- Open MySQL command line or MySQL Workbench
-- Create database
CREATE DATABASE task_management_db;

-- Import schema
source path/to/schema.sql;
```

Or from command line:

```bash
mysql -u root -p task_management_db < schema.sql
```

### 3. Configure Application

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/task_management_db
    username: root
    password: your_mysql_password
```

### 4. Build & Run

```bash
# Navigate to project directory
cd c:\Users\nguye\Desktop\bttt

# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Or run directly with Java
java -jar target/bttt-0.0.1-SNAPSHOT.jar
```

### 5. Test API

Visit: `http://localhost:8080`

## 📋 Default Test Credentials

**Manager Account:**

- Username: `manager_user`
- Password: `password123`

**Regular User Account:**

- Username: `regular_user`
- Password: `password456`

## 🧪 Testing with cURL

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123",
    "role": "USER"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }'
```

Copy the `token` from response and use it for authenticated requests.

### Get All Users (requires token)

```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Create Project (MANAGER only, requires token)

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Test Project",
    "description": "A test project"
  }'
```

## 🔧 Configuration Options

### Database Connection

```yaml
spring:
  datasource:
    url: jdbc:mysql://[host]:[port]/[database]
    username: [username]
    password: [password]
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
```

### JWT Settings

```yaml
app:
  jwtSecret: your-secret-key-here-min-32-chars
  jwtExpiration: 86400000 # Token expiration in milliseconds (24 hours)
```

### Logging

```yaml
logging:
  level:
    root: INFO
    com.example.bttt: DEBUG
    org.springframework.security: INFO
```

### Server Settings

```yaml
server:
  port: 8080
  servlet:
    context-path: /
  compression:
    enabled: true
  tomcat:
    max-connections: 200
```

## 📝 API Workflow Example

### Step 1: Register User

```bash
POST /api/auth/register
{
  "username": "alice",
  "password": "password123",
  "role": "MANAGER"
}
```

Save the token from response.

### Step 2: Create Project (as MANAGER)

```bash
POST /api/projects
Authorization: Bearer {token_from_step_1}
{
  "name": "Mobile App",
  "description": "Develop Mobile Application"
}
```

Save the project ID (e.g., 1).

### Step 3: Register Another User

```bash
POST /api/auth/register
{
  "username": "bob",
  "password": "password456",
  "role": "USER"
}
```

Save the token and user ID.

### Step 4: Create Task

```bash
POST /api/tasks
Authorization: Bearer {manager_token}
{
  "title": "Build Login Screen",
  "description": "Create authentication screen",
  "projectId": 1,
  "userId": 2,
  "deadline": 1767379200000
}
```

### Step 5: Update Task Status

```bash
PATCH /api/tasks/1/status/IN_PROGRESS
Authorization: Bearer {user_token}
```

### Step 6: Complete Task

```bash
PATCH /api/tasks/1/status/DONE
Authorization: Bearer {user_token}
```

## 🐛 Troubleshooting

### MySQL Connection Error

```
Error: java.sql.SQLException: Cannot get a connection, pool error Timeout waiting for idle object
```

**Solution:**

- Check MySQL is running: `mysql --version`
- Verify database URL in `application.yml`
- Ensure database `task_management_db` exists
- Check username/password are correct

### JWT Secret Too Short

```
Error: Key size must be at least 256 bits for HS256
```

**Solution:**
Ensure `app.jwtSecret` is at least 32 characters in `application.yml`

### Port Already in Use

```
Error: Address already in use: bind
```

**Solution:**
Change port in `application.yml`:

```yaml
server:
  port: 8081 # Use different port
```

### Role-Based Access Denied

```
Error: Access is denied
```

**Solution:**

- Ensure user has correct role (MANAGER for projects)
- Include valid token in Authorization header
- Token format must be: `Bearer {token}`

### Entity Not Found

```
Error: Project not found with id: '999'
```

**Solution:**

- Use valid entity IDs
- Check if entity exists before accessing

## 🔐 Security Checklist

Before deploying:

- [ ] Change default JWT secret to strong, unique value
- [ ] Update MySQL credentials
- [ ] Use HTTPS in production
- [ ] Enable CORS only for trusted domains
- [ ] Implement rate limiting
- [ ] Add request validation
- [ ] Implement audit logging
- [ ] Use environment variables for sensitive data
- [ ] Enable database backups
- [ ] Implement API key rotation

## 📦 Project Dependencies

```
spring-boot-starter-web (3.5.12)
spring-boot-starter-data-jpa (3.5.12)
spring-boot-starter-security (3.5.12)
spring-boot-starter-validation (3.5.12)
mysql-connector-java (8.0.33)
jjwt (0.12.3)
lombok (latest)
modelmapper (3.1.1)
```

## 🚀 Deployment

### Local Development

```bash
mvn spring-boot:run
```

### Build Production JAR

```bash
mvn clean package
```

### Run JAR

```bash
java -jar target/bttt-0.0.1-SNAPSHOT.jar
```

### Docker

```bash
# Build image
docker build -t task-management-api:1.0 .

# Run container
docker run -d \
  --name task-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/task_management_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  task-management-api:1.0
```

## 📚 Useful Links

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Security Docs](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io)
- [MySQL Docs](https://dev.mysql.com/doc/)
- [Postman](https://www.postman.com/)

## 💡 Tips

1. **Use Postman**: Import `POSTMAN_COLLECTION.json` for easy testing
2. **Check Logs**: Monitor application logs for errors
3. **Validate Input**: Always validate request payloads
4. **Test Endpoints**: Test both success and error scenarios
5. **Document APIs**: Update API documentation after changes

## 🆘 Getting Help

- Check logs in console for detailed error messages
- Review `HELP.md` for additional information
- Test endpoints with Postman collection
- Verify database connection and credentials
- Ensure all dependencies are installed with `mvn install`

---

**Happy coding! 🎉**
