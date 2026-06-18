# 项目实现统一口径

## 项目名称

基于 SpringBoot 的在线考试与题库管理系统。

## 项目定位

本项目是数据库原理课程设计，不是大型在线教育平台。开发重点是数据库设计、外键关系、事务控制、多表关联查询、视图、索引、系统测试和课程设计报告。

## 技术边界

使用 Spring Boot、MyBatis、MySQL、Lombok、JWT、BCrypt、Redis、HTML、CSS、JavaScript、Ajax。

不加入微服务、Docker、消息队列、Elasticsearch、MinIO、爬虫、短信验证码、人脸识别、防作弊监控、AI 判题等超出课程设计范围的内容。

Redis 只用于：

1. 登录 Token 状态管理。
2. 用户退出登录时主动失效 Token。
3. 修改密码、禁用用户时清理 Token。
4. 登录失败次数限制。
5. 注册频率限制。

Redis 不用于缓存题库、试卷、成绩等核心业务数据。

## 登录注册规则

普通注册默认绑定 STUDENT 角色。代码不硬编码角色 ID，而是读取默认角色编码 STUDENT，并根据 sys_role.role_code 查询角色 ID，再写入 sys_user_role。

ADMIN 和 TEACHER 账号由 init.sql 初始化，或后续由管理员后台创建。

密码使用 BCrypt 加密。登录成功后生成 JWT，JWT 包含 userId、username、roles、jti、exp。后端每次请求同时校验 JWT 和 Redis 中的 jti。

## 数据库规则

1. 用户和角色通过 sys_user_role 多对多关联。
2. 试卷和题目通过 paper_question 多对多关联。
3. 题目删除采用逻辑删除，将 question.status 置为 0。
4. 一名学生对同一张试卷只允许一条考试记录。
5. 获取考试题目接口不能返回 correct_answer 和 analysis。
6. 多选题答案统一保存为 A,B,C 格式，后端判分前排序比较。
7. 发布试卷时根据 paper_question.question_score 自动汇总 total_score。
8. 提交试卷必须使用 @Transactional。

## 开发阶段

1. 数据库设计：schema.sql、init.sql、视图、索引、ER 说明。
2. 后端基础工程：pom.xml、application.yml、统一返回、异常处理、MyBatis、跨域、Redis 配置。
3. 登录注册模块：注册、登录、JWT、Redis Token、登录日志、限流。
4. 题库管理模块：新增、修改、逻辑删除、分页筛选。
5. 试卷管理模块：创建试卷、组题、发布、查看试卷题目。
6. 在线考试模块：可参加考试、开始考试、获取题目、提交判分、事务保存。
7. 成绩错题统计模块：成绩列表、考试详情、错题本、试卷统计。
8. 前端页面：HTML + Ajax 完成主流程演示。
9. 测试与报告：测试用例、截图、核心代码、关键 SQL、Git 记录、报告。

