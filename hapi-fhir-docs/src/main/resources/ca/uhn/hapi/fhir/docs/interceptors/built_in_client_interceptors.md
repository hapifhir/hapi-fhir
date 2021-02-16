# Built-In Client Interceptors

This page describes some client interceptors that are shipped with HAPI FHIR out of the box. Of course, you are also welcome to create your own.

<a name="logging_interceptor"/>

# Logging: Logging Interceptor

The LoggingInterceptor logs details about each request and/or response that is performed using the client. All logging is performed using SLF4j.

* [LoggingInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/LoggingInterceptor.html)
* [LoggingInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/LoggingInterceptor.java)

LoggingInterceptor is highly configurable in terms of its output. It can be configured to log simple details about requests, or detailed output including payload bodies and header contents. The following example shows how to enable LoggingInterceptor. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|logging}}
```

# Security: HTTP Basic Authorization

The BasicAuthInterceptor adds an `Authorization` header containing an HTTP Basic Auth (username+password) token in every outgoing request.

* [BasicAuthInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/BasicAuthInterceptor.html)
* [BasicAuthInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/BasicAuthInterceptor.java)

The following example shows how to configure your client to	use a specific username and password in every request.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|security}}
```

# Security: HTTP Bearer Token Authorization

The BearerTokenAuthInterceptor can be used to add an `Authorization` header containing a bearer token (typically used for OIDC/OAuth2/SMART security flows) to every outgoing request.

* [BearerTokenAuthInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/BearerTokenAuthInterceptor.html)
* [BearerTokenAuthInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/BearerTokenAuthInterceptor.java)

The following example shows how to configure your client to inject a bearer token authorization header into every request.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|securityBearer}}
```

# Misc: Add Headers to Request

The AdditionlRequestHeadersInterceptor can be used to add arbitrary headers to each request created by the client.

* [AdditionalRequestHeadersInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/AdditionalRequestHeadersInterceptor.html)
* [AdditionalRequestHeadersInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/AdditionalRequestHeadersInterceptor.java)

The following example shows how to configure your client to inject a bearer token authorization header into every request.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|addHeaders}}
```

Note that headers can also be added to individual [Generic Client](/docs/client/generic_client.html) invocations inline. The example below will produce the same additional request header as the example above, although it applies only to the one request. 

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|addHeadersNoInterceptor}}
```

# Misc: Add Cookies to Request

The CookieInterceptor can be used to add an HTTP Cookie header to each request created by the client.

* [CookieInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/CookieInterceptor.html)
* [CookieInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/CookieInterceptor.java)

The following example shows how to configure your client to inject a bearer token authorization header into every request.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|cookie}}
```

# Multitenancy: Add tenant ID to path

When communicating with a server that supports [URL Base Multitenancy](/docs/server_plain/multitenancy.html#url-base-multitenancy), an extra element needs to be added to the request path. This can be done by simply appending the path to the base URL supplied to the client, but it can also be dynamically appended using this interceptor.

* [UrlTenantSelectionInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/UrlTenantSelectionInterceptor.html)
* [UrlTenantSelectionInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/UrlTenantSelectionInterceptor.java)

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|tenantId}}
```

# Performance: GZip Outgoing Request Bodies

The GZipContentInterceptor compresses outgoing contents. With this interceptor, if the client is transmitting resources to the server (e.g. for a create, update, transaction, etc.) the content will be GZipped before transmission to the server.

* [GZipContentInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/apache/GZipContentInterceptor.html)
* [GZipContentInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/apache/GZipContentInterceptor.java)

The following example shows how to enable the GZipContentInterceptor.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|gzip}}
```

# Capture: Programmatically Capturing Request/Response Details

The CapturingInterceptor can be used to capture the details of the last request that was sent by the client, as well as the corresponding response that was received. 

* [CapturingInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/CapturingInterceptor.html)
* [CapturingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/CapturingInterceptor.java)

A separate but related interceptor called ThreadLocalCapturingInterceptor also captures request/response pairs but stores these in a Java ThreadLocal so it is suitable for use in multithreaded environments.  

* [ThreadLocalCapturingInterceptor JavaDoc](/apidocs/hapi-fhir-client/ca/uhn/fhir/rest/client/interceptor/ThreadLocalCapturingInterceptor.html)
* [ThreadLocalCapturingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-client/src/main/java/ca/uhn/fhir/rest/client/interceptor/ThreadLocalCapturingInterceptor.java)
