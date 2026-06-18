# 关键 SQL 分析

## 1. 查询试卷题目 SQL

```sql
SELECT pq.id, pq.paper_id, pq.question_id, q.question_type, q.title,
       q.option_a, q.option_b, q.option_c, q.option_d,
       q.correct_answer, q.analysis, pq.question_score, pq.sort_order,
       q.difficulty, q.knowledge_point
FROM exam_system.paper_question pq
JOIN exam_system.question q ON pq.question_id = q.id
WHERE pq.paper_id = #{paperId}
  AND q.status = 1
ORDER BY pq.sort_order ASC, pq.id ASC;
```

作用：关联 `paper_question` 和 `question`，按照题目排序查询试卷中的全部题目，体现试卷和题目的多对多关系。

## 2. 查询学生成绩 SQL

```sql
SELECT exam_id, student_id, student_name, paper_id, paper_name,
       total_score, pass_score, submit_time, is_pass
FROM exam_system.v_student_score
WHERE student_id = #{studentId}
ORDER BY submit_time DESC, exam_id DESC;
```

作用：通过视图 `v_student_score` 查询学生成绩。视图内部关联 `exam_record`、`sys_user`、`paper`，并计算是否及格。

## 3. 查询学生错题 SQL

```sql
SELECT wq.id, wq.question_id, wq.exam_id, p.paper_name,
       q.question_type, q.title, q.option_a, q.option_b, q.option_c, q.option_d,
       wq.student_answer, wq.correct_answer, q.analysis, wq.create_time
FROM exam_system.wrong_question wq
JOIN exam_system.question q ON wq.question_id = q.id
JOIN exam_system.exam_record er ON wq.exam_id = er.id
JOIN exam_system.paper p ON er.paper_id = p.id
WHERE wq.student_id = #{studentId}
ORDER BY wq.create_time DESC, wq.id DESC;
```

作用：关联错题、题目、考试记录和试卷，查询学生错题本所需的题干、选项、学生答案、正确答案和解析。

## 4. 查询试卷统计 SQL

```sql
SELECT p.id AS paper_id,
       p.paper_name,
       COUNT(er.id) AS participant_count,
       COALESCE(ROUND(AVG(er.total_score), 2), 0) AS average_score,
       COALESCE(MAX(er.total_score), 0) AS highest_score,
       COALESCE(MIN(er.total_score), 0) AS lowest_score
FROM exam_system.paper p
LEFT JOIN exam_system.exam_record er ON er.paper_id = p.id AND er.status = 1
WHERE p.id = #{paperId}
GROUP BY p.id, p.paper_name;
```

作用：统计某张试卷的参加人数、平均分、最高分和最低分，用于教师端成绩统计页面。

## 5. 提交试卷事务说明

学生提交试卷时，后端 `ExamServiceImpl.submit` 使用 `@Transactional`。事务内依次执行：

1. 查询考试记录。
2. 查询试卷题目和正确答案。
3. 遍历学生提交答案。
4. 写入 `answer_record`。
5. 错题写入 `wrong_question`。
6. 更新 `exam_record` 的得分、提交时间和状态。

如果任一步失败，事务整体回滚，避免出现答题记录保存了但成绩未更新的错误状态。

