# Database Scripts

Run the SQL scripts in this order when creating a fresh local database:

1. `schema.sql`: creates the database, tables, indexes, and views.
2. `init.sql`: inserts roles, demo users, sample questions, and an initial paper.
3. `additional_test_data.sql`: adds extra records for broader manual testing.

The initial demo password is `123456`.

Recommended reset flow:

1. Stop the Spring Boot application.
2. Drop the `exam_system` database if it already exists.
3. Execute `schema.sql`.
4. Execute `init.sql`.
5. Execute `additional_test_data.sql` only when extra test data is needed.

Main relationship checks:

- `sys_user` and `sys_role` are linked through `sys_user_role`.
- `paper` and `question` are linked through `paper_question`.
- `exam_record`, `answer_record`, and `wrong_question` record the exam process.
- `v_student_score` supports student score queries.
