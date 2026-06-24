# Troubleshooting

## Maven Cannot Resolve Dependencies

If offline compilation fails, run Maven once without `-o` so it can download Spring Boot, MyBatis, JWT, Lombok, and MySQL dependencies:

```bash
mvn -DskipTests compile
```

After dependencies are cached locally, offline compilation can be retried:

```bash
mvn -o -DskipTests compile
```

## MySQL Connection Fails

Check these items:

- The `exam_system` database exists.
- `database/schema.sql` has been executed.
- `database/init.sql` has been executed.
- `src/main/resources/application.yml` uses the correct username and password.
- MySQL allows local connections on port `3306`.

## Redis Connection Fails

Check these items:

- Redis is running before the Spring Boot application starts.
- Redis is listening on `localhost:6379`.
- The selected Redis database index is available.

## Login Token Is Invalid

Check these items:

- The request includes the `Authorization` header.
- The header value starts with `Bearer `.
- The token has not expired.
- Redis has not been restarted after login.

## Static Page Requests Fail

Open the project through the Spring Boot server instead of opening HTML files directly:

```text
http://localhost:8080/login.html
```
