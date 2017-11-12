# Resource Loading

## Methods
 
### HTTP Client

There are 2 different methods of uploading resources using an HTTP client.
1. PUT or POST a single resource
   PUT method is used to create a new resource with a specified ID or update an existing resource.
   
     PUT [base]/baseDstu3/Practitioner/prac-123
     ```
     {
       "resourceType": "Practitioner",
       "id": "prac-123",
       "identifier": [
         {
           "system": "http://clinfhir.com/fhir/NamingSystem/practitioner",
           "value": "z1z1kXlcn3bhaZRsg7izSA1PYZm1"
         }
       ],
       "telecom": [
         {
           "system": "email",
           "value": "sruthi.v@collabnotes.com"
         }
       ]
     }
     ```
     Successful response
     ```
     {
       "resourceType": "OperationOutcome",
       "issue": [
         {
           "severity": "information", 
           "code": "informational",
           "diagnostics": "Successfully created resource \"Practitioner/prac-123/_history/1\" in 32ms"
         }
       ]
     }
     ```
     If the request results in an error, the "severity" will be specified as "error" and a message will be given in the "diagnostics" value field

   POST method is used to create a new resource with a generated ID.
     
     POST [base]/baseDstu3/Practitioner
     ```
     {
       "resourceType": "Practitioner",
       "identifier": [
         {
           "system": "http://clinfhir.com/fhir/NamingSystem/practitioner",
           "value": "z1z1kXlcn3bhaZRsg7izSA1PYZm1"
         }
       ],
       "telecom": [
         {
           "system": "email",
           "value": "sruthi.v@collabnotes.com"
         }
       ]
     }
     ```
     The response will be the same as the PUT method.

2. POST a transaction Bundle
    
    The [transaction](http://hl7.org/implement/standards/fhir/http.html#transaction) operation loads all the resources within a transaction Bundle.
    
    POST [base]/baseDstu3
    ```
    {
      "resourceType": "Bundle",
      "id": "example-transaction",
      "type": "transaction",
      "entry": [
        {
          "resource": {
            ...
          },
          "request": {
            "method": "PUT",
            "url": "[base]/baseDstu3/Resource/ResourceID"
          }
        },
        ...
      ]
    }
    ```
    The response will be a Bundle containing the status and location for each uploaded resource if successful or an OperationOutcome if there were errors.
    ```
    {
      "resourceType": "Bundle",
      "id": "...",
      "type": "transaction-response",
      "entry": [
        {
          "response": {
            "status": "201 Created",
            "location": "Resource/ResourceID/_history/1",
            ...
          },
          ...
        }
      ]
    }
    ```