```mermaid
flowchart TB
{{snippet:file:hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/_snippets/mermaid_palette.mmd}}
    App(["Your application"]):::app
    MethodCalled["Client Method<br/>Called"]:::step
    Encode["Resource<br/>encoded as JSON"]:::step
    RequestCreated["HTTP Request<br/>Created"]:::step
    RequestSent["HTTP Request<br/>Sent"]:::step
    ResponseReceived["HTTP Response<br/>Received"]:::step
    Parsed["Response<br/>Parsed"]:::step

    RequestHook["Java Hook:<br/>CLIENT_REQUEST"]:::javahook
    ResponseHook["Java Hook:<br/>CLIENT_RESPONSE"]:::javahook

    App e1@-- "client.update()<br/>&nbsp;&nbsp;.resource(myPatient)<br/>&nbsp;&nbsp;.execute();" --> MethodCalled
    MethodCalled e2@--> Encode
    Encode e3@--> RequestCreated
    RequestCreated -.-> RequestHook
    RequestHook e4@--> RequestSent
    RequestSent e5@--> ResponseReceived
    ResponseReceived -.-> ResponseHook
    ResponseHook e6@--> Parsed
    Parsed e7@--> MethodCalled
    MethodCalled e8@-- "return" --> App

    click RequestHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#CLIENT_REQUEST" "Open CLIENT_REQUEST pointcut docs"
    click ResponseHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#CLIENT_RESPONSE" "Open CLIENT_RESPONSE pointcut docs"
```
