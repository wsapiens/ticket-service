**********************************
*  Walmart Coding Assignment
*  TicketService Homework
*
*  Steve(Sang Ki) Park 11/20/2015
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


Once you start service by above command, it will generate database file ticket-service.mv.db (H2 database file)
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

1) request to find available seats
   endpoint: /ticket-service/v1/available-seats/venue?level={venueLevel}

$ curl -X GET http://localhost:9797/ticket-service/v1/available-seats/venue?level=3
{"venueLevel":3,"numberOfAvailableSeats":600}


*level is queryParam and optional, so you can omit the param and it will give total available seats through whole levels

$ curl -X GET http://localhost:9797/ticket-service/v1/available-seats/venue
{"venueLevel":null,"numberOfAvailableSeats":5350}



2) request to find and hold seats
   endpoint: /ticket-service/v1/hold/num-seats/{numberOfSeats}/email/{customerEmail}/venue?minLevel={minLevel}&maxLevel={maxLevel}
   minLevel and maxLevel are queryParam and optional. So you can omit any or both of queryParams.
   If no minLevel is given, it will search from 1 (Orchestra). also If no maxLevel, 
   then it will search up to 4 (Balcony 2). If response take some time, it will return later asynchronously

$ curl -X POST http://localhost:9797/ticket-service/v1/hold/num-seats/900/email/homer@simpson.com/venue?minLevel=1&maxLevel=3
[1] 78454
$ {"holdId":50,"customerEmail":"homer@simpson.com","details":[{"venueLevel":1,"numOfSeats":900}]}


$ curl -X POST http://localhost:9797/ticket-service/v1/hold/num-seats/20/email/homer@simpson.com/venue?minLevel=3
{"holdId":51,"customerEmail":"homer@simpson.com","details":[{"venueLevel":3,"numOfSeats":20}]}


* if you are on windows system with cygwin, then suggest to wrap the url by double quote

C:\>curl -X POST "http://localhost:9797/ticket-service/v1/hold/num-seats/900/email/homer@simpson.com/venue?minLevel=1&maxLevel=4"
{"holdId":61,"customerEmail":"homer@simpson.com","details":[{"venueLevel":2,"numOfSeats":900}]}



3) request to reserve seat by holdId
   endpoint: /ticket-service/v1/hold/{holdId}/email/{customerEmail}/reserve
   If reservation finished successfully, it will return confirmationCode


$ curl -X POST http://localhost:9797/ticket-service/v1/hold/51/email/homer@simpson.com/reserve
{"holdId":51,"customerEmail":"homer@simpson.com","confirmationCode":"787bff5f-ed20-33bc-949d-e49fa52ac38c"}


However seat hold is expired or customerEmail is not matched for the seatHold, it will return error message

(seat hold expired or no hold found)
$ curl -X POST http://localhost:9797/ticket-service/v1/hold/52/email/homer@simpson.com/reserve
{"timestamp":1447996898576,"status":404,"error":"Not Found","exception":"com.walmart.ticketservice.error.SeatHoldNotFoundException","message":"no such hold","path":"/ticket-service/v1/hold/52/email/homer@simpson.com/reserve"}

(customer validation fail)
$ curl -X POST http://localhost:9797/ticket-service/v1/hold/51/email/bart@simpson.com/reserve
{"timestamp":1447996837793,"status":400,"error":"Bad Request","exception":"com.walmart.ticketservice.error.CustomerValidationException","message":"email is not matching","path":"/ticket-service/v1/hold/51/email/bart@simpson.com/reserve"}



4) reset database with removing all the hold and customer data
 for testing convenience, admin endpoint is available to clean up all the seat holds and customer info.

$ curl -X DELETE http://localhost:9797/admin/seat-holds
All SeatHolds with Customer Info have been deleted!





===================================
4. Run As Test Mode - Command Line
===================================

This service also provides command line test mode to test this service without using server mode and internet.
Below is example command with command line properties. log line and result will come in same console
This is just for running the service in command line, but it initiate whole spring context and other resources again
for every command and destroy them. So it is not efficient way to test the service.

1) command to find available seats
   command line property: --mode=test --action=search --level={venueLevel}

