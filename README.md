# Students-API #

## App overview ##

This is basically a [Spring Boot](https://spring.io/projects/spring-boot) REST API wich provides secured access services to students related data, the endpoints are secured via [JWT](https://jwt.io/) to authenticate in a stateless model, accessing data available in a memory database called [H2](https://www.h2database.com/html/main.html), and ready to run locally for development purposes, and deployed in a container environment. The API documentation was created 
using OpenAPI3. 

##  Architecture overview ##

  This API was built using 3 layers with Spring Boot as framework to provide 
  all the components,dependency injection to each layer and a http server.
  
* **Data Access Layer** The implementation is using a Spring component named [Spring Data Repositories](https://bitbucket.org/tutorials/markdowndemo)
  to access data entities mapped using JPA, the methods are all described in a @Repository interface 
  and extending [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html), wich provides basic methods and implementation for 
  query methods described in the interface.The database used for this task was H2 Database Engine, because it is small, simple and fit our needs for this purpose. 

* **Business Service Layer** The implementation is using Spring component named [Spring Services](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/stereotype/Service.html)
  to process all the business rules, integrations and access, modify or create data using the data access layer. 

* **API Interface Layer** The implementation is using Spring component named [Spring Rest Controllers](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html)
  to define the contracts for each service, exposing every end point using the most 
  adequate HTTP Methods, wich is very important to take all advantage of every http 
  method feature and meaning. This layer was protected using [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/) support for authentication 
  tokens and access control, so, the protected business services can only be accessed if an authentication was provided in the HTTP Request 'Authorization' Header, using the JWT model, created and signed by this API.

## Tests ##

  The web documentation contains integration tests, but i also wrote unit tests 
  using [JUnit](https://junit.org/junit5/), [Mockito](https://site.mockito.org/) and [Spring MockMVC](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html) to write integration tests.

## Source versioning ##

  For development of the features and generation of releases, i used [Gitflow](https://github.com/nvie/gitflow), it is a branching model that supports multiples teams and all phases from dev to qa and production.

##  Build and deploy ##

* **Build** This project have a pom.xml, used to describe its depencies and build via [Maven](https://maven.apache.org/).
  There are 3 Maven profiles defined to run the application in different environments: dev,tests 
  and deploy, that last can be dev, qa or production.
  
* **Deploy** Spring boot can package the application as JAR and has an embbeded tomcat to expose 
  our rest controller endpoint thru http protocol, very simple, fast and reliable. This project is actually built to be deployed in a Docker 
  container, using environment variables to set important properties values.

##  Documentation  ##
  Documentation was created using [OpenAPI 3.0 Spring implementation and UI](https://springdoc.github.io/springdoc-openapi-demos/), 
  it includes the description off all endpoints, including authentication scheme, using this UI, consumers can 
  check the API contracts in a pretty view and test it on the fly.

##  Setup and Running ##

### Pre-requisites ###
   
   *  Java8+
   *  Docker.

### Installing

   *  Download code from the task or access and clone the master branch of this [repository](https://bitbucket.org/alencarcanton/students-api), go into students-api folder and use maven to install dependencies and run tests:  
     ```
     mvnw install
     ```
   *  If you are using some unix dist, you will need to make the mvnw an executable and call:
     ```
     ./mvnw install
     ```
  
### Testing  

  *  To run unit and integration tests:

     ```
     mvnw test
     ```

### Running 
  - To deploy locally:
    - Go inside target folder and use java to run the app jar, replacing **YOUR_FREE_HTTP_PORT** for you http port number where the api will be run:  
      ```
      java -jar -Dspring.profiles.active=deploy -DAPI_SERVER_PORT=**YOUR_FREE_HTTP_PORT** students-api-1.0.2.jar  
      ```
    - Example:   
      ```
      java -jar -Dspring.profiles.active=deploy -DAPI_SERVER_PORT=8080 students-api-1.0.2.jar  
      ```
  - To deploy on docker:
    - Go inside students-api folder and build the docker image:  
      ```  
      docker build -t schoology/students-api .  
      ```  
    - Run the image in a new container replacing **YOUR_FREE_HTTP_PORT** for you http port number where the api will be run:  
      ```  
      docker run -p **YOUR_FREE_HTTP_PORT**:**YOUR_FREE_HTTP_PORT** -e API_SERVER_PORT=**YOUR_FREE_HTTP_PORT** -t schoology/students-api  
      ```  
    - Example:  
      ```  
      docker run -p 8085:8085 -e API_SERVER_PORT=8085 -t schoology/students-api  
      ```  

##  Testing with cURL ##

- Request a token:  
- Windows
  ```
  curl --header "Content-Type: application/json" --request POST --data {\"username\":\"manager\",\"password\":\"password\"} http://localhost:**YOUR_FREE_HTTP_PORT**/api/v1/authentication  
  ```
- Linux
  ```
  curl --header "Content-Type: application/json" --request POST --data '{"username":"manager","password":"password"}' http://localhost:**YOUR_FREE_HTTP_PORT**/api/v1/authentication  
  ```
- You will receive a response like this:  
  ```
  {"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiZXhwIjoxNTgxNDg1OTAzLCJpYXQiOjE1ODE0Njc5MDN9.eCI9rvNZ5Yo5g4yDgzDJP78FBb3jqnMklx08gWjxkk8aFqOeGRCIx4IZKCwcIo9UF-nonfQlWlur4K4b1zfrnA"}  
  ```
  
  Copy the value of the token field and use on Authorization header in the next requests.  

- Refreshing the token:   
  ```
  curl --header "Content-Type: application/json" --header "Authorization: Bearer **TOKEN_HERE**"--request GET http://localhost:**YOUR_FREE_HTTP_PORT**/api/v1/authentication  
  ```
- Example  
  ```
  curl --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiZXhwIjoxNTgxNDg1OTAzLCJpYXQiOjE1ODE0Njc5MDN9.eCI9rvNZ5Yo5g4yDgzDJP78FBb3jqnMklx08gWjxkk8aFqOeGRCIx4IZKCwcIo9UF-nonfQlWlur4K4b1zfrnA" --request GET http://localhost:8085/api/v1/authentication  
  ```

- Listing students:  
  ```
  curl --header "Content-Type: application/json" --header "Authorization: Bearer **TOKEN_HERE**" --request GET http://localhost:**YOUR_HTTP_PORT**/api/v1/students  
  ```
- Example  
  ```
  curl --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiZXhwIjoxNTgxNDg1OTAzLCJpYXQiOjE1ODE0Njc5MDN9.eCI9rvNZ5Yo5g4yDgzDJP78FBb3jqnMklx08gWjxkk8aFqOeGRCIx4IZKCwcIo9UF-nonfQlWlur4K4b1zfrnA" --request GET http://localhost:8085/api/v1/students  
  ```

- Filtering, limiting and paginating stundents:  
  ```
  curl --header "Content-Type: application/json" --header "Authorization: Bearer **TOKEN_HERE**" --request GET "http://localhost:**YOUR_HTTP_PORT**/api/v1/students?name=Alencar&page=1&limit=50"  
  ```
- Example  
  ```
  curl --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW5hZ2VyIiwiZXhwIjoxNTgxNDg1OTAzLCJpYXQiOjE1ODE0Njc5MDN9.eCI9rvNZ5Yo5g4yDgzDJP78FBb3jqnMklx08gWjxkk8aFqOeGRCIx4IZKCwcIo9UF-nonfQlWlur4K4b1zfrnA" --request GET "http://localhost:8085/api/v1/students?name=Alencar&page=1&limit=50"  
  ```
- OpenAPI Documentation
  To check the generated API documentation, access this url in your browser:
  ```
  http://localhost:**YOUR_FREE_HTTP_PORT**/swagger-ui.html
  ```
- Example
  ```
  http://localhost:8085/swagger-ui.html
  ```


## Tecnologies ##

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JWT](https://jwt.io/)
- [H2](https://www.h2database.com/html/main.html)
- [Spring Data Repositories](https://bitbucket.org/tutorials/markdowndemo)
- [CrudRepository](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
- [Spring Services](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/stereotype/Service.html)
- [Spring Rest Controllers](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html)
- [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Spring MockMVC](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)
- [Gitflow](https://github.com/nvie/gitflow)
- [Maven](https://maven.apache.org/)
- [OpenAPI 3.0 Spring implementation and UI](https://springdoc.github.io/springdoc-openapi-demos/)