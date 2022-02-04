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
- Spring Cloud
- Eureka
- Prometheus
- Jacoco
- Lombok

-------------------------
Running Instructions
-------------------------

-------------------------
Database Structure
-------------------------
<h3>Firebase</h3>
<h5>Principal:</h5>
- user_id PRIMARY KEY
- user_email
- user_password

<h5>Notes:</h5>
- used for authentication
- encrypted principal object will be taken in as JWT from firebase and must be used with all requests to API!
-------------------------


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
--------------------------
