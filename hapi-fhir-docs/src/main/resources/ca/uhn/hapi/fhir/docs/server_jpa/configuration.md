# JPA Server Configuration Options

## External/Absolute Resource References

Clients may sometimes post resources to your server that contain absolute resource references. For example, consider the following resource:

```xml
<Patient xmlns="http://hl7.org/fhir">
   <id value="patient-infant-01"/>
   <name>
      <use value="official"/>
      <family value="Miller"/>
      <given value="Samuel"/>
   </name>
   <managingOrganization>
      <reference value="http://example.com/fhir/Organization/123"/>
   </managingOrganization>
</Patient>
```

By default, the server will reject this reference, as only local references are permitted by the server. This can be changed however.

If you want the server to recognize that this URL is actually a local reference (i.e. because the server will be deployed to the base URL `http://example.com/fhir/`) you can configure the server to recognize this URL via the following DaoConfig setting:

```java
@Bean
public DaoConfig daoConfig() {
	DaoConfig retVal = new DaoConfig();
	// ... other config ...
	retVal.getTreatBaseUrlsAsLocal().add("http://example.com/fhir/");
	return retVal;
}
``` 

On the other hand, if you want the server to be configurable to allow remote references, you can set this with the configuration below. Using the `setAllowExternalReferences` means that it will be possible to search for references that refer to these external references.

```java
@Bean
public DaoConfig daoConfig() {
	DaoConfig retVal = new DaoConfig();
	// Allow external references
	retVal.setAllowExternalReferences(true);
	
	// If you are allowing external references, it is recommended to
	// also tell the server which references actually will be local
	retVal.getTreatBaseUrlsAsLocal().add("http://mydomain.com/fhir");
	return retVal;
}
```

## Logical References

In some cases, you may have references which are <i>Logical References</i>,
which means that they act as an identifier and not necessarily as a literal
web address.

A common use for logical references is in references to conformance resources, such as ValueSets, StructureDefinitions, etc. For example, you might refer to the ValueSet `http://hl7.org/fhir/ValueSet/quantity-comparator` from your own resources. In this case, you are not necessarily telling the server that this is a real address that it should resolve, but rather that this is an identifier for a ValueSet where `ValueSet.url` has the given URI/URL.

HAPI can be configured to treat certain URI/URL patterns as logical by using the DaoConfig#setTreatReferencesAsLogical property (see [JavaDoc](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/dao/DaoConfig.html#setTreatReferencesAsLogical(java.util.Set))).

For example:

```java
// Treat specific URL as logical
myDaoConfig.getTreatReferencesAsLogical().add("http://mysystem.com/ValueSet/cats-and-dogs");

// Treat all references with given prefix as logical
myDaoConfig.getTreatReferencesAsLogical().add("http://mysystem.com/mysystem-vs-*");
```

# Search Result Caching

By default, search results will be cached for one minute. This means that if a client performs a search for <code>Patient?name=smith</code> and gets back 500 results, if a client performs the same search within 60000 milliseconds the previously loaded search results will be returned again. This also means that any new Patient resources named "Smith" within the last minute will not be reflected in the results.

Under many normal scenarios this is a n acceptable performance tradeoff, but in some cases it is not. If you want to disable caching, you have two options:

### Globally Disable / Change Caching Timeout

You can change the global cache using the following setting:

```java
myDaoConfig.setReuseCachedSearchResultsForMillis(null);
```

### Disable Cache at the Request Level

Clients can selectively disable caching for an individual request using the Cache-Control header:

```http
Cache-Control: no-cache
```

### Disable Paging at the Request Level

If the client knows that they will only want a small number of results (for example, a UI containing 20 results is being shown and the client knows that they will never load the next page of results) the client
may also use the <code>no-store</code> directive along with a HAPI FHIR extension called <code>max-results</code> in order to specify that only the given number of results should be fetched. This directive disabled paging entirely for the request and causes the request to return immediately when the given number of results is found. This can cause a noticeable performance improvement in some cases.

```http
Cache-Control: no-store, max-results=20
```

# Additional Information

* [This page](https://www.openhealthhub.org/t/hapi-terminology-server-uk-snomed-ct-import/592) has information on loading national editions (UK specifically) of SNOMED CT files into the database.

# Cascading Deletes

An interceptor called `CascadingDeleteInterceptor` may be registered against the Server. When this interceptor is enabled, cascading deletes may be performed using either of the following:

* The request may include the following parameter: `_cascade=delete`
* The request may include the following header: `X-Cascade: delete`

