# Consent Interceptor

HAPI FHIR 4.0.0 introduced a new interceptor, the [ConsentInterceptor](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/consent/ConsentInterceptor.html).

The consent interceptor may be used to examine client requests to apply consent directives and create audit trail events. Like the AuthorizationInterceptor above, this interceptor is not a complete working solution, but instead is a framework designed to make it easier to implement local policies.

* [ConsentInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/consent/ConsentInterceptor.html)
* [ConsentInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/consent/ConsentInterceptor.java)

The consent interceptor has several primary purposes:

** It can reject a resource from being disclosed to the user by examining it while calculating search results. This calculation is performed very early in the process of building search results, in order to ensure that in many cases the user is unaware that results have been removed.

* It can redact results, removing specific elements before they are returned to a client.

* It can create audit trail records (e.g. using an AuditEvent resource)

* It can apply consent directives (e.g. by reading relevant Consent resources)

* The consent service suppresses search the total being returned in Bundle.total for search results, even if the user explicitly requested them using the `_total=accurate` or `_summary=count` parameter.

The ConsentInterceptor requires a user-supplied instance of the [IConsentService](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/consent/IConsentService.html) interface. The following shows a simple example of an IConsentService implementation:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ConsentInterceptors.java|service}}
``` 
