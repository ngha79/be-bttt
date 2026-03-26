# API Documentation - Task Management System

## Base URL

```
http://localhost:8080/api
```

## Authentication

All endpoints except `/auth/register` and `/auth/login` require JWT token in the Authorization header:

```
Authorization: Bearer {token}
```

## Standard Response Format

### Success Response

```json
{
  "code": 200,
  "message": "Success",
  "data": {}
}
```

### Error Response

```json
{
  "code": 400,
  "message": "Error message",
  "data": null
}
```

## HTTP Status Codes

- **200**: OK - Request successful
- **201**: Created - Resource created successfully
- **400**: Bad Request - Invalid input or validation error
- **401**: Unauthorized - Missing or invalid token
- **403**: Forbidden - User doesn't have permission
- **404**: Not Found - Resource doesn't exist
- **500**: Internal Server Error - Server error

---

## Endpoint Details

### 1. AUTHENTICATION ENDPOINTS

#### POST `/auth/register`

Register a new user.

**Request Body:**

```json
{
  "username": "john_doe",
  "password": "password123",
  "role": "USER"
}
```

**Validation Rules:**

- username: 3-50 characters, must be unique
- password: 6-100 characters
- role: must be "USER" or "MANAGER"

**Response:**

```json
{
  "code": 201,
  "message": "User registered successfully",
  "data": {
    "token": "eyJ...",
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "role": "USER"
  }
}
```

**Error Cases:**

- Username already exists (400)
- Invalid role (400)
- Invalid input format (400)

---

#### POST `/auth/login`

Authenticate user and get JWT token.

**Request Body:**

```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "token": "eyJ...",
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "role": "USER"
  }
}
```

**Error Cases:**

- Invalid credentials (401)
- User not found (401)

---

### 2. USER ENDPOINTS

#### GET `/users`

Get all users.

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "username": "john_doe",
      "role": "USER",
      "createdAt": 1630000000000,
      "updatedAt": 1630000000000
    }
  ]
}
```

---

#### GET `/users/{id}`

Get user by ID.

**Parameters:**

- `id` (path): User ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

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

**Error Cases:**

- User not found (404)

---

#### GET `/users/username/{username}`

Get user by username.

**Parameters:**

- `username` (path): Username string

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

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

**Error Cases:**

- User not found (404)

---

### 3. PROJECT ENDPOINTS

#### POST `/projects`

Create a new project. **Requires MANAGER role.**

**Request Body:**

```json
{
  "name": "Website Redesign",
  "description": "Complete redesign of the website"
}
```

**Validation Rules:**

- name: required, 1-100 characters
- description: optional, max 500 characters

**Headers:**

```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response:**

```json
{
  "code": 201,
  "message": "Project created successfully",
  "data": {
    "id": 1,
    "name": "Website Redesign",
    "description": "Complete redesign of the website",
    "taskCount": 0,
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000,
    "members": []
  }
}
```

**Error Cases:**

- Unauthorized (401)
- Forbidden - User doesn't have MANAGER role (403)
- Validation error - Missing project name (400)
- Validation error - Invalid input format (400)

---

#### GET `/projects`

Get all projects.

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Website Redesign",
      "description": "Complete redesign of the website",
      "taskCount": 2,
      "createdAt": 1630000000000,
      "updatedAt": 1630000000000,
      "members": [
        {
          "id": 1,
          "userId": 1,
          "username": "manager_user",
          "role": "OWNER"
        },
        {
          "id": 2,
          "userId": 2,
          "username": "john_doe",
          "role": "MEMBER"
        }
      ]
    }
  ]
}
```

---

#### GET `/projects/{id}`

Get project by ID.

**Parameters:**

- `id` (path): Project ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Website Redesign",
    "description": "Complete redesign of the website",
    "taskCount": 2,
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000,
    "members": [
      {
        "id": 1,
        "userId": 1,
        "username": "manager_user",
        "role": "OWNER"
      },
      {
        "id": 2,
        "userId": 2,
        "username": "john_doe",
        "role": "MEMBER"
      }
    ]
  }
}
```

**Error Cases:**

- Project not found (404)

---

#### PUT `/projects/{id}`

Update project. **Requires MANAGER role.** Both name and description are optional - you only need to provide the fields you want to update.

**Parameters:**

- `id` (path): Project ID

**Request Body:**

```json
{
  "name": "Updated Website Redesign",
  "description": "Updated description"
}
```

**Validation Rules:**

- name: optional, 1-100 characters if provided
- description: optional, max 500 characters if provided
- At least one field should be provided for update to be meaningful

**Headers:**

