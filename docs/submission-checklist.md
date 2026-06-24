# Submission Checklist

Use this list before packaging the course design files.

## Source Code

- Spring Boot backend source files are included.
- Static HTML, CSS, and JavaScript files are included.
- `pom.xml` is included.
- `target/`, IDE metadata, and log files are not packaged as source code.

## Database

- `database/schema.sql` is included.
- `database/init.sql` is included.
- Extra test data is included only when it is useful for demonstration.
- The report explains the main tables, foreign keys, indexes, and views.

## Demo

- MySQL and Redis startup steps are ready.
- The database can be initialized from a clean state.
- Admin, teacher, and student demo accounts can login.
- The demo covers login, question bank, paper publishing, online exam, score query, wrong questions, and statistics.

## Git Evidence

- Commit messages describe concrete work.
- The log shows separate commits for configuration, database, backend, frontend, and documentation work.
- Screenshots show `git log --oneline --decorate` or the repository commit list.

## Report

- Requirement analysis is complete.
- E-R design and relational schema are included.
- Key SQL statements are explained.
- Core code snippets are explained.
- Test cases and results are included.
- Summary and improvement ideas are included.
