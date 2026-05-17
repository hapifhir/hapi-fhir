```mermaid
flowchart TB
{{snippet:file:hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/_snippets/mermaid_palette.mmd}}
    Client(["HTTP<br/>Client"]):::app
    Receive["Server Receives<br/>HTTP Request"]:::step
    MapURL["Server Maps Request<br/>URL into Operation"]:::step
    LocateHandler["Server Locates<br/>appropriate handler<br/>method"]:::step
    Process["Server Processes<br/>Operation"]:::step
    Response["Response<br/>Prepared"]:::step
    Sent["HTTP Response<br/>Sent"]:::step
    ExceptionTranslated["Exception Translated<br/>into OperationOutcome"]:::step

    PreProcHook["Java Hook:<br/>SERVER_INCOMING_REQUEST_PRE_PROCESSED"]:::javahook
    PreHandlerSelectedHook["Java Hook:<br/>SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED"]:::javahook
    PostProcHook["Java Hook:<br/>SERVER_INCOMING_REQUEST_POST_PROCESSED"]:::javahook
    PreHandledHook["Java Hook:<br/>SERVER_INCOMING_REQUEST_PRE_HANDLED"]:::javahook
    OutgoingHook["Java Hook:<br/>SERVER_OUTGOING_RESPONSE<br/>SERVER_OUTGOING_GRAPHQL_RESPONSE"]:::javahook
    WriterCreatedHook["Java Hook:<br/>SERVER_OUTGOING_WRITER_CREATED"]:::javahook
    CompletedNormallyHook["Java Hook:<br/>SERVER_PROCESSING_COMPLETED_NORMALLY"]:::javahook
    CompletedHook["Java Hook:<br/>SERVER_PROCESSING_COMPLETED"]:::javahook

    HandleExceptionHook["Java Hook:<br/>SERVER_HANDLE_EXCEPTION"]:::javahook
    PreProcExceptionHook["Java Hook:<br/>SERVER_PRE_PROCESS_OUTGOING_EXCEPTION"]:::javahook
    OutcomeHook["Java Hook:<br/>SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME"]:::javahook

    Client e1@-- "PUT /Patient/123<br/>Content-Type: application/json" --> Receive
    Receive -.-> PreProcHook
    PreProcHook e2@--> MapURL
    MapURL -.-> PreHandlerSelectedHook
    PreHandlerSelectedHook e3@--> LocateHandler
    LocateHandler -.-> PostProcHook
    PostProcHook e4@--> PreHandledHook
    PreHandledHook e5@--> Process

    Process e6@-- "normal" --> Response
    Response -.-> OutgoingHook
    OutgoingHook e7@--> WriterCreatedHook
    WriterCreatedHook e8@--> Sent

    Process e9@-- "exception" --> HandleExceptionHook
    HandleExceptionHook e10@--> PreProcExceptionHook
    PreProcExceptionHook e11@--> ExceptionTranslated
    ExceptionTranslated -.-> OutcomeHook
    OutcomeHook e12@--> Sent

    Sent e13@-- "normal" --> CompletedNormallyHook
    CompletedNormallyHook e14@--> CompletedHook
    Sent e15@-- "exception" --> CompletedHook
    CompletedHook e16@--> Client

    click PreProcHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_INCOMING_REQUEST_PRE_PROCESSED" "Open SERVER_INCOMING_REQUEST_PRE_PROCESSED pointcut docs"
    click PreHandlerSelectedHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED" "Open SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED pointcut docs"
    click PostProcHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_INCOMING_REQUEST_POST_PROCESSED" "Open SERVER_INCOMING_REQUEST_POST_PROCESSED pointcut docs"
    click PreHandledHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_INCOMING_REQUEST_PRE_HANDLED" "Open SERVER_INCOMING_REQUEST_PRE_HANDLED pointcut docs"
    click OutgoingHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html" "Open HAPI Pointcut docs (SERVER_OUTGOING_RESPONSE / SERVER_OUTGOING_GRAPHQL_RESPONSE)"
    click WriterCreatedHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_OUTGOING_WRITER_CREATED" "Open SERVER_OUTGOING_WRITER_CREATED pointcut docs"
    click CompletedNormallyHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_PROCESSING_COMPLETED_NORMALLY" "Open SERVER_PROCESSING_COMPLETED_NORMALLY pointcut docs"
    click CompletedHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_PROCESSING_COMPLETED" "Open SERVER_PROCESSING_COMPLETED pointcut docs"
    click HandleExceptionHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_HANDLE_EXCEPTION" "Open SERVER_HANDLE_EXCEPTION pointcut docs"
    click PreProcExceptionHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_PRE_PROCESS_OUTGOING_EXCEPTION" "Open SERVER_PRE_PROCESS_OUTGOING_EXCEPTION pointcut docs"
    click OutcomeHook "/apidocs/hapi-fhir-base/ca/uhn/fhir/interceptor/api/Pointcut.html#SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME" "Open SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME pointcut docs"
```
