<!-- Created by Claude Opus 5.5 -->
# HAPI FHIR Test Utilities

Shared test infrastructure for HAPI FHIR modules: server extensions, mocks, and the HTTP test harness. This
README covers the HTTP test harness. The other classes are documented in their javadoc.

## Issuing HTTP requests in tests

Use `HttpTestRequest` / `HttpTestResponse` (`ca.uhn.fhir.test.utilities`) when a test needs the raw wire
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
anything, and no checked exception is thrown, so a migrated test drops its `throws IOException`.

### Where to start a request

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

### Building the request

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

### Checking the response

Assertions return the response, so they chain. Every failure message includes the status line and the whole
body, so a test needs no logging of its own to diagnose one.

| Method | Checks or returns |
|---|---|
| `assertStatus(200)` | the status code; call it first |
| `assertBodyContains(...)` / `assertBodyDoesNotContain(...)` / `assertBodyEquals(...)` | the body text |
| `assertContentType(Constants.CT_FHIR_JSON_NEW)` | the MIME type, ignoring case and parameters such as `charset` |
| `assertHeader(name, value)` | the first value of a header, exactly |
| `getBodyAsResource(Bundle.class)` | the body parsed as a FHIR resource, JSON or XML per the response `Content-Type` |
| `getBodyAsJson(MyJson.class)` / `getBodyAsJsonNode()` | the body as a non-FHIR JSON model, or a tree |
| `getLocationId()` | the `Location` header as an `IIdType`, e.g. the ID a create returned |
| `getRedirectLocation()` | the `Location` of a `3xx`, after asserting it is one |
| `getBody()` / `getBodyBytes()` | the body as UTF-8 text, or the raw bytes |
| `getHeader(name)` / `getHeaders(name)` / `getContentType()` / `getStatusCode()` | the raw values, for AssertJ |

Use `assertHeader` and `assertBodyEquals` only for exact checks. For a value the test only partly knows, read it
with `getHeader` or `getBody` and use AssertJ.

## Migrating from raw Apache HttpClient

Before:

```java
HttpPost post = new HttpPost(myServerBase + "/Patient");
post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_JSON_NEW, StandardCharsets.UTF_8)));
try (CloseableHttpResponse status = ourHttpClient.execute(post)) {
	assertEquals(201, status.getStatusLine().getStatusCode());
	String id = status.getFirstHeader(Constants.HEADER_LOCATION).getValue();
	String body = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
	Patient created = myFhirContext.newJsonParser().parseResource(Patient.class, body);
	assertTrue(created.getActive());
}
```

After:

```java
HttpTestResponse response = myServer.fhirRequest("/Patient")
	.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
	.post(patient)
	.assertStatus(201);
IIdType id = response.getLocationId();
assertThat(response.getBodyAsResource(Patient.class).getActive()).isTrue();
```

| Before | After |
|---|---|
| `new HttpGet(base + path)` + `client.execute(get)` | `myServer.fhirRequest(path).get()` |
| `post.setEntity(new StringEntity(encodedResource, ...))` | `.post(resource)` |
| `post.setEntity(new UrlEncodedFormEntity(pairs))` | `.withFormParam(name, value)` for each pair, then `.postForm()` |
| `request.addHeader(name, value)` | `.withHeader(name, value)` |
| `"?identifier=" + UrlUtil.escapeUrlParam(value)` | `.withQueryParam("identifier", value)` |
| `assertEquals(200, resp.getStatusLine().getStatusCode())` | `.assertStatus(200)` |
| `IOUtils.toString(resp.getEntity().getContent(), UTF_8)` | `.getBody()` |
| `ctx.newJsonParser().parseResource(Bundle.class, body)` | `.getBodyAsResource(Bundle.class)`; if the test asked for that encoding, add `.assertContentType(...)` before it, since the old parser also checked the encoding |
| `JsonUtil.deserialize(body, MyJson.class)` | `.getBodyAsJson(MyJson.class)` |
| `resp.getFirstHeader(name).getValue()` | `.getHeader(name)` |
| `new IdType(resp.getFirstHeader("Location").getValue())` | `.getLocationId()` |
| `resp.getEntity().getContentType().getValue()` | `.getContentType()` (MIME type only) or `.assertContentType(...)` |
| `HttpTestRequest.to(myServer.getHttpClient(), absoluteUrl)` | `myServer.fhirRequestToUrl(absoluteUrl)` |
| a try-with-resources block and `throws IOException` | neither |

`HttpClientExtension` is deprecated. A test with a server extension uses its `fhirRequest(path)`. A test that
starts its own server uses `HttpTestRequest.to(client, url)` with a client from `TestHttpClientFactory.create()`,
and closes that client after the test.

### What stays on raw Apache HttpClient

The harness buffers the whole response and sends one request at a time. Tests that need something else keep the
client directly:

- tests of the HAPI FHIR client itself, where the client is the subject;
- streamed or truncated bodies, and `Expect: 100-continue`;
- multipart and gzip request bodies, and cookie inspection;
- TLS against `getSecureBase()`, where the test builds its own client.
