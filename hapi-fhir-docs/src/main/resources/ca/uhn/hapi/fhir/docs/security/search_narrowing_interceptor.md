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

<a name="constraining-by-valueset-membership"/>

# Constraining by ValueSet Membership

SearchNarrowingInterceptor can also be used to narrow searches by automatically appending `token:in` and `token:not-in` parameters.

In the example below, searches are narrowed as shown below:

* Searches for http://localhost:8000/Observation become http://localhost:8000/Observation?code:in=http://hl7.org/fhir/ValueSet/observation-vitalsignresult
* Searches for http://localhost:8000/Encounter become http://localhost:8000/Encounter?class:not-in=http://my-forbidden-encounter-classes

Important note: ValueSet Membership rules are only applied in cases where the ValueSet expansion has a code count below a configurable threshold (default is 500). To narrow searches with a larger ValueSet expansion, it is necessary to also enable [ResultSet Narrowing](#resultset-narrowing).

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|narrowingByCode}}
```


<a name="resultset-narrowing"/>

# ResultSet Narrowing

<div class="helpInfoCalloutBox">
ResultSet narrowing currently applies only to <a href="#constraining-by-valueset-membership">Constraining by ValueSet 
Membership</a>. ResultSet narrowing may be added for other types of search narrowing (e.g. by compartment) in a future 
release. 
</div>

By default, narrowing will simply modify search parameters in order to automatically constrain the results that are returned to the client. This is helpful for situations where the resource type you are trying to filter is the primary type of the search, but is less helpful when it is not.

For example suppose you wanted to narrow searches for Observations to only include Observations with a code in `http://my-value-set`. When a search is performed for `Observation?subject=Patient/123` the SearchNarrowingInterceptor will typically modify this to be performed as `Observation?subject=Patient/123&code:in=http://my-value-set`.

However this is not always possible:

* If the ValueSet expansion is too large, it is inefficient to use it in an `:in` clause and the SearchNarrowingInterceptor will not do so.
* If the result in question is fetched through an `_include` or `_revinclude` parameter, it is not possible to filter it by adding URL parameters.
* If the result in question is being returned as a result of an operation (e.g. `Patient/[id]/$expand`), it is not possible to filter it by adding URL parameters.

To enable ResultSet narrowing, the SearchNarrowingInterceptor is used along with the ConsentInterceptor, and the ConsentInterceptor is configured to include a companion consent service implementation that works with search narrowing rules. This is shown in the following example:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/AuthorizationInterceptors.java|rsnarrowing}}
``` 

