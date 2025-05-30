FROM openjdk:17-jdk

COPY app-api/build/libs/*SNAPSHOT.jar app-api.jar

ENTRYPOINT ["java", "-jar", "app-api.jar", "--spring.profiles.active=local"]