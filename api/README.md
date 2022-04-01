# UAS Lifecycle Management: API Documentation
UAS Lifecycle Management API is the Spring Boot and MySQL backend project providing JSON responses to both the Android and Web products.<br><br>
[![API Pipeline](https://git.cardiff.ac.uk/c1989132/uas-lifecycle-management/badges/dev/pipeline.svg)](https://git.cardiff.ac.uk/c1989132/uas-lifecycle-management/-/commits/dev)
[![coverage report](https://git.cardiff.ac.uk/c1989132/uas-lifecycle-management/badges/dev/coverage.svg)](https://git.cardiff.ac.uk/c1989132/uas-lifecycle-management/-/commits/dev)
# Contents
- [Spring Profiles and Database Configuration](#spring-profiles-and-database-configuration)
- [Set Up Instructions](#set-up)
- [Libraries and Tools Used](#libraries-and-tools-used)
- [Framework Diagrams](#framework-diagrams)
- [Features and Mappings](#features)
- [Testing](#testing)
  - [Unit Testing](#unit)
  - [Performance Testing](#performance)
- [Deployment](#deployment)

# Spring Profiles and Database Configuration
There are currently three Spring Boot profiles in the application: DEV, UAT and PROD.<BR>
DEV is the active set profile for development locally, and makes use of the MySQL database on localhost.<br>
UAT is the environment which mimics PROD without the risk of damaging the live product. This profile can be used before releasing/versioning the software and testing before a demo.<br>
PROD is the production environment where the live version of the API is deployed, and is suitable for client testing and the demo.

Profile | Associated Properties File | Abbreviation | Database Details | API Url
--- | --- | --- | --- | --- |
Development | application-dev.properties | dev | localhost:3306 | localhost:8080
UAT | application-uat.properties | uat | eu-cdbr-west-02.cleardb.net/heroku_79993a57f62c974 | uastest.herokuapp.com
Production | application-prod.properties | prod | eu-cdbr-west-02.cleardb.net/heroku_b6aaae960a11e20 | uasprod.herokuapp.com <br>
# Set Up
Before running the project, please make sure you have the following installed on your machine:
1. [IntelliJ](#intellij) (or another IDE - please see Installing Gradle if not using IntelliJ)
2. [Java 11 and the path variables](#java-11)
3. [MySQL Workbench and/or MySQL Client](#mysql-workbenchclient)
4. [Gradle](#gradle)

## IntelliJ
IntelliJ is used to modify the code base, run checkstyle, run tests and to also run the gradle task buildAndReport which provides the full build and test script configured on the dev branch in the repository.<br>
IntelliJ can be downloaded [here]() where you can also find the set up instructions.
## Java 11
Before running under any of the Spring profiles, please make sure that you have at least Java 11 installed and the environment variables configured on your machine so that you are able to run "java -jar".<br>
[Installation instructions for Java 11]()<br>
[Setting environment variables]()
## MySQL Workbench/Client
Please also make sure that you have MySQL Workbench and/or MySQL client installed on your machine. You will need this for database connections.<br>
[Installation instructions for MySQL]()
## Gradle 
Gradle is the build tool used on this project. It creates the jar file, and also defines the checkstyle, test and buildAndReport tasks. You will need this if you wish to modify the code and make a new jar file to release/run and you aren't using IntelliJ.
[Installation instructions for gradle]()

## Running Locally
The API is set up to handle both the MariaDB and MySQL database providers. <br>To create the database locally, please do the following:
1. Navigate to where the source zip file is located and extract it.
2. Locate the ["database.sql"](src/main/resources/database.sql) file - api > src > main > resources > database.sql
3. Open MySQL Workbench and open the sql file
4. Run the sql file - lightning bolt icon, third from the left.

Provided there are no errors on running the script, you can now run the API locally:
### Run in IntelliJ
1. Open the project in intellij
2. If the notification "gradle build found in project - api" comes up, select the api module
3. Navigate to the main class - api > src > main > java > ApiApplication
4. Right click on this and choose the option 'Run ApiApplication' (or Ctrl + Shift + F10 if on Windows)
5. You should see the build log appear in the 'Run' tab at the bottom of the IntelliJ window.
### Run the jar file
1. Open project in file explorer
2. Navigate to <update this to include jar file>
3. Type cmd in the search bar and enter
4. Run the command "java -jar api-0.0.1-SNAPSHOT.jar"
5. Provided there are no errors, the API is now up and running and can be used locally.
## Running in UAT
1. Open project in intellij (or other IDE)
2. Change Spring boot profile property to uat
3. Run gradle jar
4. Run jar from command line

This will run the api locally but with the UAT DB.<br>
To test against UAT live API, change the property for the retrofit client in android to: uastest.herokuapp.com and in web: <update me>

## Running in PROD
1. Open project in intellij (or other IDE)
2. Change Spring boot profile property to prod
3. Run gradle jar
4. Run jar from command line

This will run the api locally but with PROD DB.<br>
To test against PROD live API, change the property for the retrofit client in android to: uasprod.herokuapp.com and in web: <update me>

## Mock Users:
There are 5 users in the database which will allow you to log on on both web and android end to experience the application.

Account Type | Email | Password |
--- | --- | --- |
CEO | ceo@test.com | password
COO | coo@test.com | password | 
CTO | cto@test.com | password | 
LO | logistic@test.com | password |
General User | user@test.com | password

# Libraries and Tools Used
- Spring Boot
  - JPA
  - Web
  - Dev Tools
  - Test
  - Validation
  - Security
  - Security Test
- Project Lombok
- GSON
- JWT Token
- ITextPDF
- Java Mail
# Email configuration
By default the API is setup with the email address `sncmsuktestemail@gmail.com` for sending emails in this prototype. This can be changes to your own email in the InvoiceServiceImpl file in the emailInvoice method by changing the PasswordAuthentication username and password fields to your own email and the password to that email. 
# Framework Diagrams - C4 Model
## Context
![](api/structure_diagrams/structurizr-72910-context.png)
## Container
![](api/structure_diagrams/structurizr-72910-containers.png)
## Components
![](api/structure_diagrams/structurizr-72910-components.png)

# Features
- [Aircraft Controller (/aircraft/)](#aircraft-controller)
    - [Adding a platform through the API - POST /add](#adding-a-platform-through-the-api)
    - [GET - /user/{id}]()
    - [POST - /log-flight]()
    - [GET - /total-repairs/{tail-number}]()
    - [GET - /needing-repair]()
    - [GET - /time-operational]()
    - [POST - /time-operational]()
    - [GET - /platform-status]()
    - [POST - /platform-status/filter]()
    - [GET - /android/platform-status]()
    - [GET - /ceo-aircraft-cost-full]()
    - [GET - /ceo-aircraft-cost]()
    - [Assigning a user to an aircraft - POST /assign-user](#assigning-an-user-to-an-aircraft)
    - [POST - /aircraft-parts-status]()
    - [POST - /update-aircraft-status]()
    - [POST - /update-aircraft-part]()
    - [GET - /all]()
    - [POST - /all/filter]()
- [Auth Controller (/api/auth/)](#auth-controller)
  - [Logging in - POST /signin](#logging-in---post-signin)
  - [Signing Users Up To The System - POST /signup](#signing-users-up-to-the-system---post-signup)
  - [POST - /getJwtInfo]()
  - [GET - /getUserInfo]()
- [Parts Controller (/parts/)](#parts-controller)
  - [Adding a part through the API - POST /add](#adding-a-part-through-the-api)
  - [GET - /low-stock]()
  - [POST - /stockrequest]()
  - [GET - /stock]()
  - [GET - /location/stock]()
  - [GET - /failuretime]()
  - [GET - /most-failing/{topN}]()
  - [POST - /get-by-type]()
  - [GET - /all]()

# Aircraft Controller
Mappings and features to do with the addition, modification, deletion and fetching information about aircraft.
##Adding a platform through the API - POST Request
### Mapping Information: 
localhost:8080/aircraft/add (DEV)<br>
uastest.herokuapp.com/aircraft/add (UAT)<br>
uasprod.herokuapp.com/aircraft/add (PROD)<br>

### What it does:
This mapping allows the logged in user <update me to specify which users> to add a new platform to the database.
### Responses:
#### Successful:
A 200 OK Response should return JSON in the following format upon a successful request:<br>
`{"response": "Success"}`
#### Error Responses and Meaning:
A 400 Bad Request Response will be returned with the JSON in the following formats:

`{"response": "Invalid aircraft with specified tail number already present."}`

`{"response": "Invalid location not found."}`

`{"response": "Invalid platform status."}`

`{"response": "Invalid platform type."}`
#### Request Body
The Request Body needed for this mapping is as follows:
`{"tailNumber":"G-000",`<br>
`"location":"London",`<br>
`"platformStatus":"DESIGN",`<br>
`"platformType":"Platform_A"}` <br><br>
The Java POJO to match this can be seen [here](src/main/java/com/uas/api/models/dtos/AircraftAddNewDTO.java)<br>
JSON names are case sensitive and must be written as shown:
- `tailNumber` - must be unique and provided.
- `location` - must be a locationName present in the location table or an error will be returned.
- `platformStatus` - must be written as one of these options: "DESIGN" "PRODUCTION" "OPERATION" "REPAIR"; or an error will be returned.
- `platformType` - can only take 2 different inputs, either "Platform_A" or "Plaform_B" or an error will be returned.<br>
## GET - /user/{id}
### Mapping Information:
localhost:8080/aircraft/user/{id}<br>
uastest.herokuapp.com/aircraft/user/{id}<br>
uasprod.herokuapp.com/aircraft/user/{id}
### What it does:
This mapping retrieves all of the aircraft assigned to that particular user.
### Responses:
#### Successful:
A 200 response should return on a successful request.
#### Error Responses and Meaning:
If the user is not found, then a 400 error for bad request will occur with the body:<br>
`{"message":"User not found!","status":"BAD_REQUEST"}`

If the user is found, but does not have any aircraft assigned to them, a 200 OK response will return with an empty list.
## POST - /log-flight
### Mapping Information:
localhost:8080/aircraft/log-flight (DEV)<br>
uastest.herokuapp.com/aircraft/add (UAT)<br>
uasprod.herokuapp.com/aircraft/add (PROD)<br>
### What it does:
This method is logs flight hours for an aircraft flight and updates the flight time of the aircraft and the parts associated with that aircraft and the pilot. <br>
### Responses:
#### Successful:
A 200 response should return on a successful request.<br>
#### Error Responses and Meaning:
If an invalid user is selected to be piloting the aircraft, then a 400 error for bad request will occur with the body: <br>
`{
    "message": "Aircraft user does not exist!",
    "status": "BAD_REQUEST"
}`<br>
If the aircraft is not found, then a 400 error for a bad request will occur with the body: <br>
`{
    "message": "Aircraft not found!",
    "status": "BAD_REQUEST"
}`<br>
### Request Body:
Takes a json request body: `{"userId":1,"aircraft":"G-001","flyTime":10}`<br>
-The `userId` is the ID of the user who was piloting the aircraft.<br>
-The `aircraft` is the tail number of the aircraft that was being pilotted.<br>
-The `flyTime` is the amount of time it was pilotted. <br>
## GET - /total-repairs/{tail-number}
### Mapping Information:
localhost:8080/aircraft/total-repairs/{tail-number}<br>
uastest.herokuapp.com/aircraft/total-repairs/{tail-number}<br>
uasprod.herokuapp.com/aircraft/total-repairs/{tail-number}<br>
### What it does:
This method gets the total number of repairs carried out on a particular aircraft.
### Responses:
#### Successful:
A successful response should return a 200 OK.
#### Error Responses and Meaning:
Error responses from this method include:
- 400 Bad Request: this will be caused by the aircraft not being found.
- 401 Unauthorised: the current logged in user does not have access to this resource.
- 403 Forbidden: potentially caused by cross origins if called from the web. Check the value it is assigned to.
## GET - /needing-repair
### Mapping Information:
localhost:8080/aircraft/needing-repair<br>
uastest.herokuapp.com/aircraft/needing-repair<br>
uasprod.herokuapp.com/aircraft/needing-repair<br>
### What it does:
This method gets the aircraft that need to be repaired.
### Responses:
#### Successful:
A successful response will return a 200 OK.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## GET - /time-operational
### Mapping Information:
localhost:8080/aircraft/time-operational<br>
uastest.herokuapp.com/aircraft/time-operational<br>
uasprod.herokuapp.com/aircraft/time-operational<br>
### What it does:
This method gets all the aircraft and the total number of hours it has been operational.
### Responses:
#### Successful:
A 200 OK response will be returned upon success.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## POST - /time-operational
### Mapping Information:
localhost:8080/aircraft/time-operational<br>
uastest.herokuapp.com/aircraft/time-operational<br>
uasprod.herokuapp.com/aircraft/time-operational<br>
### What it does:
This method updates the number of hours an aircraft has been operational for.
### Responses:
#### Successful:
A successful response will return a 200 OK.
#### Error Responses and Meaning:
An error response will return a 400 Bad Request based on the following:
- Aircraft Not Found
- Invalid DTO Attribute - this means that a value of your request body is invalid, i.e. a negative hours operational integer. 
  
An error response with the following codes will be returned on other errors:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.

### Request Body:
The request body looks as follows:

`{
"tailNumber":"G-005",
"hoursToAdd":110
}`

JSON names are case sensitive.
## GET - /platform-status
### Mapping Information:
localhost:8080/aircraft/platform-status<br>
uastest.herokuapp.com/aircraft/platform-status<br>
uasprod.herokuapp.com/aircraft/platform-status<br>
### What it does:
This method fetches a list of the aircraft and group them by their platform status.
### Responses:
#### Successful:
A 200 OK response should be returned upon success.
#### Error Responses and Meaning:
Possible error responses from this mapping include:
- 403 Forbidden: If this has come from the web, this might be to do with the cross origins, so check.
- 401 Unauthorised: This means that the particular user account signed in does not have access to this mapping. Update the mapping with the required user role if needed.
## POST - /platform-status/filter
### Mapping Information:
localhost:8080/aircraft/platform-status/filter<br>
uastest.herokuapp.com/aircraft/platform-status/filter<br>
uasprod.herokuapp.com/aircraft/platform-status/filter<br>
### What it does:
This method returns a list of aircraft filtered by their platform status.
### Responses:
#### Successful:
A 200 OK response should be returned with a list of aircraft and their data.
#### Error Responses and Meaning:
A 400 Bad Request should be returned if:
- Platform Status does not exist.
### Request Body:

## GET - /android/platform-status
### Mapping Information:
localhost:8080/aircraft/android/platform-status<br>
uastest.herokuapp.com/aircraft/android/platform-status<br>
uasprod.herokuapp.com/aircraft/android/platform-status<br>
### What it does:
### Responses:
#### Successful:
#### Error Responses and Meaning:
## GET - /ceo-aircraft-cost-full
### Mapping Information:
localhost:8080/aircraft/ceo-aircraft-cost-full (DEV) <br>
uastest.herokuapp.com/aircraft/ceo-aircraft-cost-full (UAT) <br>
uasprod.herokuapp.com/aircraft/ceo-aircraft-cost-full (PROD) <br>
### What it does:
This returns a json body containing the total amount spent overall on parts and repairs along with individual aircraft cost for its parts and the repairs performed on these parts.
### Responses:
#### Successful:
Returns a status 200 with a json body containing the cost data. 
#### Error Responses and Meaning:
A GET request so error responses are unlikely but could be thrown if there is a problem communicating with the database. 
### Request Body:
No request body needed. 
## GET - /ceo-aircraft-cost
### Mapping Information:
localhost:8080/aircraft/ceo-aircraft-cost (DEV) <br>
uastest.herokuapp.com/aircraft/ceo-aircraft-cost (UAT) <br>
uasprod.herokuapp.com/aircraft/ceo-aircraft-cost (PROD) <br>
### What it does:
This returns a json body containing a list of aircraft with the total part, repair and overall costs.
### Responses:
#### Successful:
Returns a status 200 with a json body containing the cost data. 
#### Error Responses and Meaning:
A GET request so error responses are unlikely but could be thrown if there is a problem communicating with the database. 
### Request Body:
No request body needed. 
## GET - /ceo-aircraft-cost/{id}
### Mapping Information:
localhost:8080/aircraft/ceo-aircraft-cost/{id} (DEV) <br>
uastest.herokuapp.com/aircraft/ceo-aircraft-cost/{id} (UAT) <br>
uasprod.herokuapp.com/aircraft/ceo-aircraft-cost/{id} (PROD) <br>
### What it does
Gets the repair and part cost associated with a specific aircraft. 
### Responses:
#### Successful:
Returns a status 200 with a json body containing the costs associated with the given aircraft.
#### Error Responses and Meaning:
A GET request so error responses are unlikely but could be thrown if there is a problem communicating with the database. 
### Request Body:
Takes an aircraft tailnumber as a path variable. 
## Assigning an User to an Aircraft - POST /assign-user
### Mapping Information:
localhost:8080/aircraft/assign-user<br>
uastest.herokuapp.com/aircraft/assign-user<br>
uasprod.herokuapp.com/aircraft/assign-user<br>
### What it does:
This method assigns the selected user to the selected aircraft.
### Responses:
#### Successful:
On success, a 200 OK response should be returned.
#### Error Responses and Meaning:
On failure a 400 Bad_Request should be returned, with either the error of:
- Aircraft Not Found
- User not Found
### Request Body:
A pre-existing user in the database can be assigned to a pre-existing aircraft in the database to form an AircraftUser entity

The POST request should be sent to /aircraft/assign-user.

Example of JSON body:

`{"userID":"2", "tailNumber":"G-004"}`

- Both the userID and tailNumber must reference pre-existing entities in the database.
## GET - /aircraft-parts-status/{id}
### Mapping Information:
localhost:8080/aircraft/aircraft-parts-status/{id} (DEV) <br>
uastest.herokuapp.com/aircraft/aircraft-parts-status/{id} (UAT) <br>
uasprod.herokuapp.com/aircraft/aircraft-parts-status/{id} (PROD) <br>
### What it does:
A GET method that returns the parts and their status for a specific given aircraft.
### Responses:
#### Successful:
200 Response for a successful request with a json body containing the aircraft status and a list of parts with their part number, part type, and part status.
#### Error Responses and Meaning:
Not found exception can be returned if the given aircraft isn't present in the database.<br>
### Request Body:
Path variable {id} for the aircraft tail number of the aircraft the parts are being searched for.
## POST - /update-aircraft-status
### Mapping Information:
localhost:8080/aircraft/update-aircraft-status (DEV) <br>
uastest.herokuapp.com/aircraft/update-aircraft-status (UAT) <br>
uasprod.herokuapp.com/aircraft/update-aircraft-status (PROD) <br>
### What it does:
It is used to update the status of a given aircraft in the database.
### Responses:
#### Successful:
If it is successful it will return a 200 response. 
#### Error Responses and Meaning:
If the aircraft status is invalid it will return a 400 response with the body: <br>
`{
    "message": "Invalid aircraft status!",
    "status": "BAD_REQUEST"
}` <br>
If the aircraft cannot be found it will return a 400 response with the body: <br>
`{
    "message": "Aircraft not found!",
    "status": "BAD_REQUEST"
}`
### Request Body:
Takes a request body with the tail number and status. The status can be any one of these 4 and is case sensitive:<br>
`OPERATION` `PRODUCTION` `DESIGN` `REPAIR`<br>
`{"tailNumber":"G-001","status":"OPERATION"}`
## POST - /update-aircraft-part
### Mapping Information:
localhost:8080/aircraft/update-aircraft-part (DEV) <br>
uastest.herokuapp.com/aircraft/update-aircraft-part (UAT) <br>
uasprod.herokuapp.com/aircraft/update-aircraft-part (PROD) <br>
### What it does:
A post mapping that updates the part of a specified aircraft to a new specified part. If the aircraft already has a part of the same time assigned to it then it will unassign that part before assigning the given new part.
### Responses:
#### Successful:
If successful it will respond with a status 200.
#### Error Responses and Meaning:
if the part is already assigned to an aircraft then it will respond with a 400 status error with the error body of: <br>
`Part already assigned to aircraft` <br>
### Request Body:
Takes a request body with the aircraft tailnumber and the new partID. <br>
`{"tailNumber":"G-002","newPartNumber":16}`
## GET - /all
### Mapping Information:
localhost:8080/aircraft/all<br>
uastest.herokuapp.com/aircraft/all<br>
uasprod.herokuapp.com/aircraft/all<br>
### What it does:
This fetches all the aircraft from the application.
### Responses:
#### Successful:
A 200 OK response should be returned upon success, with a list of the aircraft.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
### Request Body:
## POST - /all/filter
### Mapping Information:
localhost:8080/aircraft/all/filter<br>
uastest.herokuapp.com/aircraft/all/filter<br>
uasprod.herokuapp.com/aircraft/all/filter<br>
### What it does:
### Responses:
#### Successful:
#### Error Responses and Meaning:
### Request Body:
# Auth Controller:
Mappings and features to do with the addition and authentication of users in the system.
## Logging in - POST /signin
### Mapping Information:
localhost:8080/api/auth/signin (DEV)
uastest.herokuapp.com/api/auth/signin (UAT)
uasprod.herokuapp.com/api/auth/signin (PROD)
### What it does:
This mapping allows registered users of the system to log in, and it returns a JWT token for use in the system.
### Responses:
#### Successful:
A 200 OK Response for a user of type "Logistic Officer" will return JSON in the following format:<br>
`{"token": "<jwt token here>",`<br>
`"id": "<user id here>",`<br>
`"username": "logistic@test.com",`<br>
`"email": "logistic@test.com",`<br>
`"roles": ["ROLE_USER_LOGISTIC"]}`
#### Error Responses and Meaning:
Planned 400 Bad Request responses will have JSON returned in the following format:<br>
`{"message":"<>", "status":"BAD_REQUEST"}`

The message will change based on the following scenarios:
- Incorrect credentials or blank/null email and/or password<br>
  `{
  "message": "Invalid email or password!",
  "status": "BAD_REQUEST"
  }`<br>

A 401 Not Authorised or 403 Forbidden appearing with a user who is authenticated and has the right privileges to access the system, have likely been caused by an incorrect url request to the system.
### Request Body:
The request body needed for this mapping is as follows:<br>
`{"email":"<email>",
"password":"<password>"}`

The Java POJO to match this can be seen [here](api/src/main/java/com/uas/api/requests/LoginRequest.java)<br>
JSON names are case-sensitive.
## Signing Users Up To The System - POST /signup
### Mapping Information:
localhost:8080/api/auth/signup (DEV)
uastest.herokuapp.com/api/auth/signup (UAT)
uasprod.herokuapp.com/api/auth/signup (PROD)
### What it does:
This mapping allows **Admin** users to register new users to the system. No other user can access this endpoint.<br>
#### Usage:
Android: **No**<br>
Web: **Yes** - But is not a visible option on the service, in order to restrict access to it.

### Responses
#### Successful:
A 200 OK response will return JSON in the following format:<br>
`{"message":"User registered successfully!"}`
#### Error Responses and Meaning:
Planned 400 Bad Request responses will have the JSON returned in the following format:<br>
`{"message":"<>", "status":"BAD_REQUEST"}`

The message will change based on the following scenarios:
- Email Already Exists<br>
  `{"message": "Email already exist! Please use another email.",`<br>
  `"status": "BAD_REQUEST"}`<br>
- Emails Do Not Match<br>
  `{
  "message": "Please retype your email!",
  "status": "BAD_REQUEST"
  }`<br>
- Passwords Do Not Match<br>
  `{
  "message": "Please retype your password!",
  "status": "BAD_REQUEST"
  }`<br>
### Request Body:
The request body needed for this mapping is as follows:<br>
`{"email":"gen@snc.co.uk",`<br>
`"confirmEmail":"gen@snc.co.uk",`<br>
`"password":"password",`<br>
`"confirmPassword":"password",`<br>
`"firstName" : "Jen",`<br>
`"lastName":"User"
}`
The Java POJO to match this can be seen [here]()<br>
JSON names are case-sensitive.
# Parts Controller 
### Adding a part through the API

A Part can be added using the by sending a post request to /parts/add. The JSON needs to be formatted as shown below. 

` {"partType":"1","aircraft":"G-001","location":"London","manufacture":"2022-02-20 11:00:00","partStatus":"OPERATIONAL"}`


- `partType` must be a valid part type id from the part type table or an error will be returned in the response.
- `aircraft` can be blank and the part will be inserted without an aircraft table reference, If the aircraft field is populated but there isn't an aircraft with that tailnumber in the db, then it will be defaulted to an empty field. 
- `location` must match a location name in the db or an error will be returned in the response and no part will be added to the db.
- `manufacture` can be left blank and the database will set the manufacture date and time to the time the part was added. If the manufacture time is inputted with the request then it must be written in the form "YYYY-MM-DD HH:MM:SS".
- `partStatus` must written like so to be accepted in the database. Either "OPERATIONAL", "AWAITING_REPAIR", "BEING_REPAIRED", "BEYOND_REPAIR". 

An example of a json request to add a part without the tail number or aircraft would look like this. The fields must still be included but just left blank. 


` {"partType":"1","aircraft":"","location":"London","manufacture":"","partStatus":"OPERATIONAL"}`

If the request is successful and the part is added to the json then a json response will be returned with:`{"response": "Success"}`
If the request is unsuccessful the response will show an error for bad request and return a response with an appropriate error message, these include:

`{
    "response": "Invalid part type."
}`

`{
    "response": "Invalid location."
}`

`{
    "response": "Invalid datetime."
}` This will show when the manufacture date is filled out but not in the correct format. 

`{
    "response": "Invalid part status."
}`
## GET - /low-stock
### Mapping Information:
localhost:8080/parts/low-stock<br>
uastest.herokuapp.com/parts/low-stock<br>
uasprod.herokuapp.com/parts/low-stock<br>
### What it does:
This method gets the low stock across all locations.
### Responses:
#### Successful:
A 200 OK response should be returned with a list of low stock (less than 40%) across the different locations.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## POST - /stockrequest
### Mapping Information:
localhost:8080/parts/stockrequest (DEV)<br>
uastest.herokuapp.com/parts/stockrequest (UAT)<br>
uasprod.herokuapp.com/parts/stockrequest (PROD)<br>
### What it does:
Post request for ordering more stock. It adds the stock order to the database and generates a pdf invoice which is sent to the given part supplier email address.
### Responses:
#### Successful:
Returns a 200 status response with no body. 
#### Error Responses and Meaning:
It could return errors for a location that isn't present in the database, or if the partID and quantities arrays are not the same size.
### Request Body:
`{"location":"Cardiff","supplierEmail":"replace@withsupplier.email","partIDs":[1,2],"quantities":[2,2]}`
## GET - /stock
### Mapping Information:
localhost:8080/parts/stock<br>
uastest.herokuapp.com/parts/stock<br>
uasprod.herokuapp.com/parts/stock<br>
### What it does:
This gets the stock levels for all locations.
### Responses:
#### Successful:
A 200 OK response should be returned on success with a list of stock.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## GET - /location/stock
### Mapping Information:
localhost:8080/parts/location/stock<br>
uastest.herokuapp.com/parts/location/stock<br>
uasprod.herokuapp.com/parts/location/stock<br>
### What it does:
This method fetches stock from the different locations
### Responses:
#### Successful:
A 200 OK response should be returned sorting stock by locations.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## GET - /failuretime
### Mapping Information:
localhost:8080/parts/failure-time<br>
uastest.herokuapp.com/parts/failure-time<br>
uasprod.herokuapp.com/parts/failure-time<br>
### What it does:
This method gets the failure time for all of the parts.
### Responses:
#### Successful:
A 200 OK response should return with a list of the parts and their failure times.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## GET - /most-failing/{topN}
### Mapping Information:
localhost:8080/parts/most-failing/{topN}<br>
uastest.herokuapp.com/parts/most-failing/{topN}<br>
uasprod.herokuapp.com/parts/most-failing/{topN}<br>
### What it does:
This method gets the top failing parts and is adjustable by the topN path variable.
### Responses:
#### Successful:
A 200 OK response should be returned on success, with a list of the parts, their failure time and cost.
#### Error Responses and Meaning:
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
### Request Body:
## GET - /get-by-type/{id}
### Mapping Information:
localhost:8080/parts/get-by-type/{id} (DEV)<br>
uastest.herokuapp.com/parts/get-by-type/{id} (UAT)<br>
uasprod.herokuapp.com/parts/get-by-type/{id} (PROD)
### What it does:
Gets all parts for a specific part type that are not assigned to an aircraft. It is used to display available parts on the front end for a user to choose to assign to an aircraft.
### Responses:
#### Successful:
A 200 response with an array of partIDs for parts that are available of that type.
#### Error Responses and Meaning:
Could return an error if the API cannot communicate with the database.
An error response with the following codes will be returned on error:
- 403 Forbidden: If the request has been made from the web this is potentially cross origins, please check which one it is pointing to.
- 401 Unauthorised: The current logged in user does not have access to this resource.
## GET - /parts/all
### Mapping Information:
localhost:8080/parts/all (DEV)<br>
uastest.herokuapp.com/parts/all (UAT)<br>
uasprod.herokuapp.com/parts/all (PROD)<br>

### What it does:
This mapping allows the logged in user to retrieve all parts from the database. Plus their stock locations and compatible platforms.
### Responses:
#### Successful:
A 200 OK Response should return JSON in the following format upon a successful request:<br>
`[
{
"partNumber": 1,`<br>
`"partType": "Wing A",`<br>
`"cost": 200.00,`<br>
`"weight": 50000,`<br>
`"typicalFailureHours": 600,`<br>
`"stockLocations":[
{
"partNumber": 1,
"location": "Cardiff",
"stockCount": 10
},
{
"partNumber": 1,
"location": "London",
"stockCount": 10
}
],`<br>
`"compatiblePlatforms":[
"Platform A",
"Platform B"
]
}]`
#### Error Responses and Meaning:
A 400 Bad Request Response will be returned with the JSON in the following formats:

`{"response": "Parts not found!"}`

#### Request Body
No Request Body is needed for this mapping.


## GET - /parts/stock-order/all
### Mapping Information:
localhost:8080/parts/stock-order/all (DEV)<br>
uastest.herokuapp.com/parts/stock-order/all (UAT)<br>
uasprod.herokuapp.com/parts/stock-order/all (PROD)<br>

### What it does:
This mapping allows the logged in user to retrieve all previous part stock orders.
### Responses:
#### Successful:
A 200 OK Response should return JSON in the following format upon a successful request:<br>
`[{"locationName": "Cardiff",`<br>
`"supplierEmail": "supplierOne@test.com",`<br>
`"totalCost": 2000.0,`<br>
`"orderDateTime": "2022-01-29T11:17:43.000+00:00",`<br>
`"partName": "Wing A",`<br>
`"quantity": 20
}]`

#### Request Body
No Request Body is needed for this mapping.

# Testing
## Unit
Unit testing has been carried out on all services and controllers, with both acceptance and failure testing taking place. The code has a code coverage of over 70%.
## Performance
Performance testing has been carried out, and response times have been checked. All our methods with the exception of sending a stock request respond in under 200ms.
## Automated Endpoint Testing
All of the endpoints listed in the documentation have been tested using Postman's automated testing feature. You can run this on all three environments (dev, uat, prod) by using the link below and changing the url variable.<br>
All of the tests check for a 200/201/202 response, and that it is responding in under 200ms. The only exception to the latter rule is Stock Request as this talks to an external service and so takes longer.<br>
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/63dbd0128b1a396d2994?action=collection%2Fimport)<br>
# Deployment
The API has been deployed using Gitlab CI. When deploying to UAT or PROD, the pipeline goes through the following stages:
- Build
- Publish
- Connect-to-uat (or prod)
- Deploy-to-uat (or prod)

The build stage will build the project using Java 11 and gradle. It will then check for any checkstyle errors (and will fail if any are found), it will run all the tests (and will fail if any tests fail), then finally it will check the code coverage (and will fail if less than 60%) and generate results files for all of these stages.

The publish stage will then publish these tests results as artifacts, after the build stage has passed. You can download these under the publish job.

The connect to uat works only on the uat branch. Prod has it's own connect-to-prod which works on prod only. This connects to the external database and takes a dump of the current data and publishes it as an sql file in the artifact. This is so that if there are any errors upon deploying, the database can be rolled back.

The deploy-to-uat works only on the uat branch. Prod has its own deploy to prod which works on prod only. This uses Ruby to deploy the application to heroku.

