# Built-In Server Interceptors

This page describes some server interceptors that are shipped with HAPI FHIR out of the box. Of course, you are also welcome to create your own.

<a name="logging_interceptor"/>

# Logging: Logging Interceptor

The LoggingInterceptor can be used to generate a new log line (via SLF4j) for each incoming request. LoggingInterceptor provides a flexible message format that can be used to provide a customized level detail about each incoming request.

* [LoggingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/LoggingInterceptor.html)
* [LoggingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/LoggingInterceptor.java)

The following example shows how to register a logging interceptor within a FHIR RESTful server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServletExamples.java|loggingInterceptor}}
```

This interceptor will then produce output similar to the following:

```bash
2014-09-04 02:37:30.030 Source[127.0.0.1] Operation[vread Patient/1667/_history/1] UA[Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.94 Safari/537.36] Params[?_format=json]
2014-09-04 03:30:00.443 Source[127.0.0.1] Operation[search-type Organization] UA[Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)] Params[]
```

# Response Customizing: Syntax Highlighting

The ResponseHighlighterInterceptor detects when a request is coming from a browser and returns HTML with syntax highlighted XML/JSON instead of just the raw text. In other words, if a user uses a browser to request `http://foo/Patient/1` by typing this address into their URL bar, they will get a nicely formatted HTML back with a human readable version of the content. This is particularly helpful for testers and public/development APIs where users are likely to invoke the API directly to see how it works.

* [ResponseHighlighterInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/ResponseHighlighterInterceptor.html)
* [ResponseHighlighterInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/ResponseHighlighterInterceptor.java)

To see an example of how the output of this interceptor looks, see our demo server using the following example query: [http://hapi.fhir.org/baseR4/Patient](http://hapi.fhir.org/baseR4/Patient). The HTML view you see in that page with colour and indenting is provided by ResponseHighlighterInterceptor. Without this interceptor the response will simply be raw JSON/XML (as it will also be with this interceptor if the request is not coming from a browser, or is invoked by JavaScript).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServletExamples.java|responseHighlighterInterceptor}}
```

# Response Customizing: Exception Handling

The ExceptionHandlingInterceptor can be used to customize what is returned to the client and what is logged when the server throws an exception for any reason (including routine things like UnprocessableEntityExceptions thrown as a matter of normal processing in a create method, but also including unexpected exceptions thrown by client code).

* [ExceptionHandlingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/ExceptionHandlingInterceptor.html)
* [ExceptionHandlingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/ExceptionHandlingInterceptor.java)

The following example shows how to register the ExceptionHandlingInterceptor.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServletExamples.java|exceptionInterceptor}}
```

# Response Customizing: Evaluate FHIRPath

The FhirPathFilterInterceptor looks for a request URL parameter in the form `_fhirpath=(expression)` in all REST requests. If this parameter is found, the value is treated as a [FHIRPath](http://hl7.org/fhirpath/) expression. The response resource will be replaced with a [Parameters](http://hl7.org/fhir/parameters.html) resource containing the results of the given expression applied against the response resource.   

* [FhirPathFilterInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/FhirPathFilterInterceptor.html)
* [FhirPathFilterInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/FhirPathFilterInterceptor.java)

The following example shows how to register the ExceptionHandlingInterceptor.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServletExamples.java|exceptionInterceptor}}
```

An example URL to invoke this function is shown below:

```url
https://hapi.fhir.org/baseR4/Patient?_fhirpath=Bundle.entry.resource.as(Patient).name&_pretty=true
```

A sample response to this query is shown below:

```json
{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "result",
    "part": [ {
      "name": "expression",
      "valueString": "Bundle.entry.resource.as(Patient).name"
    }, {
      "name": "result",
      "valueHumanName": {
        "family": "Simpson",
        "given": [ "Homer", "Jay" ]
      }
    }, {
      "name": "result",
      "valueHumanName": {
        "family": "Simpson",
        "given": [ "Grandpa" ]
      }
    } ]
  } ]
}
```

# Validation: Request and Response Validation

HAPI FHIR provides a pair of interceptors that can be used to validate incoming requests received by the server, as well as outgoing responses generated by the server.

The RequestValidatingInterceptor and ResponseValidatingInterceptor can be used to perform validation of resources on their way into and out of the server respectively.

* [RequestValidatingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/RequestValidatingInterceptor.html)
* [RequestValidatingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/RequestValidatingInterceptor.java)
* [ResponseValidatingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/ResponseValidatingInterceptor.html)
* [ResponseValidatingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/ResponseValidatingInterceptor.java)

The RequestValidatingInterceptor looks at resources coming into the server (e.g. for create, update, $operations, transactions, etc.) and validates them. The ResponseValidatingInterceptor looks at resources being returned by the server (e.g. for read, search, $operations, etc.) and validates them.

These interceptors can be configured to add headers to the response, fail the response (returning an HTTP 422 and throwing an exception in the process), or to add to the OperationOutcome returned by the server.

See [Instance Validator](/docs/validation/instance_validator.html) for information on how validation works in HAPI FHIR.

The following example shows how to register this interceptor within a HAPI FHIR REST server.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServletExamples.java|validatingInterceptor}}
```

# Security: CORS

HAPI FHIR includes an interceptor which can be used to implement CORS support on your server. See [Server CORS Documentation](/docs/security/cors.html#cors_interceptor) for information on how to use this interceptor.


# Security: Authorization

HAPI FHIR provides a powerful interceptor that can be used to implement user- and system-level authorization rules that are aware of FHIR semantics. See [Authorization](/docs/security/authorization_interceptor.html) for more information.


# Security: Consent

HAPI FHIR provides an interceptor that can be used to implement consent rules and directives. See [Consent Interceptor](/docs/security/consent_interceptor.html) for more information.
  

# Security: Search Narrowing

HAPI FHIR provides an interceptor that can be used to implement consent rules and directives. See [Consent Interceptor](/docs/security/consent_interceptor.html) for more information.


# Security: Rejecting Unsupported HTTP Verbs

Some security audit tools require that servers return an HTTP 405 if an unsupported HTTP verb is received (e.g. TRACE). The BanUnsupportedHttpMethodsInterceptor can be used to accomplish this.

* [BanUnsupportedHttpMethodsInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/BanUnsupportedHttpMethodsInterceptor.html)
* [BanUnsupportedHttpMethodsInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/BanUnsupportedHttpMethodsInterceptor.java)


# Request Pre-Processing: Override Meta.source

If you wish to override the value of `Resource.meta.source` using the value	supplied in an HTTP header, you can use the CaptureResourceSourceFromHeaderInterceptor to accomplish this.

* [CaptureResourceSourceFromHeaderInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/CaptureResourceSourceFromHeaderInterceptor.html)
* [CaptureResourceSourceFromHeaderInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/CaptureResourceSourceFromHeaderInterceptor.java)

# Utility: ResponseSizeCapturingInterceptor

The ResponseSizeCapturingInterceptor can be used to capture the number of characters written in each HTTP FHIR response.

* [ResponseSizeCapturingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/ResponseSizeCapturingInterceptor.html)
* [ResponseSizeCapturingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/ResponseSizeCapturingInterceptor.java)

