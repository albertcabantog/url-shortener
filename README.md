# README #

A REST web service application to generate URL shortener

## Prerequisite ##
The application is written in Java and Spring framework.  The following should be installed to build and run the application:

* Java 17
* Maven 3.9.4
* Git 2.42 or higher

## Build ##

* Clone this code base

```
git clone https://github.com/albertcabantog/url-shortener.git
```

* Run Maven inside the url-shortener directory to build the application without executing the unit tests

```
mvn clean install -DskipTests
```

## Unit tests ##

All unit test will be executed and the subsequent report will be generated.  Here is to run the test:

* Run `mvn test`
* Test reports will be written in `target/surefire-reports`

## Deployment ##

The deployment of the application allows access to the web service APIs.  
Below are the steps to deploy and run the application:

* Run `mvn spring-boot:run`
* Open a browser and load `http://localhost:8080/swagger-ui.html`

## Try the REST service ##

Loading the swagger UI page will list all the REST service operations.  
It documents each of the API including the error messages and code, allowed data entry, required parameters, 
description of the operation, and allows for the actual call to the REST service.

Let's try `/api/url/` by clicking on the POST button.  
Click on the `Try it out!` button and enter the URL to generate a shorten URL code.  
Click on the `Execute` button, this will send the request to the server and return the response.  
Under the `Response Body` it will display the shorten URL.

After generating a shortened URL, this can be used to redirect to the original URL.  Open a new browser tab and use the
shortened URL code in this format `http://localhost:8080/<shortened-url-goes-here>`

## In-memory database and performance ##

The application is using an embedded in-memory H2 database to store the shorten URL code and the original URL.  
This database is refreshed every time the application is started and all data will be lost once the application is stopped.

The application is also equipped with caching mechanism to improve performance and reduce the latency calling the database.
