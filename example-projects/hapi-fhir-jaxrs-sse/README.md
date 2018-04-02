This example sets up a FHIR server that can ship out Server-sent events using standard Jersey 2.x components. Start up the server and eg. issue the following curl request 'curl -v -X GET http://localhost:8080/Patient/listen'. The will block curl and once any events are shipped to the server, they will automatically be sent to the curl client.

Changes can be sent to the server on localhost:8080/Patient which accepts any kind of patients that has at least one identifier.

Voila
