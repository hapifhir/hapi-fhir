# Search Narrowing Interceptor

HAPI FHIR 3.7.0 introduced a new interceptor, the [SearchNarrowingInterceptor](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/auth/SearchNarrowingInterceptor.html).

* [SearchNarrowingInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/auth/SearchNarrowingInterceptor.html)
* [SearchNarrowingInterceptor Source](https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/auth/SearchNarrowingInterceptor.java)

This interceptor is designed to be used in conjunction with AuthorizationInterceptor. It uses a similar strategy where a dynamic list is built up for each request, but the purpose of this interceptor is to modify client searches that are received (after HAPI FHIR received the HTTP request, but before the search is actually performed) to restrict the search to only search for specific resources or compartments that the user has access to.

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

