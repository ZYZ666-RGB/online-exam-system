# 基于 SpringBoot 的在线考试与题库管理系统

本项目是数据库原理课程设计项目，重点体现数据库设计和数据库驱动业务实现，不做大型在线教育平台。

## 技术栈

- 后端：Spring Boot、MyBatis、MySQL、Lombok、JWT、BCrypt、Redis
- 前端：HTML、CSS、JavaScript、Ajax
- 工具：IntelliJ IDEA、Navicat/SQLyog、Git、Visio

Redis 仅用于登录 Token 状态管理、退出登录、Token 主动失效和登录/注册限流，不缓存题库、试卷、成绩等核心业务数据。

## 核心角色

- ADMIN：用户管理、角色管理、登录日志查看
- TEACHER：题库管理、试卷管理、发布考试、查看学生成绩
- STUDENT：查看考试、在线答题、提交试卷、查看成绩和错题

普通注册默认绑定 STUDENT 角色。ADMIN 和 TEACHER 账号通过初始化 SQL 创建，避免用户自行注册为高权限角色。

## 数据库设计重点

- sys_user 与 sys_role 通过 sys_user_role 建立多对多关系
- paper 与 question 通过 paper_question 建立多对多关系
- 提交试卷使用事务保证 answer_record、wrong_question、exam_record 一致
- 使用索引优化高频查询
- 使用 v_student_score 视图支持学生成绩查询
- 使用 answer_record 快照字段保存历史考试题目状态

## 测试账号

初始化脚本中的测试账号统一密码为 `123456`。

- 管理员：admin
- 教师：teacher01
- 学生：student01

## 本地编译

项目已在 `.mvn/local-settings.xml` 中固定使用 C 盘 Maven 仓库：

```text
C:/Users/ASUS/.m2/repository
```

编译命令：

```bash
mvn -o -DskipTests compile
```

如果后续新增依赖导致本地仓库缺失，再取消 `-o` 或手动下载依赖。

## 本地运行

1. 启动 MySQL 和 Redis。
2. 执行 `database/schema.sql`。
3. 执行 `database/init.sql`。
4. 按本机环境修改 `src/main/resources/application.yml` 的数据库账号密码。
5. 启动项目：

```bash
mvn spring-boot:run
```

6. 浏览器访问：

```text
http://localhost:8080/login.html
```

## 已实现接口

认证模块：

- `POST /api/auth/register` 用户注册，默认绑定 STUDENT
- `POST /api/auth/login` 用户登录，返回 JWT
- `GET /api/auth/userInfo` 当前用户信息
- `POST /api/auth/logout` 退出登录，删除 Redis Token

题库模块：

- `POST /api/questions` 新增题目
- `PUT /api/questions/{id}` 修改题目
- `DELETE /api/questions/{id}` 逻辑删除题目
- `GET /api/questions` 分页筛选题目
- `GET /api/questions/{id}` 查询题目详情

试卷模块：

- `POST /api/papers` 创建试卷
- `GET /api/papers` 查询试卷列表
- `GET /api/papers/{id}` 查询试卷详情
- `POST /api/papers/{paperId}/questions` 给试卷添加题目
- `DELETE /api/papers/{paperId}/questions/{questionId}` 移除试卷题目
- `GET /api/papers/{paperId}/questions` 查询试卷题目
- `PUT /api/papers/{paperId}/publish` 发布试卷并自动汇总总分

考试模块：

- `GET /api/exams/available` 学生查询可参加考试
- `POST /api/exams/{paperId}/start` 学生开始考试
- `GET /api/exams/{examId}/questions` 获取考试题目，不返回正确答案
- `POST /api/exams/{examId}/submit` 提交试卷，事务判分
- `GET /api/exams/my-scores` 查询我的成绩
- `GET /api/exams/{examId}/detail` 查询考试详情

错题与统计模块：

- `GET /api/wrong-questions` 学生查询错题本
- `GET /api/statistics/paper/{paperId}` 教师查询试卷统计
- `GET /api/statistics/paper/{paperId}/records` 教师查询试卷学生考试记录

## 开发顺序

1. 数据库设计：schema.sql、init.sql、索引、视图、ER 说明
2. 后端基础工程：统一返回、异常处理、MyBatis、跨域、Redis 配置
3. 登录注册模块：BCrypt、JWT、Redis Token、登录日志、限流
4. 题库管理模块
5. 试卷管理模块
6. 在线考试模块
7. 成绩、错题、统计模块
8. 原生 HTML + Ajax 前端页面
9. 测试用例、截图、课程设计报告材料
