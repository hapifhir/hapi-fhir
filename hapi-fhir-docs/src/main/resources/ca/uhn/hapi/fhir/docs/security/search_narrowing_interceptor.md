# Search Narrowing Interceptor

The [SearchNarrowingInterceptor](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/auth/SearchNarrowingInterceptor.html) can be used to automatically narrow or constrain the scope of FHIR searches.

* [SearchNarrowingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/auth/SearchNarrowingInterceptor.html)
* [SearchNarrowingInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/auth/SearchNarrowingInterceptor.java)

This interceptor is designed to be used in conjunction with the [Authorization Interceptor](./authorization_interceptor.html). It uses a similar strategy where a dynamic list is built up for each request, but the purpose of this interceptor is to modify client searches that are received (after HAPI FHIR receives the HTTP request, but before the search is actually performed) to restrict the search to only search for specific resources or compartments that the user has access to.

This could be used, for example, to allow the user to perform a search for:

```url
http://baseurl/Observation?category=laboratory
```

...and then receive results as though they had requested:

```url
http://baseurl/Observation?subject=Patient/123&category=laboratory
```

An example of this interceptor follows:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|narrowing}}
``` 

# Constraining by ValueSet Membership

SearchNarrowingInterceptor can also be used to narrow searches by automatically appending `token:in` and `token:not-in` parameters.

In the example below, searches would be narrows as shown below:

* Searches for http://localhost:8000/Observation become http://localhost:8000/Observation?code:in=http://hl7.org/fhir/ValueSet/observation-vitalsignresult
* Searches for http://localhost:8000/Encounter become http://localhost:8000/Encounter?class:not-in=http://my-forbidden-encounter-classes

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|narrowingByCode}}
``` 
