# TicketService
This is a simple ticket service app that can display available seats, hold a number of seats for the customer, and reserve a held seat. 

#Assumptions
1. Security support is out of scope in this app. All rest endpoints and their services are wide open. 
2. High scalability support is out of scope in this app. The app is not designed to support high volume traffic, or highly concurrent situation. 
3. UI is also out of scope.. Only backend is implemented in this app. 
4. Unit test coverage does not need to be perfect. 

# Design consideration
 1. This app is based on <b>Spring Boot</b> framework since Spring boot provides many rich features out of the box. A lot of 
 boilerplate code are avoid by using Spring framework. 
 2. Spring Data is chosen to persist the data due to its simplicity. 
 3. This app uses the typical layered architectures: dao, service, controller. 
 4. Aspect is used to clean up the expired seat holds. To me this task is a cross cutting concern so aspect is used. 

# Build Instruction
I choose Maven as the build tool. 

Run below command to generate the executable jar file:<br>
<b>mvn clean install</b>

Now navigate to the <b>target</b> directory, and run the below comand to start up the app:<br>
<b>java -jar TicketService-0.0.1-SNAPSHOT.jar</b>


# Database
The application is designed to run on any relational database. A in-memory <b>H2 Database</b> is embedded when the app starts up. Therefore the user 
does not need to install any additional database to try out this app. 

Standard abbreviations are used for any database table name, or column name. For example, "CLMN" is for "COLUMN" in SEAT table, 
"RSRVD_FL" is for "RESERVATION_FLAG" in SEAT_HOLD table. You can find many examples in the entity classes in <b>model</b> package. 


# Unit test
1. <b>Spring MockMvc</b> is chosen to test rest controllers. It gives us the ability to verify the request headers, reques/response body as well as the status code.
2. <b>Mockito</b> is used to for all unit test classes. <br>

#Rest APIs
Besides all the endpoints offered by Spring Boot framewwork, Ticket Service provides 4 APIs ont its own. You can either use curl, or Postman to try them. 
Here is the base url:<br>
<b>http://localhost:8090/TicketService/v1</b>

<br>All the APIs are described below:
1. creates a venue with certain number of row and columns. <br>
<b>URL: /venues/rows/{rowNumber}/columns/{column number}</b> <b>POST</b> <br>
<b>Request Body</b>: N/A

2. Find all available seats. <br>
<b>URL: /seats GET</b> <br>
<b>Request Body</b>: N/A

3. Find and hold the best available seats for a customer.<br>
<b>URL: /holds POST</b> <br>
<b>Request Body</b>: {
                     	"numberOfSeats":5,
                     	"email": "James.Zhou@test.org"
                     }
4. Reserve and commit to a specific group of holds for a customer. <br>
<b>URL: /reservations POST</b> <br>
<b>Request Body</b>: {
                     	"seatHoldId":3,
                     	"email": "James.Zhou@test.org"
                     }<br>
                     
#Error Handling
TicketService translates the erroneous conditions into proper https status code so the user understands what went wrong. Also the app always logs
the whole exception stack for easy troubleshooting purpose. Please refer to class <b>RestControllerErrorHandler.java</b> for more information<br>
 
 Here are some example mappings between exceptions and http statuses:<br>
 <b>ResourceNotFoundException <-> 404
 ExceedLimitException <-> 509
 HibernateOptimisticLockingFailureException <-> 400
 IllegalArgumentException <-> 400
 Generic Exception <-> 500
 
 