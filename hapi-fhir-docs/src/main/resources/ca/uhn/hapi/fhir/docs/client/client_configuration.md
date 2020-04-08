# Client Configuration

This page outlines ways that the client can be configured for specific behaviour.

<a name="performance"/>

# Performance

## Server Conformance Check

By default, the client will query the server before the very first operation to download the server's conformance/metadata statement and verify that the server is appropriate for the given client. This check is only done once per server endpoint for a given FhirContext.

This check is useful to prevent bugs or unexpected behaviour when talking to servers. It may introduce unnecessary overhead however in circumstances where the client and server are known to be compatible. The following example shows how to disable this check.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|dontValidate}}
```

## Deferred Model Scanning

By default, HAPI will scan each model type it encounters as soon as it encounters it. This scan includes a check for all fields within the type, and makes use of reflection to do this.

While this process is not particularly significant on reasonably performant machines (one benchmark showed that this takes roughly 0.6 seconds to scan all types on one developer workstation), on some devices (e.g. Android phones where every millisecond counts) it may be desirable to defer this scan.

When the scan is deferred, objects will only be scanned when they are actually accessed, meaning that only types that are actually used in an application get scanned. 

The following example shows how to defer model scanning:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/GenericClientExample.java|deferModelScanning}}
```

# Configuring the HTTP Client

REST clients (both Generic and Annotation-Driven) use [Apache HTTP Client](http://hc.apache.org/httpcomponents-client-ga/) as a provider by default (except on Android, where [OkHttp](http://square.github.io/okhttp/) is the default).

The Apache HTTP Client is very powerful and extremely flexible, but can be confusing at first to configure, because of the low-level approach that the library uses.

In many cases, the default configuration should suffice. HAPI FHIR also encapsulates some of the more common configuration settings you might want to use (socket timeouts, proxy settings, etc.) so that these can be configured through HAPI's API without needing to understand the underlying HTTP Client library.

This configuration is provided by accessing the [IRestfulClientFactory](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/client/api/IRestfulClientFactory.html) class from the FhirContext.

Note that individual requests and responses can be tweaked using [Client Interceptors](/docs/interceptors/client_interceptors.html). This approach is generally useful for configuration involving tweaking the HTTP request/response, such as adding authorization headers or logging.

## Setting Socket Timeouts

The following example shows how to configure low level socket timeouts for client operations.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|timeouts}}
```

## Configuring an HTTP Proxy

The following example shows how to configure the use of an HTTP proxy in the client.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|proxy}}
```

## Using OkHttp instead of Apache HttpClient

As of HAPI FHIR 2.0, an alternate client implementation is available. This client replaces the low-level Apache HttpClient implementation with the Square [OkHttp](http://square.github.io/okhttp/) library.

Changing HTTP implementations should be mostly transparent (it has no effect on the actual FHIR semantics which are transmitted over the wire) but might be useful if you have an application that uses OkHttp in other parts of the application and has specific configuration for that library.

Note that as of HAPI FHIR 2.1, OkHttp is the default provider on Android, and will be used without any configuration being required. This is done because HttpClient is deprecated on Android and has caused problems in the past.

To use OkHttp, first add the library as a dependency to your project POM:

```xml
<dependency>
    <groupId>ca.uhn.hapi.fhir</groupId>
    <artifactId>hapi-fhir-client-okhttp</artifactId>
    <version>${hapi_stable_version}</version>		
</dependency>
```

Then, set the client factory to use OkHttp.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ClientExamples.java|okhttp}}
```
