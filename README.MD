This application was written by Andrew Aslakson
- Built to run on minikube using kubernetes

- General race tracking system?

- Secure with Firebase Authentication?

- Build React app too? - realistically probably just postman.

------------------------
Tech Stack
------------------------
- Java 11
- Maven
- Spring-Boot
- Prometheus
- Jacoco
- Lombok

-------------------------
Running Instructions
-------------------------
- tests "mvn clean test -Dspring.profiles.active=test"
  - this generates jacoco reports and runs all Unit and Integration tests
  - test profile uses H2 database, and populates it with 5 test users
- local Environment: Uses h2 database, used while developing
- Dev Environment: meant to be for kubernetes, most likely connects to DB hosted on kubernetes
  - Not yet actually configured
- h2-console: configured to be at: "localhost:5000/h2-console", URL: "jdbc:h2:mem:local"
  - Username: admin
  - password: password
- Prometheus Statistics: reached at: "localhost:5000/actuator/prometheus"

-------------------------
Database Structure
-------------------------
![Database Diagram](https://github.com/andrewaslakson1/UserService/blob/main/src/main/resources/documentation/MicroService_Test_API_DB_Config.png?raw=true)

-------------------------
Production History
-------------------------
-------------------------
02-04-2022
-------------------------
- Initialized Project
- Created Git Repository
- Added POM dependencies
- Created Driver class (Just starts Spring Boot Application)
- Added ERD Documentation
- Refactored to just be UserService
- Added Basic CRUD operations
--------------------------
--------------------------
02-08-2022
--------------------------
- Finished MetricsAspect
- Finished LoggingAspect
- Added more Exceptions to Exception Handler
- h2-console and prometheus metrics are available on endpoints
---------------------------
---------------------------
02-09-2022
---------------------------
- Added Unit Testing For Service Layer Logic
- Did a lot more reconfiguring of things today
---------------------------
---------------------------
02-10-2022
---------------------------
- Added integration testing
- finished configuring local and test profiles
- Changed UserId to be GenerationStrategy.IDENTITY
---------------------------
