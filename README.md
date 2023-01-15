# Spring Data JDBC integration for SQLite

This is a Spring Data JDBC integration for SQLite. The basic usage follows [Spring Data JDBC - Reference Documentation](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/).

## Install

For Gradle
```
    implementation "org.komamitsu:spring-data-sqlite:1.0.0"
```

For Maven
```
<dependency>
    <groupId>org.komamitsu</groupId>
    <artifactId>spring-data-sqlite</artifactId>
    <version>1.0.0</version>
</dependency>

```

## Configurations

### spring.datasource.url

For instance,

```
spring.datasource.url=jdbc:sqlite:our-app.db
```

## Annotations

`@EnableSqliteRepositories` annotation is needed on the JVM application to use this integration as follows

```java
@SpringBootApplication
@EnableSqliteRepositories
public class MyApplication {
  @Autowired GroupRepository groupRepository;
  @Autowired UserRepository userRepository;

  :
```

## Example application

[Example application](../example) is a JVM application that uses this integration. It only serves as a reference and does not necessarily meet production code standards.

