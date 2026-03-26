-- ==========================================
-- DROP TABLES
-- ==========================================
-- ==========================================
-- DROP TABLES
-- ==========================================
DROP TABLE IF EXISTS project_members CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==========================================
-- USERS
-- ==========================================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100),
                       phone VARCHAR(20),
                       avatar VARCHAR(500),
                       avatar_public_id VARCHAR(255),
                       address VARCHAR(255),
                       gender VARCHAR(10),
                       date_of_birth BIGINT,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       created_at BIGINT NOT NULL,
                       updated_at BIGINT NOT NULL
);

CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_role ON users(role);

-- ==========================================
-- PROJECTS
-- ==========================================
CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(500),
                          created_at BIGINT NOT NULL,
                          updated_at BIGINT NOT NULL
);

-- ==========================================
-- PROJECT MEMBERS
-- ==========================================
CREATE TABLE project_members (
                                 id BIGSERIAL PRIMARY KEY,
                                 project_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 role VARCHAR(50) NOT NULL DEFAULT 'MEMBER',
                                 joined_at BIGINT NOT NULL,
                                 updated_at BIGINT NOT NULL,
                                 CONSTRAINT fk_pm_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_pm_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                 CONSTRAINT uk_project_user UNIQUE (project_id, user_id)
);

-- ==========================================
-- TASKS
-- ==========================================
CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
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

