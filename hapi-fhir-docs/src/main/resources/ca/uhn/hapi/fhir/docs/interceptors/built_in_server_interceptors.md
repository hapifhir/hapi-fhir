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

<a name="request-tenant-partition-interceptor"/>

# Partitioning: Multitenant Request Partition

If the JPA server has [partitioning](/docs/server_jpa_partitioning/partitioning.html) enabled, the RequestTenantPartitionInterceptor can be used in combination with a [Tenant Identification Strategy](/docs/server_plain/multitenancy.html) in order to achieve a multitenant solution. See [JPA Server Partitioning](/docs/server_jpa_partitioning/partitioning.html) for more information on partitioning.

* [RequestTenantPartitionInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/partition/RequestTenantPartitionInterceptor.html)
* [RequestTenantPartitionInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/partition/RequestTenantPartitionInterceptor.java)


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

<a name="request_and_response_validation"/>

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

**See Also:** The [Repository Validating Interceptor](/docs/validation/repository_validating_interceptor.html) provides a different and potentially more powerful way of validating data when paired with a HAPI FHIR JPA Server.


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

# Subscription: Subscription Debug Log Interceptor

When using Subscriptions, the debug log interceptor can be used to add a number of additional lines to the server logs showing the internals of the subscription processing pipeline.

* [SubscriptionDebugLogInterceptor JavaDoc](/apidocs/hapi-fhir-jpaserver-subscription/ca/uhn/fhir/jpa/subscription/util/SubscriptionDebugLogInterceptor.html)
* [SubscriptionDebugLogInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-jpaserver-subscription/src/main/java/ca/uhn/fhir/jpa/subscription/util/SubscriptionDebugLogInterceptor.java)


# Request Pre-Processing: Override Meta.source

If you wish to override the value of `Resource.meta.source` using the value	supplied in an HTTP header, you can use the CaptureResourceSourceFromHeaderInterceptor to accomplish this.

* [CaptureResourceSourceFromHeaderInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/CaptureResourceSourceFromHeaderInterceptor.html)
* [CaptureResourceSourceFromHeaderInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/CaptureResourceSourceFromHeaderInterceptor.java)

# Utility: ResponseSizeCapturingInterceptor

The ResponseSizeCapturingInterceptor can be used to capture the number of characters written in each HTTP FHIR response.

* [ResponseSizeCapturingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/ResponseSizeCapturingInterceptor.html)
* [ResponseSizeCapturingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/ResponseSizeCapturingInterceptor.java)

# JPA Server: Allow Cascading Deletes

* [CascadingDeleteInterceptor JavaDoc](/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/interceptor/CascadingDeleteInterceptor.html)
* [CascadingDeleteInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-jpaserver-base/src/main/java/ca/uhn/fhir/jpa/interceptor/CascadingDeleteInterceptor.java)

The CascadingDeleteInterceptor allows clients to request deletes be cascaded to other resources that contain incoming references. See [Cascading Deletes](/docs/server_jpa/configuration.html#cascading-deletes) for more information. 


<a name="overridepathbasedreferentialintegrityfordeletesinterceptor"/>

# JPA Server: Disable Referential Integrity for Some Paths

* [OverridePathBasedReferentialIntegrityForDeletesInterceptor JavaDoc](/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/interceptor/OverridePathBasedReferentialIntegrityForDeletesInterceptor.html)
* [OverridePathBasedReferentialIntegrityForDeletesInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-jpaserver-base/src/main/java/ca/uhn/fhir/jpa/interceptor/OverridePathBasedReferentialIntegrityForDeletesInterceptor.java)

The OverridePathBasedReferentialIntegrityForDeletesInterceptor can be registered and configured to allow resources to be deleted even if other resources have outgoing references to the deleted resource. While it is generally a bad idea to allow deletion of resources that are referred to from other resources, there are circumstances where it is desirable. For example, if you have Provenance or AuditEvent resources that refer to a Patient resource that was created in error, you might want to alow the Patient to be deleted while leaving the Provenance and AuditEvent resources intact (including the now-invalid outgoing references to that Patient).

This interceptor uses FHIRPath expressions to indicate the resource paths that should not have referential integrity applied to them. For example, if this interceptor is configured with a path of `AuditEvent.agent.who`, a Patient resource would be allowed to be deleted even if one or more AuditEvents had references in that path to the given Patient (unless other resources also had references to the Patient).  
  

# JPA Server: Retry on Version Conflicts

The UserRequestRetryVersionConflictsInterceptor allows clients to request that the server avoid version conflicts (HTTP 409) when two concurrent client requests attempt to modify the same resource. See [Version Conflicts](/docs/server_jpa/configuration.html#retry-on-version-conflict) for more information. 

# JPA Server: Validate Data Being Stored

The RepositoryValidatingInterceptor can be used to enforce validation rules on data stored in a HAPI FHIR JPA Repository. See [Repository Validating Interceptor](/docs/validation/repository_validating_interceptor.html) for more information. 
