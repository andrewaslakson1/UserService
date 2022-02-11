#syntax=docker/dockerfile:1

FROM maven:3.8.4-openjdk-11 as base

WORKDIR /app

COPY .mvn/ .mvn
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src

FROM base as test
RUN ["mvn", "test", "-Dspring.profiles.active=test"]

From base as dev
CMD ["mvn", "spring-boot:run", "-Dspring.profiles.active=dev"]

From base as build
RUN ["mvn", "package", "-Dmaven.test.skip"]

From openjdk:11-jre-slim as production
CMD ["mvn", "spring-boot:run", "-Dspring.profiles.active=dev"]
EXPOSE 5000

COPY --from=build /app/target/User-Service*.jar /User-Service.jar
