# UAS Lifecycle Management
This is the git repository for the API and the web front end. For more information on the API and it's features please go to:<br>
<b>uas-lifecycle-management > api > ApiREADME.md</b><br>

<h2>Documentation</h2>

<h3>Adding a platform through the API</h3>
To add a platform navigate to /aircraft/api and send a post request with the JSON for an aircraft in the body, e.g. {"tailNumber":"G-000","location":"London","platformStatus":"Design","platformType":"Platform_A"} (json names are case sensitive and must be written as shown).