```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Updated Website Redesign",
    "description": "Updated description",
    "taskCount": 2,
    "createdAt": 1630000000000,
    "updatedAt": 1630000001000,
    "members": [
      {
        "id": 1,
        "userId": 1,
        "username": "manager_user",
        "role": "OWNER"
      },
      {
        "id": 2,
        "userId": 2,
        "username": "john_doe",
        "role": "MEMBER"
      }
    ]
  }
}
```

**Error Cases:**

- Project not found (404)
- Forbidden - User doesn't have MANAGER role (403)
- Validation error - Invalid field values (400)

---

#### DELETE `/projects/{id}`

Delete project. **Requires MANAGER role.**

**Parameters:**

- `id` (path): Project ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Project deleted successfully"
}
```

**Error Cases:**

- Project not found (404)
- Forbidden - User doesn't have MANAGER role (403)

---

### 4. TASK ENDPOINTS

#### POST `/tasks`

Create a new task.

**Request Body:**

```json
{
  "title": "Design Homepage",
  "description": "Create mockups for the homepage",
  "projectId": 1,
  "userId": 2,
  "deadline": 1767379200000
}
```

**Validation Rules:**

- title: 1-100 characters, required
- description: max 500 characters
- projectId: required, must exist
- userId: required, must exist
- deadline: required, must be in future

**Headers:**

```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response:**

```json
{
  "code": 201,
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Design Homepage",
    "description": "Create mockups for the homepage",
    "status": "TODO",
    "deadline": 1767379200000,
    "userId": 2,
    "username": "john_doe",
    "projectId": 1,
    "projectName": "Website Redesign",
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

**Error Cases:**

- Project not found (404)
- User not found (404)
- Deadline in past (400)
- Validation error (400)

---

#### GET `/tasks`

Get all tasks.

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "title": "Design Homepage",
      "description": "Create mockups for the homepage",
      "status": "TODO",
      "deadline": 1767379200000,
      "userId": 2,
      "username": "john_doe",
      "projectId": 1,
      "projectName": "Website Redesign",
      "createdAt": 1630000000000,
      "updatedAt": 1630000000000
    }
  ]
}
```

---

#### GET `/tasks/{id}`

Get task by ID.

**Parameters:**

- `id` (path): Task ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "title": "Design Homepage",
    "description": "Create mockups for the homepage",
    "status": "TODO",
    "deadline": 1767379200000,
    "userId": 2,
    "username": "john_doe",
    "projectId": 1,
    "projectName": "Website Redesign",
    "createdAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

**Error Cases:**

- Task not found (404)

---

#### GET `/tasks/user/{userId}`

Get tasks assigned to a user.

**Parameters:**

- `userId` (path): User ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
    "code": 200,
    "message": "Success",
    "data": [...]
}
```

**Error Cases:**

- User not found (404)

---

#### GET `/tasks/project/{projectId}`

Get tasks in a project.

**Parameters:**

- `projectId` (path): Project ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
    "code": 200,
    "message": "Success",
    "data": [...]
}
```

**Error Cases:**

- Project not found (404)

---

#### GET `/tasks/status/{status}`

Get tasks with specific status.

**Parameters:**

- `status` (path): Task status (TODO, IN_PROGRESS, DONE)

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
    "code": 200,
    "message": "Success",
    "data": [...]
}
```

**Error Cases:**

- Invalid status (400)

---

#### PUT `/tasks/{id}`

Update task.

**Parameters:**

- `id` (path): Task ID

**Request Body:**

```json
{
  "title": "Updated Title",
  "description": "Updated description",
  "projectId": 1,
  "userId": 2,
  "deadline": 1767379200000
}
```

**Headers:**

```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response:**

```json
{
    "code": 200,
    "message": "Success",
    "data": {...}
}
```

**Error Cases:**

- Task not found (404)
- Task status is DONE (400)
- Deadline in past (400)

---

#### PATCH `/tasks/{id}/status/{status}`

Update task status with validation.

**Parameters:**

- `id` (path): Task ID
- `status` (path): New status (TODO, IN_PROGRESS, DONE)

**Headers:**

```
Authorization: Bearer {token}
```

**Status Transitions:**

- TODO → IN_PROGRESS (allowed)
- IN_PROGRESS → DONE (allowed)
- Other transitions: not allowed

**Response:**

```json
{
    "code": 200,
    "message": "Success",
    "data": {...}
}
```

**Error Cases:**

- Task not found (404)
- Task already DONE (400)
- Invalid status transition (400)

---

#### PUT `/tasks/{taskId}/assign/{userId}`

Assign task to a different user.

**Parameters:**