-- ==========================================
-- INSERT USERS (30 records)
-- ==========================================
-- ==========================================
-- INSERT USERS (30)
-- ==========================================
INSERT INTO users (username, password, email, phone, avatar, avatar_public_id, address, gender, date_of_birth, role, created_at, updated_at)
VALUES
    ('hoangnv', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'hoang.nguyen@company.com', '0912345678', NULL, NULL, 'Hanoi', 'MALE', 631152000000, 'MANAGER',  1711382400000, 1711382400000),
    ('minhanh', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'anh.le@company.com', '0987654321', NULL, NULL, 'HCM', 'FEMALE', 883612800000, 'MANAGER',  1711382400000, 1711382400000),
    ('trungtt', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'trung.tran@company.com', '0905111222', NULL, NULL, 'Danang', 'MALE', 788918400000, 'USER',  1711382400000, 1711382400000),
    ('linhpt', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'linh.pham@company.com', '0944555666', NULL, NULL, 'Hanoi', 'FEMALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('duongvq', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'duong.vu@company.com', '0333444555', NULL, NULL, 'Hue', 'MALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('thuha', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'ha.nguyen@company.com', '0911222333', NULL, NULL, 'Hanoi', 'FEMALE', 978307200000, 'USER',  1711382400000, 1711382400000),
    ('tuan_pm', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'tuan.pm@company.com', '0919998887', NULL, NULL, 'HCM', 'MALE', 757382400000, 'MANAGER',  1711382400000, 1711382400000),
    ('diep_soft', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'diep.soft@company.com', '0922111333', NULL, NULL, 'Danang', 'FEMALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('son_dev', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'son.dev@company.com', '0933444555', NULL, NULL, 'Hanoi', 'MALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('lan_design', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'lan.design@company.com', '0944555111', NULL, NULL, 'Hue', 'FEMALE', 978307200000, 'USER',  1711382400000, 1711382400000),
    ('quang_fe', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'quang.fe@company.com', '0955666777', NULL, NULL, 'Hanoi', 'MALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('ngoc_hr', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'ngoc.hr@company.com', '0966777888', NULL, NULL, 'HCM', 'FEMALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('bach_be', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'bach.be@company.com', '0977888999', NULL, NULL, 'Danang', 'MALE', 883612800000, 'USER',  1711382400000, 1711382400000),
    ('mai_tester', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'mai.test@company.com', '0988999000', NULL, NULL, 'Hanoi', 'FEMALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('cuong_ops', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'cuong.ops@company.com', '0999000111', NULL, NULL, 'Hue', 'MALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('anh_mkt', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'anh.mkt@company.com', '0900111222', NULL, NULL, 'Hanoi', 'FEMALE', 978307200000, 'USER',  1711382400000, 1711382400000),
    ('binh_ba', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'binh.ba@company.com', '0911222444', NULL, NULL, 'HCM', 'MALE', 883612800000, 'USER',  1711382400000, 1711382400000),
    ('chi_uiux', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'chi.uiux@company.com', '0922333555', NULL, NULL, 'Danang', 'FEMALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('dong_dev', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'dong.dev@company.com', '0933444666', NULL, NULL, 'Hanoi', 'MALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('giang_com', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'giang.pm@company.com', '0944555777', NULL, NULL, 'Hue', 'FEMALE', 852076800000, 'USER',  1711382400000, 1711382400000),
    ('hung_leads', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'hung.leads@company.com', '0955666888', NULL, NULL, 'Hanoi', 'MALE', 820108800000, 'USER',  1711382400000, 1711382400000),
    ('khanh_ds', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'khanh.ds@company.com', '0966777999', NULL, NULL, 'HCM', 'FEMALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('lam_legal', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'lam.legal@company.com', '0977888000', NULL, NULL, 'Danang', 'MALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('nga_acc', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'nga.acc@company.com', '0988999111', NULL, NULL, 'Hanoi', 'FEMALE', 978307200000, 'USER',  1711382400000, 1711382400000),
    ('phuong_sale', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'phuong.sale@company.com', '0999000222', NULL, NULL, 'Hue', 'MALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('quynh_qc', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'quynh.qc@company.com', '0900111333', NULL, NULL, 'Hanoi', 'FEMALE', 946684800000, 'USER',  1711382400000, 1711382400000),
    ('sang_dev', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'sang.dev@company.com', '0911222555', NULL, NULL, 'HCM', 'MALE', 978307200000, 'USER',  1711382400000, 1711382400000),
    ('thao_hr', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'thao.hr@company.com', '0922333666', NULL, NULL, 'Danang', 'FEMALE', 915148800000, 'USER',  1711382400000, 1711382400000),
    ('vinh_arch', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'vinh.arch@company.com', '0933444777', NULL, NULL, 'Hanoi', 'MALE', 883612800000, 'USER',  1711382400000, 1711382400000),
    ('yen_bi', '$2a$10$8.UnVuG9SeW8PPR88V8lMeyW6OBCu29O7yYAnzQ97D/MNoS71zM7.', 'yen.bi@company.com', '0944555999', NULL, NULL, 'Hue', 'FEMALE', 946684800000, 'USER',  1711382400000, 1711382400000);

-- ==========================================
-- INSERT PROJECTS (10)
-- ==========================================
INSERT INTO projects (name, description, created_at, updated_at)
VALUES
    ('E-Learning App', 'Online education system', 1711382400000, 1711382400000),
    ('Smart Warehouse', 'IoT inventory tracking', 1711382400000, 1711382400000),
    ('Fintech Wallet', 'Mobile payment solution', 1711382400000, 1711382400000),
    ('HR Management', 'Internal employee portal', 1711382400000, 1711382400000),
    ('CRM System', 'Customer relationship tool', 1711382400000, 1711382400000),
    ('E-commerce Web', 'Multi-vendor marketplace', 1711382400000, 1711382400000),
    ('Health Tracker', 'Patient monitoring app', 1711382400000, 1711382400000),
    ('Real Estate CMS', 'Property listing site', 1711382400000, 1711382400000),
    ('Logistics App', 'Driver and fleet management', 1711382400000, 1711382400000),
    ('Cloud Storage', 'Private file hosting service', 1711382400000, 1711382400000);

-- ==========================================
-- INSERT PROJECT MEMBERS (Liên kết user với project)
-- ==========================================
INSERT INTO project_members (project_id, user_id, role, joined_at, updated_at)
VALUES
    -- Project 1: E-Learning App (hoangnv, trungtt, linhpt, duongvq)
    (1, 1, 'MEMBER', 1711382400000, 1711382400000),
    (1, 3, 'MEMBER', 1711382400000, 1711382400000),
    (1, 4, 'MEMBER', 1711382400000, 1711382400000),
    (1, 5, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 2: Smart Warehouse (minhanh, son_dev, lan_design)
    (2, 2, 'MEMBER', 1711382400000, 1711382400000),
    (2, 9, 'MEMBER', 1711382400000, 1711382400000),
    (2, 10, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 3: Fintech Wallet (tuan_pm, quang_fe, ngoc_hr)
    (3, 7, 'MEMBER', 1711382400000, 1711382400000),
    (3, 11, 'MEMBER', 1711382400000, 1711382400000),
    (3, 12, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 4: HR Management (minhanh, bach_be, mai_tester)
    (4, 2, 'MEMBER', 1711382400000, 1711382400000),
    (4, 13, 'MEMBER', 1711382400000, 1711382400000),
    (4, 14, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 5: CRM System (hoangnv, cuong_ops, anh_mkt)
    (5, 1, 'MEMBER', 1711382400000, 1711382400000),
    (5, 15, 'MEMBER', 1711382400000, 1711382400000),
    (5, 16, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 6: E-commerce Web (tuan_pm, binh_ba, chi_uiux)
    (6, 7, 'MEMBER', 1711382400000, 1711382400000),
    (6, 17, 'MEMBER', 1711382400000, 1711382400000),
    (6, 18, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 7: Health Tracker (minhanh, dong_dev, giang_com)
    (7, 2, 'MEMBER', 1711382400000, 1711382400000),
    (7, 19, 'MEMBER', 1711382400000, 1711382400000),
    (7, 20, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 8: Real Estate CMS (hoangnv, hung_leads, khanh_ds)
    (8, 1, 'MEMBER', 1711382400000, 1711382400000),
    (8, 21, 'MEMBER', 1711382400000, 1711382400000),
    (8, 22, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 9: Logistics App (tuan_pm, lam_legal, nga_acc)
    (9, 7, 'MEMBER', 1711382400000, 1711382400000),
    (9, 23, 'MEMBER', 1711382400000, 1711382400000),
    (9, 24, 'MEMBER', 1711382400000, 1711382400000),

    -- Project 10: Cloud Storage (minhanh, phuong_sale, quynh_qc)
    (10, 2, 'MEMBER', 1711382400000, 1711382400000),
    (10, 25, 'MEMBER', 1711382400000, 1711382400000),
    (10, 26, 'MEMBER', 1711382400000, 1711382400000);

-- ==========================================
-- INSERT TASKS (Liên kết với user và project)
-- ==========================================
INSERT INTO tasks (title, description, status, deadline, user_id, project_id, created_at, updated_at)
VALUES
    -- Project 1: E-Learning App
    ('DB Schema Design', 'Design ERD for app', 'DONE', 1712678400000, 1, 1, 1711382400000, 1711382400000),
    ('Backend API', 'Implement Login API', 'IN_PROGRESS', 1713110400000, 3, 1, 1711382400000, 1711382400000),
    ('Frontend UI', 'Build user interface', 'TODO', 1713542400000, 4, 1, 1711382400000, 1711382400000),
    ('Testing', 'Unit and integration tests', 'TODO', 1714060800000, 5, 1, 1711382400000, 1711382400000),

    -- Project 2: Smart Warehouse
    ('IoT Setup', 'Configure sensors', 'TODO', 1714579200000, 9, 2, 1711382400000, 1711382400000),
    ('Data Collection', 'Implement data pipeline', 'IN_PROGRESS', 1714320000000, 2, 2, 1711382400000, 1711382400000),
    ('Dashboard', 'Create real-time dashboard', 'TODO', 1715184000000, 10, 2, 1711382400000, 1711382400000),

    -- Project 3: Fintech Wallet
    ('Payment Gateway', 'Integrate payment API', 'IN_PROGRESS', 1713542400000, 11, 3, 1711382400000, 1711382400000),
    ('Security Audit', 'Penetration testing', 'TODO', 1713283200000, 12, 3, 1711382400000, 1711382400000),
    ('UI Fixes', 'Responsive mobile design', 'TODO', 1712505600000, 7, 3, 1711382400000, 1711382400000),

    -- Project 4: HR Management
    ('User Roles', 'Implement role management', 'TODO', 1714233600000, 13, 4, 1711382400000, 1711382400000),
    ('Leave Management', 'Build leave request system', 'IN_PROGRESS', 1713974400000, 14, 4, 1711382400000, 1711382400000),
    ('Reports', 'Generate HR reports', 'TODO', 1715097600000, 2, 4, 1711382400000, 1711382400000),

    -- Project 5: CRM System
    ('Contact Management', 'Manage client contacts', 'DONE', 1712246400000, 15, 5, 1711382400000, 1711382400000),
    ('Sales Pipeline', 'Track sales opportunities', 'IN_PROGRESS', 1714147200000, 16, 5, 1711382400000, 1711382400000),
    ('Reports Dashboard', 'Create analytics dashboard', 'TODO', 1715270400000, 1, 5, 1711382400000, 1711382400000),

    -- Project 6: E-commerce Web
    ('Product Catalog', 'Build product listing', 'DONE', 1711814400000, 17, 6, 1711382400000, 1711382400000),
    ('Shopping Cart', 'Implement cart functionality', 'IN_PROGRESS', 1713801600000, 18, 6, 1711382400000, 1711382400000),
    ('Checkout', 'Complete checkout flow', 'TODO', 1714924800000, 7, 6, 1711382400000, 1711382400000),

    -- Project 7: Health Tracker
    ('User Registration', 'Build signup flow', 'DONE', 1711987200000, 19, 7, 1711382400000, 1711382400000),
    ('Health Records', 'Store health data', 'IN_PROGRESS', 1714060800000, 20, 7, 1711382400000, 1711382400000),
    ('Analytics', 'Health trend analysis', 'TODO', 1715356800000, 2, 7, 1711382400000, 1711382400000),

    -- Project 8: Real Estate CMS
    ('Property Listing', 'Create listing system', 'DONE', 1712332800000, 21, 8, 1711382400000, 1711382400000),
    ('Property Search', 'Build search filters', 'IN_PROGRESS', 1713627600000, 22, 8, 1711382400000, 1711382400000),
    ('Virtual Tour', 'Integrate 3D tours', 'TODO', 1715702400000, 1, 8, 1711382400000, 1711382400000),

    -- Project 9: Logistics App
    ('Tracking System', 'Real-time GPS tracking', 'DONE', 1712073600000, 23, 9, 1711382400000, 1711382400000),
    ('Driver Dashboard', 'Driver mobile app', 'IN_PROGRESS', 1714060800000, 24, 9, 1711382400000, 1711382400000),
    ('Notifications', 'Push notification system', 'TODO', 1714838400000, 7, 9, 1711382400000, 1711382400000),

    -- Project 10: Cloud Storage
    ('File Upload', 'Implement file upload', 'DONE', 1712419200000, 25, 10, 1711382400000, 1711382400000),
    ('File Sharing', 'Share files with others', 'IN_PROGRESS', 1713888000000, 26, 10, 1711382400000, 1711382400000),
    ('Storage Quota', 'Manage storage limits', 'TODO', 1715097600000, 2, 10, 1711382400000, 1711382400000);
-- ==========================================
-- INDEXES
