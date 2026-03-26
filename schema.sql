-- Task Management Database Schema for SQL Server

-- Drop existing tables if they exist
IF OBJECT_ID('project_members', 'U') IS NOT NULL DROP TABLE project_members;
IF OBJECT_ID('tasks', 'U') IS NOT NULL DROP TABLE tasks;
IF OBJECT_ID('projects', 'U') IS NOT NULL DROP TABLE projects;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;

-- Create Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);

CREATE INDEX idx_username ON users(username);

-- Create Projects table
CREATE TABLE projects (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);

CREATE INDEX idx_name ON projects(name);

-- Create Project Members table
CREATE TABLE project_members (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'MEMBER',
    joined_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    CONSTRAINT fk_project_member_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_member_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_project_user UNIQUE (project_id, user_id)
);

CREATE INDEX idx_project_id ON project_members(project_id);
CREATE INDEX idx_user_id ON project_members(user_id);
CREATE INDEX idx_role ON project_members(role);

-- Create Tasks table
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',
    deadline BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE INDEX idx_task_user_id ON tasks(user_id);
CREATE INDEX idx_task_project_id ON tasks(project_id);
CREATE INDEX idx_status ON tasks(status);
CREATE INDEX idx_deadline ON tasks(deadline);
CREATE INDEX idx_tasks_user_project ON tasks(user_id, project_id);
CREATE INDEX idx_users_role ON users(role);

-- Sample data (optional)
-- Insert sample users
INSERT INTO users (username, password, role, created_at, updated_at) 
VALUES 
('123123', '$2a$10$slYQmyNdGzin7olVN3p5Be.8F8mhB1/0vDFGQD3cF3F03.xzjq4Vu', 'MANAGER', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE())),
('1231234', '$2a$10$DmCp.RX3m6HVMxR4VXqvT.tIYKVhjcaAFAO6SXtlPLfYrANEe.a1u', 'USER', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()));

-- Insert sample projects
INSERT INTO projects (name, description, created_at, updated_at) 
VALUES 
('Website Redesign', 'Complete redesign of the company website', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE())),
('Mobile App Development', 'Develop iOS and Android mobile applications', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()));

-- Insert sample tasks
INSERT INTO tasks (title, description, status, deadline, user_id, project_id, created_at, updated_at) 
VALUES 
('Design Homepage', 'Create mockups for the homepage', 'TODO', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()) + 7*24*60*60*1000, 1, 1, DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE())),
('Develop Backend API', 'Build REST API endpoints', 'IN_PROGRESS', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()) + 14*24*60*60*1000, 2, 2, DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()));

-- Insert sample project members
INSERT INTO project_members (project_id, user_id, role, joined_at, updated_at)
VALUES
(1, 1, 'OWNER', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE())),
(1, 2, 'MEMBER', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE())),
(2, 1, 'MANAGER', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE())),
(2, 2, 'OWNER', DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()), DATEDIFF_BIG(MILLISECOND, '1970-01-01', GETUTCDATE()));

-- Note: Sample passwords are:
-- 123123: password123
-- 1231234: password456
