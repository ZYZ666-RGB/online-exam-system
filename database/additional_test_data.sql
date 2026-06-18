USE exam_system;

INSERT INTO exam_system.question (
    question_type, title, option_a, option_b, option_c, option_d,
    correct_answer, analysis, default_score, difficulty, knowledge_point, create_by, status
)
SELECT q.question_type, q.title, q.option_a, q.option_b, q.option_c, q.option_d,
       q.correct_answer, q.analysis, q.default_score, q.difficulty, q.knowledge_point,
       teacher.id, 1
FROM (
    SELECT 'single' AS question_type, 'SQL 中用于向表中插入新记录的语句是？' AS title,
           'INSERT' AS option_a, 'UPDATE' AS option_b, 'DELETE' AS option_c, 'SELECT' AS option_d,
           'A' AS correct_answer, 'INSERT 语句用于向表中插入新记录。' AS analysis,
           10 AS default_score, 'easy' AS difficulty, 'SQL语句' AS knowledge_point
    UNION ALL SELECT 'multiple', '下列哪些语句属于数据库定义语言 DDL？',
           'CREATE TABLE', 'ALTER TABLE', 'DROP TABLE', 'SELECT',
           'A,B,C', 'DDL 负责定义和维护数据库对象，SELECT 属于数据查询语言。', 10, 'medium', 'DDL'
    UNION ALL SELECT 'judge', 'HAVING 子句通常用于对分组后的结果进行过滤。',
           '正确', '错误', NULL, NULL,
           'A', 'WHERE 先过滤原始记录，HAVING 通常过滤 GROUP BY 之后的分组结果。', 10, 'medium', '分组查询'
    UNION ALL SELECT 'single', '若关系模式中非主属性不传递依赖于候选键，则通常满足哪一种范式？',
           '第一范式', '第二范式', '第三范式', 'BCNF',
           'C', '第三范式要求非主属性不传递依赖于候选键。', 10, 'hard', '范式'
    UNION ALL SELECT 'multiple', '关于数据库索引，下列说法正确的是？',
           '可以提高部分查询效率', '会占用额外存储空间', '可能降低写入维护成本', '适合所有字段无差别创建',
           'A,B,C', '索引能提升查询，但需要额外空间，插入、更新、删除时也要维护索引。', 10, 'medium', '索引'
    UNION ALL SELECT 'single', '在常见隔离级别中，能够避免脏读的是？',
           'READ UNCOMMITTED', 'READ COMMITTED', '未开启事务', '无锁读',
           'B', 'READ COMMITTED 可以避免读取到其他事务尚未提交的数据。', 10, 'medium', '事务隔离级别'
    UNION ALL SELECT 'multiple', '下列哪些属于并发事务可能产生的问题？',
           '脏读', '不可重复读', '幻读', '语法错误',
           'A,B,C', '并发事务常见问题包括脏读、不可重复读和幻读。', 10, 'medium', '并发控制'
    UNION ALL SELECT 'judge', '主键字段允许出现 NULL 值。',
           '正确', '错误', NULL, NULL,
           'B', '主键必须唯一且非空。', 10, 'easy', '主键'
    UNION ALL SELECT 'single', '在 SQL 聚合查询中，用于按照指定字段分组的关键字是？',
           'ORDER BY', 'GROUP BY', 'DISTINCT', 'LIMIT',
           'B', 'GROUP BY 用于将查询结果按照指定字段分组。', 10, 'easy', '分组查询'
    UNION ALL SELECT 'multiple', '下列哪些属于常见的表连接方式？',
           'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'RANDOM JOIN',
           'A,B,C', '常见连接包括内连接、左连接、右连接等，RANDOM JOIN 不是标准连接类型。', 10, 'easy', '连接查询'
    UNION ALL SELECT 'single', '将多对多联系转换为关系模型时，通常需要增加什么？',
           '中间关系表', '删除两个实体', '只保留一个实体', '取消主键',
           'A', '多对多联系通常通过中间表拆分为两个一对多关系。', 10, 'medium', 'ER图'
    UNION ALL SELECT 'judge', '外键通常引用另一张表的主键或唯一键。',
           '正确', '错误', NULL, NULL,
           'A', '外键用于建立引用关系，通常引用被参照表的主键或唯一键。', 10, 'easy', '外键'
    UNION ALL SELECT 'single', '下列哪种视图最可能不能直接更新？',
           '只基于单表且包含主键的简单视图', '包含聚合函数和分组统计的视图', '只选择表中部分普通字段的视图', '不包含表达式的单表视图',
           'B', '包含聚合、分组等复杂计算的视图通常不能直接更新。', 10, 'hard', '视图'
    UNION ALL SELECT 'multiple', '下列哪些做法有助于优化数据库查询？',
           '为高频过滤字段建立合适索引', '只查询业务需要的字段', '分析执行计划', '任何查询都使用 SELECT *',
           'A,B,C', '合理索引、减少返回列、分析执行计划都有助于优化查询。', 10, 'medium', '查询优化'
    UNION ALL SELECT 'single', '数据库按时间点恢复通常需要结合全量备份和什么日志？',
           '二进制日志', '错误日志', '慢查询日志', '普通文本日志',
           'A', 'MySQL 等数据库常借助全量备份和二进制日志进行时间点恢复。', 10, 'hard', '备份恢复'
    UNION ALL SELECT 'judge', 'SERIALIZABLE 通常是隔离性最强但并发性能较低的事务隔离级别。',
           '正确', '错误', NULL, NULL,
           'A', 'SERIALIZABLE 通过更严格的隔离减少并发异常，但通常会降低并发性能。', 10, 'hard', '事务隔离级别'
    UNION ALL SELECT 'single', '防止 SQL 注入最推荐的方式是？',
           '拼接用户输入生成 SQL', '使用参数化查询或预编译语句', '关闭所有索引', '把密码明文保存',
           'B', '参数化查询可以将 SQL 结构和用户输入分离，是防止 SQL 注入的重要手段。', 10, 'medium', 'SQL安全'
    UNION ALL SELECT 'multiple', '数据库逻辑设计阶段通常会产出哪些内容？',
           '关系模式', '主键设计', '外键设计', '服务器机房布线图',
           'A,B,C', '逻辑设计主要关注关系模式、主键、外键和约束等内容。', 10, 'medium', '数据库设计'
) q
JOIN exam_system.sys_user teacher ON teacher.username = 'teacher01'
WHERE NOT EXISTS (
    SELECT 1 FROM exam_system.question old_question WHERE old_question.title = q.title
);

