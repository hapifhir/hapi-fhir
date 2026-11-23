# Testing a Server over HTTP
<!-- Created by Claude Opus 5.5 -->

The `hapi-fhir-test-utilities` module provides `HttpTestRequest` and `HttpTestResponse`, a request builder and a
response with assertions, for tests that send HTTP requests to a running FHIR server. HAPI FHIR's own tests use
them, and so can tests of a server you build with HAPI FHIR.

```xml
<dependency>
	<groupId>ca.uhn.hapi.fhir</groupId>
	<artifactId>hapi-fhir-test-utilities</artifactId>
	<version>VERSION</version>
	<scope>test</scope>
</dependency>
```

Use them (package `ca.uhn.fhir.test.utilities`) when a test needs the raw wire
response, such as the status, headers, body bytes or content type, rather than a parsed resource from
`IGenericClient`. They replace the build-request, execute-in-try-with-resources, read-entity, check-status
boilerplate:

```java
Patient patient = myServer.fhirRequest("/Patient/123")
	.get()
	.assertStatus(200)
	.getBodyAsResource(Patient.class);
```

The response is read fully into memory and the connection is closed before you get it. You never close
anything, and no checked exception is thrown, so the test needs no `throws IOException`.

# Where to Start a Request

| Your test has | Use |
|---|---|
| A `RestfulServerExtension` | `myServer.fhirRequest("/Patient/123")` |
| A URL the server returned (a Bundle `next` link, `Location`, a polling `Content-Location`) | `myServer.fhirRequestToUrl(url)` |
| A JPA test server helper (`BaseRestServerHelper`, e.g. `BaseResourceProviderR4Test.myServer`) | `myServer.fhirRequest("/Patient/123")` |
| An `HttpServletExtension` (a non-FHIR servlet) | `myServer.request("/path")` |
| Its own Jetty server and client | `HttpTestRequest.to(myClient, myFhirContext, url)`, with a client from `TestHttpClientFactory.create()` that the test closes |
| An HTTP client other than Apache HttpClient 4.x or 5.x | Implement `IHttpTestTransport` and use `HttpTestRequest.to(transport, ctx, url)` |

Pass a `FhirContext` (all the server entry points do) when the request sends a resource body or the test parses
the response with `getBodyAsResource`.

# Building the Request

| Method | Sends |
|---|---|
| `withHeader(name, value)` | any header; call once per header |
| `withQueryParam(name, value)` | a query parameter, percent-encoded as UTF-8, after any query already in the URL |
| `withBasicAuth(user, password)` / `withBearerToken(token)` | an `Authorization` header |
| `withPreferHandling(handling)` / `withLenient()` | `Prefer: handling=...` |
| `withFormParam(name, value)` then `postForm()` | an `application/x-www-form-urlencoded` body, e.g. for an OAuth token endpoint |
| `withoutRedirects()` | nothing; returns a `3xx` as-is instead of following it |

Then one verb: `get()`, `delete()`, `head()`, `options()`, `post(...)`, `put(...)`, `patch(...)`, `postJson(...)`,
`putJson(...)`, `postForm()`, or `method(...)` for any other verb.

- `post(resource)` / `put(resource)` send the resource as `application/fhir+json`.
- `post(string, contentType)` encodes with the charset the content type names, or UTF-8 when it names none.
  Use this when the test is about the content type or charset.
- `post(bytes, contentType)` sends the bytes unchanged, for binary payloads.
- `postJson(object)` / `putJson(object)` serialize a non-FHIR model (an `IModelJson`) as `application/json`.
- `patch(jsonPatch)` sends `application/json-patch+json`.

# Checking the Response

Assertions return the response, so they chain. Every failure message includes the status line and the whole
body, so a test needs no logging of its own to diagnose one.

| Method | Checks or returns |
|---|---|
| `assertStatus(200)` | the status code; call it first |
| `assertBodyContains(...)` / `assertBodyDoesNotContain(...)` / `assertBodyEquals(...)` | the body text |
| `assertMimeType(Constants.CT_FHIR_JSON_NEW)` | the MIME type, ignoring case and parameters such as `charset`; pass a type without parameters, or it throws |
| `assertHeader(name, value)` | the first value of a header, exactly |
| `getBodyAsResource(Bundle.class)` | the body parsed as a FHIR resource, JSON or XML per the response `Content-Type` |
| `getBodyAsType(MyJson.class)` / `getBodyAsJsonNode()` | the body as a non-FHIR JSON model, or a tree |
| `getLocationId()` | the `Location` header as an `IIdType`, e.g. the ID a create returned |
| `getRedirectLocation()` | the `Location` of a `3xx`, after asserting it is one |
| `getBody()` / `getBodyBytes()` | the body as UTF-8 text, or the raw bytes |
| `getHeader(name)` / `getHeaders(name)` / `getMimeType()` / `getStatusCode()` | the raw values, for AssertJ |

Use `assertHeader` and `assertBodyEquals` only for exact checks. For a value the test only partly knows, read it
with `getHeader` or `getBody` and use AssertJ.

# What Stays on Raw Apache HttpClient

The harness buffers the whole response and sends one request at a time. Tests that need something else keep the
client directly:

- tests of the HAPI FHIR client itself, where the client is the subject;
- streamed or truncated bodies, and `Expect: 100-continue`;
- multipart and gzip request bodies, and cookie inspection;
- TLS against `getSecureBase()`, where the test builds its own client.
