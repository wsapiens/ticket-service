**********************************
*  Walmart Coding Assignment
*  TicketService Homework
*
*         Steve Park (11/20/2015)
**********************************

This ticket service allows user to create multiple holds and reservations for one customer.
Also this service will hold any available seat within levels user provided and return holdId. 
If available seats are not enough in one level, then hole available seat in next level. 
There is no limit on number of holding nor on number of seat to hold. This service will returns
seat holdId when it can hold at least one seat. So if user request 10 seats, but find 2 seat then
it holds the 2 seats and return seat holdId. However seat hold id can be empty if service cannot
find any available seat in the levels user provided.

This service is design for simple web service, but also provides command line test mode too.


===================================
1. Build Project By Maven
===================================

Run below command inside of this project folder, ticket-service-0.0.1.jar will be created under ./target 
inside of project directory

$ mvn clean install


* if want to build by gradle, run below command, ticket-service-0.0.1.jar will be created under ./build/libs
inside of project directory

$ gradle clean test jar bootRepackage






===================================
2. Run As Server Mode
===================================

This service is designed for web service as running server mode
For running this service with default configuration, just run below command in the folder which contains ticket-service-0.0.1.jar
Default port will be 9797 and hold expiration time is 120 seconds.


$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher


Once you start service by above command, it will generate database file ticket-service.mv.db
Service will be running as server mode by embed tomcat inside of ticket-service-0.0.1.jar
If you want to stop the service after starting, please do Ctrl+C on the console

Also if you want to change the default configuration (port number or expiration time), then please copy application.properties 
under /src/main/resources of this project to wherever you want and edit properties.
Below is example property change. change port to 9393 and seat hold expiration time to 3minute

[ application.properties ]
#
#   Ticket Service Property
#
server.port=${port:9393}
service.log.level=${log.level:DEBUG}
seat.hold.expire.second=${expire.time:180}


after changing property, run below command. (this case is application.properties and the .jar are in same folder and linux system)

$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log







===================================
3. Send Request to Service
===================================

After starting service as server mode, service will expect receiving request over HTTP.
If curl command is available, then you can send requests like below. If curl is not available, then REST client plugin for Chrome or FireFox
Browser can be used. All the requests will be handled asynchronously

-. request to find available seats
   endpoint: /ticket-service/v1/venue/{venueLevel}/available-seats

$ curl -X GET http://localhost:9797/ticket-service/v1/venue/3/available-seats
{"venueLevel":3,"numberOfAvailableSeats":600}



-. request to find and hold seats
   endpoint: /ticket-service/v1/hold/num-seats/{numberOfSeats}/email/{customerEmail}/venue?minLevel={minLevel}&maxLevel={maxLevel}
   minLevel and maxLevel are queryparam and optional. If no minLevel, it will search from 1 (Orchestra). also If no maxLevel, 
   then it will search to 4 (Balcony 2). If response take some time, it will return later

$ curl -X POST http://localhost:9797/ticket-service/v1/hold/num-seats/900/email/homer@simpson.com/venue?minLevel=1&maxLevel=3
[1] 78454
$ {"holdId":50,"customerEmail":"homer@simpson.com","details":[{"venueLevel":1,"numOfSeats":900}]}


$ curl -X POST http://localhost:9797/ticket-service/v1/hold/num-seats/20/email/homer@simpson.com/venue?minLevel=3
{"holdId":51,"customerEmail":"homer@simpson.com","details":[{"venueLevel":3,"numOfSeats":20}]}



-. request to reserve seat by holdId
   endpoint: /ticket-service/v1/hold/{holdId}/email/{customerEmail}/reserve
   If reservation finished successfully, it will return confirmationCode


$ curl -X POST http://localhost:9797/ticket-service/v1/hold/51/email/homer@simpson.com/reserve
{"holdId":51,"customerEmail":"homer@simpson.com","confirmationCode":"787bff5f-ed20-33bc-949d-e49fa52ac38c"}


However seat hold is expired or customerEmail is not matched for the seatHold, it will return error message




spark-mbp:~ spark$ curl -X POST http://localhost:9797/ticket-service/v1/hold/50/email/homer@simpson.com/reserve
{"timestamp":1447992953182,"status":404,"error":"Not Found","exception":"com.walmart.ticketservice.error.SeatHoldNotFoundException","message":"no such hold","path":"/ticket-service/v1/hold/50/email/homer@simpson.com/reserve"}






===================================
4. Run As Test Mode - Command Line
===================================