INSERT INTO exam_system.paper (
    paper_name, duration, total_score, pass_score, start_time, end_time, status, create_by
)
SELECT '数据库原理综合练习卷', 90, 100, 60,
       '2026-06-01 00:00:00', '2026-12-31 23:59:59', 1, teacher.id
FROM exam_system.sys_user teacher
WHERE teacher.username = 'teacher01'
  AND NOT EXISTS (
      SELECT 1 FROM exam_system.paper p WHERE p.paper_name = '数据库原理综合练习卷'
  );

INSERT INTO exam_system.paper_question (paper_id, question_id, question_score, sort_order)
SELECT paper.id, question.id, 10, q.sort_order
FROM exam_system.paper paper
JOIN (
    SELECT 'SQL 中用于向表中插入新记录的语句是？' AS title, 1 AS sort_order
    UNION ALL SELECT '下列哪些语句属于数据库定义语言 DDL？', 2
    UNION ALL SELECT 'HAVING 子句通常用于对分组后的结果进行过滤。', 3
    UNION ALL SELECT '若关系模式中非主属性不传递依赖于候选键，则通常满足哪一种范式？', 4
    UNION ALL SELECT '关于数据库索引，下列说法正确的是？', 5
    UNION ALL SELECT '在常见隔离级别中，能够避免脏读的是？', 6
    UNION ALL SELECT '下列哪些属于并发事务可能产生的问题？', 7
    UNION ALL SELECT '主键字段允许出现 NULL 值。', 8
    UNION ALL SELECT '在 SQL 聚合查询中，用于按照指定字段分组的关键字是？', 9
    UNION ALL SELECT '下列哪些属于常见的表连接方式？', 10
) q
JOIN exam_system.question question ON question.title = q.title
WHERE paper.paper_name = '数据库原理综合练习卷'
  AND NOT EXISTS (
      SELECT 1
      FROM exam_system.paper_question old_pq
      WHERE old_pq.paper_id = paper.id
        AND old_pq.question_id = question.id
  );
