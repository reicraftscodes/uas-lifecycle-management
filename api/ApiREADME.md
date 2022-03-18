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
Before running under any of the Spring profiles, please make sure that you have at least Java 11 installed and the environment variables configured on your machine so that you are able to run "java -jar".<br>
[Here are instructions for installing Java and setting up]()<br>
Please also make sure that you have MySQL Workbench and/or MySQL client installed on your machine. You will need this for database connections.<br>
[Installation instructions for MySQL]()
### Running Locally
The API is set up to handle both the MariaDB and MySQL database providers. <br>To create the database locally, please do the following:
1. Navigate to where the source zip file is located and extract it.
2. Locate the ["database.sql"](src/main/resources/database.sql) file - api > src > main > resources > database.sql
3. Open MySQL Workbench and open the sql file
4. Run the sql file - lightning bolt icon, third from the left.

Provided there are no errors on running the script, you can now run the API locally:
1. Navigate to <update this to include jar file>
2. Run the command "java -jar api-0.0.1-SNAPSHOT.jar"
3. Provided there are no errors, the API is now up and running and can be used locally.
### Running in UAT
1. Open project in intellij (or other IDE)
2. Change Spring boot profile property to uat
3. Run gradle jar
4. Run jar from command line

This will run the api locally but with UAT db.
To test against UAT live API, change the property for the retrofit client in android to: uastest.herokuapp.com and in web: <update me>

### Running in PROD
1. Open project in intellij (or other IDE)
2. Change Spring boot profile property to prod
3. Run gradle jar
4. Run jar from command line

This will run the api locally but with PROD db.
To test against PROD live API, change the property for the retrofit client in android to: uasprod.herokuapp.com and in web: <update me>
# Libraries and Tools Used
# Framework Diagrams
# Requirements
# Features

### Adding a platform through the API
To add a platform navigate to /aircraft/api and send a post request with the JSON for an aircraft in the body, e.g. 

`{"tailNumber":"G-000","location":"London","platformStatus":"DESIGN","platformType":"Platform_A"} `

(json names are case sensitive and must be written as shown).

- `tailNumber` - must be unique and provided. 
- `location` - must be a locationName present in the location table or an error will be returned. 
- `platformStatus` - must be written as one of these options: "DESIGN" "PRODUCTION" "OPERATION" "REPAIR"; or an error will be returned. 
- `platformType` - can only take 2 different inputs, either "Platform_A" or "Plaform_B" or an error will be returned.

If a request is successful the json returned will be `{"response": "Success"}`
If the request is unsuccessful then an bad request response will be returned with the json containing an error message. Examples of these include:

`{
    "response": "Invalid aircraft with specified tail number already present."
}`

`{
    "response": "Invalid location not found."
}`

`{
    "response": "Invalid platform status."
}`

`{
    "response": "Invalid platform type."
}`

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

### Assigning an User to an Aircraft

A pre-existing user in the database can be assigned to a pre-existing aircraft in the database to form an AircraftUser entity

The POST request should be sent to /aircraft/assign-user. 

Example of JSON body:

`{"userID":"2", "tailNumber":"G-004"}`

-Both the userID and tailNumber must reference pre-existing entities in the database.

# Testing
## Unit
## Performance
# Deployment



