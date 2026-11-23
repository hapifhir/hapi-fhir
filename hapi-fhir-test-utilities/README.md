<!-- Created by Claude Opus 5.5 -->
# HAPI FHIR Test Utilities

Shared test infrastructure for HAPI FHIR modules: server extensions, mocks, and the HTTP test harness. This
README covers migrating tests to the HTTP test harness. The other classes are documented in their javadoc.

## Issuing HTTP requests in tests

Use `HttpTestRequest` / `HttpTestResponse` (`ca.uhn.fhir.test.utilities`). How to start a request, build it,
check the response, and which tests stay on raw Apache HttpClient are documented in
[Testing a Server over HTTP](https://hapifhir.io/hapi-fhir/docs/server_plain/testing.html)
(source: `hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_plain/testing.md`).

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
| `ctx.newJsonParser().parseResource(Bundle.class, body)` | `.getBodyAsResource(Bundle.class)`; if the test asked for that encoding, add `.assertMimeType(...)` before it, since the old parser also checked the encoding |
| `JsonUtil.deserialize(body, MyJson.class)` | `.getBodyAsType(MyJson.class)` |
| `resp.getFirstHeader(name).getValue()` | `.getHeader(name)` |
| `new IdType(resp.getFirstHeader("Location").getValue())` | `.getLocationId()` |
| `resp.getEntity().getContentType().getValue()` | `.getMimeType()` (parameters such as `charset` stripped) or `.assertMimeType(...)` |
| `HttpTestRequest.to(myServer.getHttpClient(), absoluteUrl)` | `myServer.fhirRequestToUrl(absoluteUrl)` |
| a try-with-resources block and `throws IOException` | neither |

`HttpClientExtension` is deprecated and will be removed in a future release. A test with a server extension uses
its `fhirRequest(path)`. A test that starts its own server uses `HttpTestRequest.to(client, url)` with a client
from `TestHttpClientFactory.create()`, and closes that client after the test.
