DROP DATABASE IF EXISTS exam_system;
CREATE DATABASE exam_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE exam_system;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：1正常，0禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_sys_user_username (username),
    UNIQUE KEY uk_sys_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码：ADMIN、TEACHER、STUDENT',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    UNIQUE KEY uk_sys_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户角色关联ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    UNIQUE KEY uk_sys_user_role_user_role (user_id, role_id),
    KEY idx_sys_user_role_user_id (user_id),
    KEY idx_sys_user_role_role_id (role_id),
    CONSTRAINT fk_sys_user_role_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
    CONSTRAINT fk_sys_user_role_role FOREIGN KEY (role_id) REFERENCES sys_role (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    question_type VARCHAR(20) NOT NULL COMMENT '题型：single、multiple、judge',
    title TEXT NOT NULL COMMENT '题干',
    option_a VARCHAR(500) NOT NULL COMMENT 'A选项',
    option_b VARCHAR(500) NOT NULL COMMENT 'B选项',
    option_c VARCHAR(500) DEFAULT NULL COMMENT 'C选项',
    option_d VARCHAR(500) DEFAULT NULL COMMENT 'D选项',
    correct_answer VARCHAR(255) NOT NULL COMMENT '正确答案',
    analysis TEXT COMMENT '题目解析',
    default_score INT NOT NULL DEFAULT 5 COMMENT '默认分值',
    difficulty VARCHAR(20) NOT NULL COMMENT '难度：easy、medium、hard',
    knowledge_point VARCHAR(100) NOT NULL COMMENT '知识点',
    create_by BIGINT NOT NULL COMMENT '创建人',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_question_create_by (create_by),
    KEY idx_question_type (question_type),
    KEY idx_question_difficulty (difficulty),
    KEY idx_question_knowledge_point (knowledge_point),
    CONSTRAINT fk_question_create_by FOREIGN KEY (create_by) REFERENCES sys_user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

CREATE TABLE paper (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '试卷ID',
    paper_name VARCHAR(100) NOT NULL COMMENT '试卷名称',
    duration INT NOT NULL COMMENT '考试时长，单位分钟',
    total_score INT NOT NULL DEFAULT 0 COMMENT '总分',
    pass_score INT NOT NULL DEFAULT 0 COMMENT '及格分',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0草稿，1已发布，2已结束',
    create_by BIGINT NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_paper_create_by (create_by),
    KEY idx_paper_status (status),
    KEY idx_paper_time (start_time, end_time),
    CONSTRAINT fk_paper_create_by FOREIGN KEY (create_by) REFERENCES sys_user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

CREATE TABLE paper_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '试卷题目关联ID',
    paper_id BIGINT NOT NULL COMMENT '试卷ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    question_score INT NOT NULL COMMENT '该题在本试卷中的分值',
    sort_order INT NOT NULL COMMENT '题目排序',
    UNIQUE KEY uk_paper_question (paper_id, question_id),
    KEY idx_paper_question_paper_id (paper_id),
    KEY idx_paper_question_question_id (question_id),
    CONSTRAINT fk_paper_question_paper FOREIGN KEY (paper_id) REFERENCES paper (id) ON DELETE CASCADE,
    CONSTRAINT fk_paper_question_question FOREIGN KEY (question_id) REFERENCES question (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷题目关联表';

CREATE TABLE exam_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '考试记录ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    paper_id BIGINT NOT NULL COMMENT '试卷ID',
    start_time DATETIME NOT NULL COMMENT '开始考试时间',
    submit_time DATETIME DEFAULT NULL COMMENT '提交时间',
    total_score INT NOT NULL DEFAULT 0 COMMENT '得分',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0进行中，1已提交',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_exam_record_student_paper (student_id, paper_id),
    KEY idx_exam_record_student_id (student_id),
    KEY idx_exam_record_paper_id (paper_id),
    KEY idx_exam_record_status (status),
    CONSTRAINT fk_exam_record_student FOREIGN KEY (student_id) REFERENCES sys_user (id),
    CONSTRAINT fk_exam_record_paper FOREIGN KEY (paper_id) REFERENCES paper (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表';

CREATE TABLE answer_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '答题记录ID',
    exam_id BIGINT NOT NULL COMMENT '考试记录ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    question_title_snapshot TEXT NOT NULL COMMENT '题干快照',
    option_snapshot TEXT NOT NULL COMMENT '选项快照，JSON字符串',
    student_answer VARCHAR(1000) DEFAULT NULL COMMENT '学生答案',
    correct_answer_snapshot VARCHAR(255) NOT NULL COMMENT '正确答案快照',
    is_correct TINYINT NOT NULL COMMENT '是否正确：1正确，0错误',
    score INT NOT NULL DEFAULT 0 COMMENT '本题得分',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_answer_record_exam_question (exam_id, question_id),
    KEY idx_answer_record_exam_id (exam_id),
    KEY idx_answer_record_question_id (question_id),
    CONSTRAINT fk_answer_record_exam FOREIGN KEY (exam_id) REFERENCES exam_record (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_record_question FOREIGN KEY (question_id) REFERENCES question (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答题记录表';

CREATE TABLE wrong_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '错题ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    exam_id BIGINT NOT NULL COMMENT '考试记录ID',
    student_answer VARCHAR(1000) DEFAULT NULL COMMENT '学生答案',
    correct_answer VARCHAR(255) NOT NULL COMMENT '正确答案',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '错误时间',
    UNIQUE KEY uk_wrong_question_exam_question (exam_id, question_id),
    KEY idx_wrong_question_student_id (student_id),
    KEY idx_wrong_question_question_id (question_id),
    KEY idx_wrong_question_exam_id (exam_id),
    CONSTRAINT fk_wrong_question_student FOREIGN KEY (student_id) REFERENCES sys_user (id),
    CONSTRAINT fk_wrong_question_question FOREIGN KEY (question_id) REFERENCES question (id),
    CONSTRAINT fk_wrong_question_exam FOREIGN KEY (exam_id) REFERENCES exam_record (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题表';

CREATE TABLE login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '登录日志ID',
    user_id BIGINT DEFAULT NULL COMMENT '用户ID，登录失败时可为空',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    login_ip VARCHAR(50) DEFAULT NULL COMMENT '登录IP',
    login_status TINYINT NOT NULL COMMENT '登录状态：1成功，0失败',
    fail_reason VARCHAR(255) DEFAULT NULL COMMENT '失败原因',
    login_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    KEY idx_login_log_user_id (user_id),
    KEY idx_login_log_username (username),
    KEY idx_login_log_time (login_time),
    CONSTRAINT fk_login_log_user FOREIGN KEY (user_id) REFERENCES sys_user (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

CREATE OR REPLACE VIEW v_student_score AS
SELECT
    er.id AS exam_id,
    er.student_id,
    u.real_name AS student_name,
    er.paper_id,
    p.paper_name,
    er.total_score,
    p.pass_score,
    er.submit_time,
    CASE WHEN er.total_score >= p.pass_score THEN 1 ELSE 0 END AS is_pass
FROM exam_record er
JOIN sys_user u ON er.student_id = u.id
JOIN paper p ON er.paper_id = p.id
WHERE er.status = 1;

