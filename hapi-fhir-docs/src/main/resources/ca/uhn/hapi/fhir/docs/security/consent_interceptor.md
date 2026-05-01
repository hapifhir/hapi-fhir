# Consent Interceptor

HAPI FHIR 4.0.0 introduced a new interceptor, the [ConsentInterceptor](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/consent/ConsentInterceptor.html).

The consent interceptor may be used to examine client requests to apply consent directives and create audit trail events. Like the AuthorizationInterceptor above, this interceptor is not a complete working solution, but instead is a framework designed to make it easier to implement local policies.

* [ConsentInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/consent/ConsentInterceptor.html)
* [ConsentInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/consent/ConsentInterceptor.java)

The consent interceptor has several primary purposes:

* It can reject a resource from being disclosed to the user by examining it while calculating search results. This calculation is performed very early in the process of building search results, in order to ensure that in many cases the user is unaware that results have been removed.

* It can redact results, removing specific elements before they are returned to a client.

* It can create audit trail records (e.g. using an AuditEvent resource)

* It can apply consent directives (e.g. by reading relevant Consent resources)

* The consent service suppresses search the total being returned in Bundle.total for search results, even if the user explicitly requested them using the `_total=accurate` or `_summary=count` parameter.

The ConsentInterceptor requires a user-supplied instance of the [IConsentService](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/consent/IConsentService.html) interface. The following shows a simple example of an IConsentService implementation:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ConsentInterceptors.java|service}}
``` 

## Performance and Privacy

Filtering search results in `canSeeResource()` requires inspecting every resource during a search and editing the results.
This is slower than the normal path, and will prevent the reuse of the results from the search cache.
The `willSeeResource()` operation supports reusing cached search results, but removed resources may be 'visible' as holes in returned bundles.
Disabling `canSeeResource()` by returning `false` from `processCanSeeResource()` will enable the search cache.

## Write Operation Responses

`willSeeResource()` is invoked for every resource that is about to be returned to the client, including the resource
body that is echoed back from a successful write operation (`POST`/`PUT`/`PATCH`) when the client has requested the
`Prefer: return=representation` behaviour (the FHIR default). This lets the consent service mask, redact, or fully
suppress the resource that the server echoes back after a write, using the same logic that protects read and search
responses.

The behaviour for a write response follows the same rules as a read response:

* `PROCEED` / `AUTHORIZED`: The (possibly modified) resource is returned in the response body and the original
  success status (`200 OK` or `201 Created`) is preserved.
* `REJECT` *with* a replacement `OperationOutcome` on the `ConsentOutcome`: The `OperationOutcome` is returned in
  the response body in place of the written resource. The original success status is preserved.
* `REJECT` *without* a replacement resource: The response body is suppressed and the status code is changed to
  `204 No Content`. The write itself is **not** rolled back &mdash; the resource is still persisted on the server,
  the consent service has only suppressed the echoed response body.

If your consent service needs to prevent the *write itself* (rather than just hide its response from the caller),
use `startOperation()` to reject the request before storage takes place, or pair the `ConsentInterceptor` with an
`AuthorizationInterceptor` that denies the write.

<a id="pre-authorizing-requests"></a>

## Pre-Authorizing Requests

In some situations, it is useful to pre-authorize a request programmatically. This can be achieved by 
overriding the `authorizeRequest(RequestDetails theRequestDetails)` method of the `ConsentInterceptor`. 

The example below shows how this method could be overridden in a system that uses user data in the `RequestDetails`
to indicate that a request should be pre-authorized.

```java
@Override
public void authorizeRequest(RequestDetails theRequestDetails) {
    String userDataValue = (String) theRequestDetails.getUserData().get("SOME_KEY");
    if ("SOME_VALUE".equals(userDataValue)){
        super.authorizeRequest(theRequestDetails);
    }
}
```