$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log --mode=test --action=search --level=2
2015-11-19 23:32:57.248  INFO   --- [           main] com.walmart.ticketservice.Application    : run in command line test mode
2015-11-19 23:32:58.489  INFO   --- [           main] org.eclipse.jetty.util.log               : Logging initialized @2258ms
2015-11-19 23:33:01.789  INFO   --- [           main] o.f.c.i.dbsupport.DbSupportFactory       : Database: jdbc:h2:file:./ticket-service (H2 1.4)
2015-11-19 23:33:01.887  INFO   --- [           main] o.f.core.internal.command.DbValidate     : Validated 2 migrations (execution time 00:00.040s)
2015-11-19 23:33:01.910  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "PUBLIC": 2
2015-11-19 23:33:01.911  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Schema "PUBLIC" is up to date. No migration necessary.
2015-11-19 23:33:07.673  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqIn ] TicketServiceImpl ( int com.walmart.ticketservice.service.TicketService.numSeatsAvailable(Optional) )
2015-11-19 23:33:08.212  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqOut] TicketServiceImpl ( int com.walmart.ticketservice.service.TicketService.numSeatsAvailable(Optional) )
Number Of Available Seats: 2000

*level is optional, so you can run this only with --mode=test --action=search



2) command to find and hold seats
   command line property: --mode=test --action=hold --numSeats={numSeats} --minLevel={minLevel} --maxLevel={maxLevel} --email={customerEmail}

$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log --mode=test --action=hold --numSeats=20 --minLevel=1 --maxLevel=3 --email=homer@simpson.com
2015-11-19 23:36:29.940  INFO   --- [           main] com.walmart.ticketservice.Application    : run in command line test mode
2015-11-19 23:36:31.303  INFO   --- [           main] org.eclipse.jetty.util.log               : Logging initialized @2416ms
2015-11-19 23:36:34.647  INFO   --- [           main] o.f.c.i.dbsupport.DbSupportFactory       : Database: jdbc:h2:file:./ticket-service (H2 1.4)
2015-11-19 23:36:34.744  INFO   --- [           main] o.f.core.internal.command.DbValidate     : Validated 2 migrations (execution time 00:00.039s)
2015-11-19 23:36:34.768  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "PUBLIC": 2
2015-11-19 23:36:34.769  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Schema "PUBLIC" is up to date. No migration necessary.
2015-11-19 23:36:41.095  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqIn ] TicketServiceImpl ( SeatHold com.walmart.ticketservice.service.TicketService.findAndHoldSeats(int,Optional,Optional,String) )
2015-11-19 23:36:41.527  INFO   --- [           main] c.w.t.service.TicketServiceImpl          : create new customer by email: homer@simpson.com
2015-11-19 23:36:41.651  INFO   --- [           main] c.w.t.service.TicketServiceImpl          : hold 20 seats for email: homer@simpson.com
2015-11-19 23:36:41.652  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqOut] TicketServiceImpl ( SeatHold com.walmart.ticketservice.service.TicketService.findAndHoldSeats(int,Optional,Optional,String) )
Seat HoldId: 150

*minLevel and maxLevel are optional, so you can omit any or both of those like --mode=test --action=hold --numSeats=20 --minLevel=3 --email=homer@simpson.com



3) command to reserve seat by holdId
   command line property: --mode=test --action=reserve --holdId={seatHoldId} --email={customerEmail}

$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log --mode=test --action=reserve --holdId=150 --email=homer@simpson.com
2015-11-19 23:38:16.010  INFO   --- [           main] com.walmart.ticketservice.Application    : run in command line test mode
2015-11-19 23:38:17.308  INFO   --- [           main] org.eclipse.jetty.util.log               : Logging initialized @2326ms
2015-11-19 23:38:20.483  INFO   --- [           main] o.f.c.i.dbsupport.DbSupportFactory       : Database: jdbc:h2:file:./ticket-service (H2 1.4)
2015-11-19 23:38:20.603  INFO   --- [           main] o.f.core.internal.command.DbValidate     : Validated 2 migrations (execution time 00:00.052s)
2015-11-19 23:38:20.681  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "PUBLIC": 2
2015-11-19 23:38:20.683  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Schema "PUBLIC" is up to date. No migration necessary.
2015-11-19 23:38:26.567  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqIn ] TicketServiceImpl ( String com.walmart.ticketservice.service.TicketService.reserveSeats(int,String) )
2015-11-19 23:38:26.763  INFO   --- [           main] c.w.t.service.TicketServiceImpl          : Reserved Seat for email: homer@simpson.com, seatHoldId: 150, confirmationCode: 1d8e4d3a-5499-310f-a12f-e014568e0886
2015-11-19 23:38:26.763  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqOut] TicketServiceImpl ( String com.walmart.ticketservice.service.TicketService.reserveSeats(int,String) )
Reservation Confirmation Code: 1d8e4d3a-5499-310f-a12f-e014568e0886



-. also it will show error if hold is expired or customerEmail is not matched

