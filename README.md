Steps to run the application(Windows):
1. Download the project
2. Install Java 17, https://www.oracle.com/java/technologies/downloads/#jdk17-windows
3. Install Maven 3.8.6, https://maven.apache.org/download.cgi
4. Remember to update your environment variables for Java and Maven
5. Open command line, cd to the project
6. Use command "mvn spring-boot:run" to run the application
7. You can use Postman or other tools to test the application

You can also use IntelliJ IDEA to run the application:
1. Download the project
2. Download IntelliJ IDEA
3. Import it as a Maven project
4. Use IntelliJ IDEA to run the application 

APIs:
1. Add transaction  
POST http://localhost:8080/transactions
2. Spend points  
POST http://localhost:8080/points
3. Get balance  
GET http://localhost:8080/balances

