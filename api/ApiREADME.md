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


