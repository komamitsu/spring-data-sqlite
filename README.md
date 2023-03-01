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

## Repository class

This library provides `SqliteRepository` that inherits PagingAndSortingRepository and has 2 new APIs `insert()` and `update()`. Spring Data JDBC basically requires users to use auto generated ID columns. If users want to use non auto generated ID column, there are [some workarounds](https://spring.io/blog/2021/09/09/spring-data-jdbc-how-to-use-custom-id-generation), but they're not perfect solutions. The 2 new APIs addresses the problem.

```java
public interface CarRepository extends SqliteRepository<Car, Integer> {}
```

```java
    Car car = new Car(assignedCarId, "my new car");
    carRepository.insert(car);
       :
    carRepository.update(modifiedCar);
```


## Example application

[Example application](../example) is a JVM application that uses this integration. It only serves as a reference and does not necessarily meet production code standards.

