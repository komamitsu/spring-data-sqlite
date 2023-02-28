# Example application

This is an example application for Spring Data integration with SQLite and also used for testing if the integration works with a JVM application via jar dependencies.

## Preparation

This project depends on local maven repository for Spring Data integration with SQLite components. Please execute the following command under `spring-data-sqlite` project root.

```
$ pwd
/home/me/src/spring-data-sqlite
$ ./gradlew publishToMavenLocal
```

## Execution

The following command executes this application that invokes some features of the integration.

```
$ ./gradlew run
```
