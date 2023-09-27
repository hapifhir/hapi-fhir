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

If you want the server to recognize that this URL is actually a local reference (i.e. because the server will be deployed to the base URL `http://example.com/fhir/`) you can configure the server to recognize this URL via the following JpaStorageSettings setting:

```java
@Bean
public JpaStorageSettings storageSettings() {
   JpaStorageSettings retVal = new JpaStorageSettings();
	// ... other config ...
	retVal.getTreatBaseUrlsAsLocal().add("http://example.com/fhir/");
	return retVal;
}
``` 

On the other hand, if you want the server to be configurable to allow remote references, you can set this with the configuration below. Using the `setAllowExternalReferences` means that it will be possible to search for references that refer to these external references.

```java
@Bean
public JpaStorageSettings storageSettings() {
   JpaStorageSettings retVal = new JpaStorageSettings();
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

A common use for logical references is in references to conformance resources, such as ValueSets, StructureDefinitions,
etc. For example, you might refer to the ValueSet `http://hl7.org/fhir/ValueSet/quantity-comparator` from your own
resources. In this case, you are not necessarily telling the server that this is a real address that it should resolve,
but rather that this is an identifier for a ValueSet where `ValueSet.url` has the given URI/URL.

HAPI can be configured to treat certain URI/URL patterns as logical by using the JpaStorageSettings#setTreatReferencesAsLogical
property (
see [JavaDoc](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/entity/StorageSettings.html#setTreatReferencesAsLogical(java.util.Set)))
.

For example:

```java
// Treat specific URL as logical
myStorageSettings.getTreatReferencesAsLogical().add("http://mysystem.com/ValueSet/cats-and-dogs");

// Treat all references with given prefix as logical
myStorageSettings.getTreatReferencesAsLogical().add("http://mysystem.com/mysystem-vs-*");
```

## Referential Integrity

Enabling referential integrity will ensure that reference values exist in the database. If the referenced entity does 
not exist, the server will return an error.

It is important to note that referential integrity is not enforced on database-level. The referential integrity check
*only* validates references that are indexed by a `SearchParameter`.

### Enabling Referential Integrity
Referential integrity can be configured on two levels: `write` and `delete`.

#### JPA Server
```java
@Bean
public JpaStorageSettings storageSettings() {
   JpaStorageSettings retVal = new JpaStorageSettings();
	// ... other config ...
	retVal.setEnforceReferentialIntegrityOnWrite(true);
	retVal.setEnforceReferentialIntegrityOnDelete(true);
	return retVal;
}
``` 
#### JPA Server Starter
This can be easily enabled in the `application.yaml` file at the following paths:
```yaml
hapi:
   fhir:
    enforce_referential_integrity_on_write: true
    enforce_referential_integrity_on_delete: true
```

# Search Result Caching

By default, search results will be cached for one minute. This means that if a client performs a search for <code>Patient?name=smith</code> and gets back 500 results, if a client performs the same search within 60000 milliseconds the previously loaded search results will be returned again. This also means that any new Patient resources named "Smith" within the last minute will not be reflected in the results.

Under many normal scenarios this is a n acceptable performance tradeoff, but in some cases it is not. If you want to disable caching, you have two options:

### Globally Disable / Change Caching Timeout

You can change the global cache using the following setting:

```java
myStorageSettings.setReuseCachedSearchResultsForMillis(null);
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


<a name="cascading-deletes"/>

# Cascading Deletes

An interceptor called `CascadingDeleteInterceptor` may be registered against the server. When this interceptor is enabled, cascading deletes may be performed using either of the following:

* The request may include the following parameter: `_cascade=delete`
* The request may include the following header: `X-Cascade: delete`

<a name="retry-on-version-conflict"/>

# Version Conflicts

If a server is serving multiple concurrent requests against the same resource, a [ResourceVersionConflictException](/hapi-fhir/apidocs/hapi-fhir-base/ca/uhn/fhir/rest/server/exceptions/ResourceVersionConflictException.html) may be thrown (resulting in an **HTTP 409 Version Conflict** being returned to the client). For example, if two client requests attempt to update the same resource at the exact same time, this exception will be thrown for one of the requests. This exception is not a bug in the server itself, but instead is a defense against client updates accidentally being lost because of concurrency issues. When this occurs, it is important to consider what the root cause might be, since concurrent writes against the same resource are often indicative of a deeper application design issue.

An interceptor called `UserRequestRetryVersionConflictsInterceptor` may be registered against the server. When this interceptor is enabled, requests may include an optional header requesting for the server to try to avoid returning an error due to concurrent writes. The server will then try to avoid version conflict errors by automatically retrying requests that would have otherwise failed due to a version conflict.

With this interceptor in place, the following header can be added to individual HTTP requests to instruct the server to avoid version conflict errors:

```http
X-Retry-On-Version-Conflict: retry; max-retries=100
```    

# Controlling Delete with Expunge size

Delete with expunge submits a job to delete and expunge the requested resources. This is done in batches. If the DELETE
?_expunge=true syntax is used to trigger the delete expunge, then the batch size will be determined by the value
of [Expunge Batch Size](/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/JpaStorageSettings.html#getExpungeBatchSize())
property.

# Disabling Non Resource DB History

This setting controls whether MdmLink and any other non-resource (ex: Patient is a FHIR resource, MdmLink is not) DB history is enabled.  Presently, this only affects the history for MDM links, but the functionality may be extended to other domains.

Clients may want to disable this setting for performance reasons as it populates a new set of database tables when enabled.

Setting this property explicitly to false disables the feature:  [Non Resource DB History](/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/JpaStorageSettings.html#isNonResourceDbHistoryEnabled())

# Prevent Conditional Updates to Invalidate Match Criteria

JPA Server prevents conditional updated to invalidate match criteria for first version of resources. 
This setting, disabled by default, allows to configure the same behaviour for later versions.

Setting this property explicitly to true enables the feature: [Prevent Conditional Updates Invalidating Match Criteria](/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/JpaStorageSettings.html#isPreventInvalidatingConditionalMatchCriteria())

