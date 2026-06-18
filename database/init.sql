USE exam_system;

INSERT INTO exam_system.sys_role (role_code, role_name, description) VALUES
('ADMIN', '管理员', '负责用户、角色和登录日志管理'),
('TEACHER', '教师', '负责题库、试卷和成绩统计管理'),
('STUDENT', '学生', '负责在线考试、成绩和错题查看');

-- 测试账号统一密码：123456
INSERT INTO exam_system.sys_user (username, password, email, real_name, status) VALUES
('admin', '$2a$10$M3aFJJdVhacJClQXK2qVfObMT9/sveP.5SEvpCZ.FWqhkoBqhiSe.', 'admin@example.com', '系统管理员', 1),
('teacher01', '$2a$10$M3aFJJdVhacJClQXK2qVfObMT9/sveP.5SEvpCZ.FWqhkoBqhiSe.', 'teacher01@example.com', '范老师', 1),
('student01', '$2a$10$M3aFJJdVhacJClQXK2qVfObMT9/sveP.5SEvpCZ.FWqhkoBqhiSe.', 'student01@example.com', '张三', 1);

INSERT INTO exam_system.sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM exam_system.sys_user u JOIN exam_system.sys_role r ON r.role_code = 'ADMIN' WHERE u.username = 'admin';

INSERT INTO exam_system.sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM exam_system.sys_user u JOIN exam_system.sys_role r ON r.role_code = 'TEACHER' WHERE u.username = 'teacher01';

INSERT INTO exam_system.sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM exam_system.sys_user u JOIN exam_system.sys_role r ON r.role_code = 'STUDENT' WHERE u.username = 'student01';