- `taskId` (path): Task ID
- `userId` (path): User ID to assign to

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
    "code": 200,
    "message": "Success",
    "data": {...}
}
```

**Error Cases:**

- Task not found (404)
- User not found (404)

---

#### DELETE `/tasks/{id}`

Delete task.

**Parameters:**

- `id` (path): Task ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Task deleted successfully"
}
```

**Error Cases:**

- Task not found (404)

---

### 5. PROJECT MEMBERS ENDPOINTS

#### POST `/project-members`

Add a member to a project.

**Request Body:**

```json
{
  "projectId": 1,
  "userId": 2,
  "role": "MEMBER"
}
```

**Validation Rules:**

- projectId: required, must exist
- userId: required, must exist
- role: optional (OWNER, MANAGER, MEMBER, VIEWER), defaults to MEMBER

**Headers:**

```
Authorization: Bearer {token}
Content-Type: application/json
```

**Response:**

```json
{
  "code": 201,
  "message": "Member added successfully",
  "data": {
    "id": 1,
    "projectId": 1,
    "userId": 2,
    "username": "john_doe",
    "role": "MEMBER",
    "joinedAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

**Error Cases:**

- Project not found (404)
- User not found (404)
- User already a member (400)
- Invalid role (400)

---

#### GET `/project-members/{id}`

Get project member by ID.

**Parameters:**

- `id` (path): Project Member ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "projectId": 1,
    "userId": 2,
    "username": "john_doe",
    "role": "MEMBER",
    "joinedAt": 1630000000000,
    "updatedAt": 1630000000000
  }
}
```

**Error Cases:**

- Project member not found (404)

---

#### GET `/project-members/project/{projectId}`

Get all members of a project.

**Parameters:**

- `projectId` (path): Project ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "projectId": 1,
      "userId": 1,
      "username": "manager_user",
      "role": "OWNER",
      "joinedAt": 1630000000000,
      "updatedAt": 1630000000000
    },
    {
      "id": 2,
      "projectId": 1,
      "userId": 2,
      "username": "john_doe",
      "role": "MEMBER",
      "joinedAt": 1630000001000,
      "updatedAt": 1630000001000
    }
  ]
}
```

**Error Cases:**

- Project not found (404)

---

#### GET `/project-members/user/{userId}`

Get all projects that a user is member of.

**Parameters:**

- `userId` (path): User ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "projectId": 1,
      "userId": 2,
      "username": "john_doe",
      "role": "MEMBER",
      "joinedAt": 1630000000000,
      "updatedAt": 1630000000000
    }
  ]
}
```

**Error Cases:**

- User not found (404)

---

#### PATCH `/project-members/{id}/role/{role}`

Update member role in a project.

**Parameters:**

- `id` (path): Project Member ID
- `role` (path): New role (OWNER, MANAGER, MEMBER, VIEWER)

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "projectId": 1,
    "userId": 2,
    "username": "john_doe",
    "role": "MANAGER",
    "joinedAt": 1630000000000,
    "updatedAt": 1630000001000
  }
}
```

**Error Cases:**

- Project member not found (404)
- Invalid role (400)

---

#### DELETE `/project-members/{id}`

Remove a member from a project by member ID.

**Parameters:**

- `id` (path): Project Member ID

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Member removed successfully"
}
```

**Error Cases:**

- Project member not found (404)

---

#### DELETE `/project-members/project/{projectId}/user/{userId}`

Remove a specific user from a project.

**Parameters:**

- `projectId` (path): Project ID
- `userId` (path): User ID to remove

**Headers:**

```
Authorization: Bearer {token}
```

**Response:**

```json
{
  "code": 200,
  "message": "Member removed successfully"
}
```

**Error Cases:**

- Project member not found (404)
- User not a member of project (404)

---

## Timestamps

All timestamps are in milliseconds (Unix timestamp \* 1000).

Example conversion:

```javascript
// JavaScript
const timestamp = Date.now(); // Current time in milliseconds
const date = new Date(timestamp); // Convert to Date object
```

---

## Rate Limiting

No rate limiting is currently implemented, but it's recommended for production.

---

## Pagination

No pagination is implemented in the current version. Future versions will support:

```
GET /api/tasks?page=0&size=20&sort=id,desc
```

---

## Error Handling

Every error includes:

- **code**: HTTP status code
- **message**: Human-readable error message
- **data**: null (for errors)

Example error:

```json
{
  "code": 404,
  "message": "Task not found with id: '999'",
  "data": null
}
```

---

## Version History

**v1.0.0** (Current)

- Initial release
- Full CRUD operations for Projects and Tasks
- JWT authentication
- Role-based authorization

---

## Support

For API issues or questions:

1. Check the QUICK_START.md for setup help
2. Review README.md for detailed documentation
3. Test endpoints with POSTMAN_COLLECTION.json
4. Check application logs for errors
