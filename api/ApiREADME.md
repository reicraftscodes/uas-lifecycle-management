# UAS Lifecycle Management: API Documentation
## Set Up
### SQL script
Please locate and download the sql script (when Oliver has completed his issues) and run this in the MySQL workspace. <b> PLEASE NOTE: If you are using a personal laptop and have MySQL downloaded, there might be mild syntax differences and you may have to adjust the script - if you need to do this, please create a separate script and DO NOT push the changes to git. The API is set up to handle both the MariaDB and MySQL database providers so should work with whatever provider you are using</b><br>
### Profiles
There are currently three properties files in the application. application.properties, application-dev.properties and application-prod.properties. The application.properties file applies the properties needed irregardless of what profile you are using - e.g. jwt token timeout.<br>
#### Profiles in Project
Profile | Associated Properties File | Abbreviation |
--- | --- | --- |
Development | application-dev.properties | dev
Production | application-prod.properties | prod<br> 
When working locally, please use the <b><i>dev profile</i></b> with the mysql database backend - this should have been automatically set in the application.properties file, but if it hasn't, add the following to the file:<br>
spring.profiles.active=dev<br>
The production profile will be used if/when we push the API to a live site such as heroku.<br>

<h2>API Usage</h2>

<h3>Adding a platform through the API</h3>
To add a platform navigate to /aircraft/api and send a post request with the JSON for an aircraft in the body, e.g. 

`{"tailNumber":"G-000","location":"London","platformStatus":"DESIGN","platformType":"Platform_A"} `

(json names are case sensitive and must be written as shown).

- The tailNumber must be unique and provided. 
- The location must be a locationName present in the location table or an error will be returned. 
- platformStatus must be written as one of these options or an error will be returned. "DESIGN" "PRODUCTION" "OPERATION" "REPAIR"
- platformType can only take 2 different inputs, either "Platform_A" or "Plaform_B" or an error will be returned.

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

<h3>Adding a part through the API</h3>

A Part can be added using the by sending a post request to /parts/add. The JSON needs to be formatted as shown below. 

` {"partType":"1","aircraft":"G-001","location":"London","manufacture":"2022-02-20 11:00:00","partStatus":"OPERATIONAL"}`


- partType must be a valid part type id from the part type table or an error will be returned in the response.
- aircraft can be blank and the part will be inserted without an aircraft table reference, If the aircraft field is populated but there isn't an aircraft with that tailnumber in the db, then it will be defaulted to an empty field. 
- location must match a location name in the db or an error will be returned in the response and no part will be added to the db.
- manufacture can be left blank and the database will set the manufacture date and time to the time the part was added. If the manufacture time is inputted with the request then it must be written in the form "YYYY-MM-DD HH:MM:SS".
- partStatus must written like so to be accepted in the database. Either "OPERATIONAL", "AWAITING_REPAIR", "BEING_REPAIRED", "BEYOND_REPAIR". 

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



