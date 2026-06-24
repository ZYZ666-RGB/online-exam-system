# API Checklist

Use this checklist when preparing screenshots, demo flow, or interface testing.

## Auth

- `POST /api/auth/register`: register a student account.
- `POST /api/auth/login`: login and receive a JWT token.
- `GET /api/auth/userInfo`: read current user information.
- `POST /api/auth/logout`: logout and invalidate the token in Redis.

## Question Bank

- `POST /api/questions`: create a question.
- `PUT /api/questions/{id}`: update a question.
- `DELETE /api/questions/{id}`: logically delete a question.
- `GET /api/questions`: page and filter questions.
- `GET /api/questions/{id}`: read question details.

## Paper

- `POST /api/papers`: create a paper.
- `GET /api/papers`: list papers.
- `GET /api/papers/{id}`: read paper details.
- `POST /api/papers/{paperId}/questions`: add a question to a paper.
- `DELETE /api/papers/{paperId}/questions/{questionId}`: remove a question from a paper.
- `GET /api/papers/{paperId}/questions`: list questions in a paper.
- `PUT /api/papers/{paperId}/publish`: publish a paper and refresh total score.

## Exam

- `GET /api/exams/available`: list available exams for a student.
- `POST /api/exams/{paperId}/start`: start or reuse an exam record.
- `GET /api/exams/{examId}/questions`: read exam questions without answers.
- `POST /api/exams/{examId}/submit`: submit answers and calculate score.
- `GET /api/exams/my-scores`: list my scores.
- `GET /api/exams/{examId}/detail`: read exam details.

## Wrong Questions And Statistics

- `GET /api/wrong-questions`: list wrong questions for a student.
- `GET /api/statistics/paper/{paperId}`: read paper statistics.
- `GET /api/statistics/paper/{paperId}/records`: read student exam records for a paper.
