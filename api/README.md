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
Production | application-prod.properties | prod | <update me> | uasprod.herokuapp.com <br>
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
CEO | ceo@test.com | dev
COO | coo@test.com | uat | 
CTO | cto@test.com | prod | 
LO | logistic@test.com | HEHE |
General User | HEHE | HEHE

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
# Framework Diagrams - C4 Model
## Context
![](api\structure_diagrams\structurizr-72910-context.png)
## Container
![](api\structure_diagrams\structurizr-72910-containers.png)
## Components
![](api\structure_diagrams\structurizr-72910-components.png)

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
  - [POST - /signin]()
  - [POST - /signup]()
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
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /log-flight
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /total-repairs/{tail-number}
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /needing-repair
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /time-operational
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /time-operational
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /platform-status
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /platform-status/filter
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /android/platform-status
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /ceo-aircraft-cost-full
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /ceo-aircraft-cost
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## Assigning an User to an Aircraft - POST /assign-user
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
A pre-existing user in the database can be assigned to a pre-existing aircraft in the database to form an AircraftUser entity

The POST request should be sent to /aircraft/assign-user.

Example of JSON body:

`{"userID":"2", "tailNumber":"G-004"}`

-Both the userID and tailNumber must reference pre-existing entities in the database.
## POST - /aircraft-parts-status
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /update-aircraft-status
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /update-aircraft-part
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
# Auth Controller:
## GET - /all
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /all/filter
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
##POST - /signin
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
##POST - /signup
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
##POST - /getJwtInfo
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
##GET - /getUserInfo
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
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
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /stockrequest
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /stock
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /location/stock
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /failuretime
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## GET - /most-failing/{topN}
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
## POST - /get-by-type
    ### Mapping Information:
    ### What it does:
    ### Responses:
    #### Successful:
    #### Error Responses and Meaning:
    ### Request Body:
# Testing
## Unit
## Performance
# Deployment



