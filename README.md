# UAS Lifecycle Management
This is the git repository for the API and the web front end. For more information on the API and it's features please go to:<br>
<b>uas-lifecycle-management > api > ApiREADME.md</b><br>

<h2>Documentation</h2>

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