INSERT INTO exam_system.question (
    question_type, title, option_a, option_b, option_c, option_d,
    correct_answer, analysis, default_score, difficulty, knowledge_point, create_by, status
) VALUES
('single', '在关系数据库中，用于唯一标识表中一条记录的字段或字段组合称为？',
 '主键', '外键', '索引', '视图',
 'A', '主键用于唯一标识关系表中的每一条记录。', 10, 'easy', '主键', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '下列哪一项用于维护两个关系表之间的引用完整性？',
 '唯一约束', '外键约束', '默认约束', '检查约束',
 'B', '外键约束用于维护表与表之间的引用关系。', 10, 'easy', '外键', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '下列哪些属于数据库事务的 ACID 特性？',
 '原子性', '一致性', '隔离性', '可读性',
 'A,B,C', '事务 ACID 包括原子性、一致性、隔离性和持久性。', 10, 'medium', '事务', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('judge', '视图本质上是基于一条查询语句定义的虚拟表。',
 '正确', '错误', NULL, NULL,
 'A', '视图通常不直接存储数据，而是保存查询定义。', 10, 'easy', '视图', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '为提高按照用户名查询用户的效率，通常应该在哪个字段上建立索引？',
 'password', 'username', 'real_name', 'status',
 'B', '用户名是登录时的高频查询条件，适合建立唯一索引。', 10, 'medium', '索引', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '关于 E-R 图到关系模型的转换，下列说法正确的是？',
 '实体通常转换为关系表', '多对多关系需要转换为中间表', '属性通常转换为字段', '所有外键都可以省略',
 'A,B,C', 'E-R 图转换时实体、属性和关系都需要映射到关系模型，多对多关系通常引入中间表。', 10, 'medium', 'ER图', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', 'SQL 中用于向表中插入新记录的语句是？',
 'INSERT', 'UPDATE', 'DELETE', 'SELECT',
 'A', 'INSERT 语句用于向表中插入新记录。', 10, 'easy', 'SQL语句', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '下列哪些语句属于数据库定义语言 DDL？',
 'CREATE TABLE', 'ALTER TABLE', 'DROP TABLE', 'SELECT',
 'A,B,C', 'DDL 负责定义和维护数据库对象，SELECT 属于数据查询语言。', 10, 'medium', 'DDL', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('judge', 'HAVING 子句通常用于对分组后的结果进行过滤。',
 '正确', '错误', NULL, NULL,
 'A', 'WHERE 先过滤原始记录，HAVING 通常过滤 GROUP BY 之后的分组结果。', 10, 'medium', '分组查询', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '若关系模式中非主属性不传递依赖于候选键，则通常满足哪一种范式？',
 '第一范式', '第二范式', '第三范式', 'BCNF',
 'C', '第三范式要求非主属性不传递依赖于候选键。', 10, 'hard', '范式', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '关于数据库索引，下列说法正确的是？',
 '可以提高部分查询效率', '会占用额外存储空间', '可能降低写入维护成本', '适合所有字段无差别创建',
 'A,B,C', '索引能提升查询，但需要额外空间，插入、更新、删除时也要维护索引。', 10, 'medium', '索引', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '在常见隔离级别中，能够避免脏读的是？',
 'READ UNCOMMITTED', 'READ COMMITTED', '未开启事务', '无锁读',
 'B', 'READ COMMITTED 可以避免读取到其他事务尚未提交的数据。', 10, 'medium', '事务隔离级别', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '下列哪些属于并发事务可能产生的问题？',
 '脏读', '不可重复读', '幻读', '语法错误',
 'A,B,C', '并发事务常见问题包括脏读、不可重复读和幻读。', 10, 'medium', '并发控制', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('judge', '主键字段允许出现 NULL 值。',
 '正确', '错误', NULL, NULL,
 'B', '主键必须唯一且非空。', 10, 'easy', '主键', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '在 SQL 聚合查询中，用于按照指定字段分组的关键字是？',
 'ORDER BY', 'GROUP BY', 'DISTINCT', 'LIMIT',
 'B', 'GROUP BY 用于将查询结果按照指定字段分组。', 10, 'easy', '分组查询', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '下列哪些属于常见的表连接方式？',
 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'RANDOM JOIN',
 'A,B,C', '常见连接包括内连接、左连接、右连接等，RANDOM JOIN 不是标准连接类型。', 10, 'easy', '连接查询', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '将多对多联系转换为关系模型时，通常需要增加什么？',
 '中间关系表', '删除两个实体', '只保留一个实体', '取消主键',
 'A', '多对多联系通常通过中间表拆分为两个一对多关系。', 10, 'medium', 'ER图', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('judge', '外键通常引用另一张表的主键或唯一键。',
 '正确', '错误', NULL, NULL,
 'A', '外键用于建立引用关系，通常引用被参照表的主键或唯一键。', 10, 'easy', '外键', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '下列哪种视图最可能不能直接更新？',
 '只基于单表且包含主键的简单视图', '包含聚合函数和分组统计的视图', '只选择表中部分普通字段的视图', '不包含表达式的单表视图',
 'B', '包含聚合、分组等复杂计算的视图通常不能直接更新。', 10, 'hard', '视图', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '下列哪些做法有助于优化数据库查询？',
 '为高频过滤字段建立合适索引', '只查询业务需要的字段', '分析执行计划', '任何查询都使用 SELECT *',
 'A,B,C', '合理索引、减少返回列、分析执行计划都有助于优化查询。', 10, 'medium', '查询优化', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '数据库按时间点恢复通常需要结合全量备份和什么日志？',
 '二进制日志', '错误日志', '慢查询日志', '普通文本日志',
 'A', 'MySQL 等数据库常借助全量备份和二进制日志进行时间点恢复。', 10, 'hard', '备份恢复', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('judge', 'SERIALIZABLE 通常是隔离性最强但并发性能较低的事务隔离级别。',
 '正确', '错误', NULL, NULL,
 'A', 'SERIALIZABLE 通过更严格的隔离减少并发异常，但通常会降低并发性能。', 10, 'hard', '事务隔离级别', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('single', '防止 SQL 注入最推荐的方式是？',
 '拼接用户输入生成 SQL', '使用参数化查询或预编译语句', '关闭所有索引', '把密码明文保存',
 'B', '参数化查询可以将 SQL 结构和用户输入分离，是防止 SQL 注入的重要手段。', 10, 'medium', 'SQL安全', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1),
('multiple', '数据库逻辑设计阶段通常会产出哪些内容？',
 '关系模式', '主键设计', '外键设计', '服务器机房布线图',
 'A,B,C', '逻辑设计主要关注关系模式、主键、外键和约束等内容。', 10, 'medium', '数据库设计', (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01'), 1);

INSERT INTO exam_system.paper (
    paper_name, duration, total_score, pass_score, start_time, end_time, status, create_by
) VALUES (
    '数据库原理基础测试卷', 60, 50, 30, '2026-06-01 00:00:00', '2026-12-31 23:59:59', 1,
    (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01')
);

INSERT INTO exam_system.paper_question (paper_id, question_id, question_score, sort_order)
SELECT p.id, q.id, 10, q.sort_order
FROM exam_system.paper p
JOIN (
    SELECT id, 1 AS sort_order FROM exam_system.question WHERE title LIKE '在关系数据库中%'
    UNION ALL
    SELECT id, 2 AS sort_order FROM exam_system.question WHERE title LIKE '下列哪一项%'
    UNION ALL
    SELECT id, 3 AS sort_order FROM exam_system.question WHERE title LIKE '下列哪些属于数据库事务%'
    UNION ALL
    SELECT id, 4 AS sort_order FROM exam_system.question WHERE title LIKE '视图本质上%'
    UNION ALL
    SELECT id, 5 AS sort_order FROM exam_system.question WHERE title LIKE '为提高按照用户名%'
) q
WHERE p.paper_name = '数据库原理基础测试卷';

INSERT INTO exam_system.paper (
    paper_name, duration, total_score, pass_score, start_time, end_time, status, create_by
) VALUES (
    '数据库原理综合练习卷', 90, 100, 60, '2026-06-01 00:00:00', '2026-12-31 23:59:59', 1,
    (SELECT id FROM exam_system.sys_user WHERE username = 'teacher01')
);

INSERT INTO exam_system.paper_question (paper_id, question_id, question_score, sort_order)
SELECT p.id, q.id, 10, q.sort_order
FROM exam_system.paper p
JOIN (
    SELECT id, 1 AS sort_order FROM exam_system.question WHERE title LIKE 'SQL 中用于向表中插入%'
    UNION ALL
    SELECT id, 2 AS sort_order FROM exam_system.question WHERE title LIKE '下列哪些语句属于数据库定义语言%'
    UNION ALL
    SELECT id, 3 AS sort_order FROM exam_system.question WHERE title LIKE 'HAVING 子句通常%'
    UNION ALL
    SELECT id, 4 AS sort_order FROM exam_system.question WHERE title LIKE '若关系模式中非主属性%'
    UNION ALL
    SELECT id, 5 AS sort_order FROM exam_system.question WHERE title LIKE '关于数据库索引%'
    UNION ALL
    SELECT id, 6 AS sort_order FROM exam_system.question WHERE title LIKE '在常见隔离级别中%'
    UNION ALL
    SELECT id, 7 AS sort_order FROM exam_system.question WHERE title LIKE '下列哪些属于并发事务%'
    UNION ALL
    SELECT id, 8 AS sort_order FROM exam_system.question WHERE title LIKE '主键字段允许%'
    UNION ALL
    SELECT id, 9 AS sort_order FROM exam_system.question WHERE title LIKE '在 SQL 聚合查询中%'
    UNION ALL
    SELECT id, 10 AS sort_order FROM exam_system.question WHERE title LIKE '下列哪些属于常见的表连接方式%'
) q
WHERE p.paper_name = '数据库原理综合练习卷';