(seat hole expired)
$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log --mode=test --action=reserve --holdId=200 --email=homer@simpson.com
2015-11-19 23:44:26.972  INFO   --- [           main] com.walmart.ticketservice.Application    : run in command line test mode
2015-11-19 23:44:28.251  INFO   --- [           main] org.eclipse.jetty.util.log               : Logging initialized @2372ms
2015-11-19 23:44:31.463  INFO   --- [           main] o.f.c.i.dbsupport.DbSupportFactory       : Database: jdbc:h2:file:./ticket-service (H2 1.4)
2015-11-19 23:44:31.553  INFO   --- [           main] o.f.core.internal.command.DbValidate     : Validated 2 migrations (execution time 00:00.032s)
2015-11-19 23:44:31.572  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "PUBLIC": 2
2015-11-19 23:44:31.573  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Schema "PUBLIC" is up to date. No migration necessary.
2015-11-19 23:44:37.601  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqIn ] TicketServiceImpl ( String com.walmart.ticketservice.service.TicketService.reserveSeats(int,String) )
2015-11-19 23:44:37.754 ERROR   --- [           main] c.w.t.service.TicketServiceImpl          : fail on reservation, the SeatHold is expired, seatHoldId: 200
2015-11-19 23:44:37.755  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqErr] TicketServiceImpl ( String com.walmart.ticketservice.service.TicketService.reserveSeats(int,String) ) - fail on reservation, the SeatHold is expired, seatHoldId: 200
fail on reservation, the SeatHold is expired, seatHoldId: 200


(customer validation fail)
$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log --mode=test --action=reserve --holdId=150 --email=marge@simpson.com
2015-11-19 23:39:53.272  INFO   --- [           main] com.walmart.ticketservice.Application    : run in command line test mode
2015-11-19 23:39:54.713  INFO   --- [           main] org.eclipse.jetty.util.log               : Logging initialized @2567ms
2015-11-19 23:39:58.047  INFO   --- [           main] o.f.c.i.dbsupport.DbSupportFactory       : Database: jdbc:h2:file:./ticket-service (H2 1.4)
2015-11-19 23:39:58.164  INFO   --- [           main] o.f.core.internal.command.DbValidate     : Validated 2 migrations (execution time 00:00.044s)
2015-11-19 23:39:58.192  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "PUBLIC": 2
2015-11-19 23:39:58.193  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Schema "PUBLIC" is up to date. No migration necessary.
2015-11-19 23:40:04.548  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqIn ] TicketServiceImpl ( String com.walmart.ticketservice.service.TicketService.reserveSeats(int,String) )
2015-11-19 23:40:04.688 ERROR   --- [           main] c.w.t.service.TicketServiceImpl          : Customer Eamil Validation on SeatHold fail, seatHoldId: 150, customerEmail: marge@simpson.com
2015-11-19 23:40:04.688  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqErr] TicketServiceImpl ( String com.walmart.ticketservice.service.TicketService.reserveSeats(int,String) ) - Customer Eamil Validation on SeatHold fail, seatHoldId: 150, customerEmail: marge@simpson.com
Customer Eamil Validation on SeatHold fail, seatHoldId: 150, customerEmail: marge@simpson.com



4) command to clean up seat hold data and customer info on database, for test convenience.
   command line property: --mode=test --action=reset

$ java -cp ticket-service-0.0.1.jar org.springframework.boot.loader.JarLauncher --spring.config.location=./application.properties --logging.file=./link.log --mode=test --action=reset
2015-11-19 23:45:41.213  INFO   --- [           main] com.walmart.ticketservice.Application    : run in command line test mode
2015-11-19 23:45:42.786  INFO   --- [           main] org.eclipse.jetty.util.log               : Logging initialized @2657ms
2015-11-19 23:45:46.010  INFO   --- [           main] o.f.c.i.dbsupport.DbSupportFactory       : Database: jdbc:h2:file:./ticket-service (H2 1.4)
2015-11-19 23:45:46.108  INFO   --- [           main] o.f.core.internal.command.DbValidate     : Validated 2 migrations (execution time 00:00.040s)
2015-11-19 23:45:46.140  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "PUBLIC": 2
2015-11-19 23:45:46.141  INFO   --- [           main] o.f.core.internal.command.DbMigrate      : Schema "PUBLIC" is up to date. No migration necessary.
2015-11-19 23:45:52.272  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqIn ] ResetServiceImpl ( void com.walmart.ticketservice.service.ResetService.reset() )
2015-11-19 23:45:52.273  INFO   --- [           main] c.w.t.service.ResetServiceImpl           : received reset request, so clean up seatHold and customer tables on database
2015-11-19 23:45:52.909  INFO   --- [           main] c.w.ticketservice.aop.ServiceTracker     : [ReqOut] ResetServiceImpl ( void com.walmart.ticketservice.service.ResetService.reset() )
Datbase has been cleaned up with removing all hold, reservation and customer info


* if want to run with default configuration, then you can omit --spring.config.location property in the command line
