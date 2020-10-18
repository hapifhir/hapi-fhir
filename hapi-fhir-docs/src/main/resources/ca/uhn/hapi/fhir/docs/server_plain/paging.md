# Paging Responses

The **Search** and **History** operations both return a bundle which contain zero or more resources. FHIR RESTful servers may optionally support paging responses, meaning that (for example) if a search returns 500 resources, the server can return a bundle containing only the first 20 and a link which will return the next 20, etc.

By default, RESTful servers will not page, but will rather return all resources immediately in a single bundle. However, you can take advantage of built-in paging functionality to automatically add paging links to generated Bundle resources, and to handle these links by requesting further data.
 
There are two complementary parts to the HAPI FHIR server paging support: paging providers, and bundle providers.

## Paging Providers

To support paging, a server must have an [IPagingProvider](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/IPagingProvider.html) implementation set. The paging provider is used to store resource return lists between incoming calls by clients.

A paging provider provides two key methods:

* `String storeResultList(RequestDetails, IBundleProvider)`, which takes a bundle provider (see below) and stores it for later retrieval. This might be by simply keeping it in memory, but it might also store it on disk, in a database, etc. This method must return a textual ID which can be used to retrieve this list later.

* `retrieveResultList(RequestDetails, String)`</code>, which takes an ID obtained by a previous call to `storeResultList` and returns the corresponding	result list.

Note that the IPagingProvider is intended to be simple and implementable and you are encouraged to provide your own implementations.

The following example shows a server implementation with paging	support.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PagingServer.java|provider}}
```

HAPI FHIR contains couple of implementations for `IPagingProvider`.

### DatabaseBackedPagingProvider

When using `DatabaseBackedPagingProvider` HAPI FHIR searches may be done asynchronously. This means that
the result is also cached to the database and the client may base the cached search result set.

### FifoMemoryPagingProvider

When using `FifoMemoryPagingProvider` HAPI FHIR server search is persisted on the server memory and when
pages are fetched the server returns the results from the cached memory (unless the cache overflowed and the old result
set is no longer available).

# Bundle Providers

If a server supports a paging provider, a further optimization is to also use a bundle provider. A bundle provider simply takes the place of the `List<IBaseResource>` return type in your provider methods. In other words, instead of returning *List<IBaseResource>*, your search method will return [IBundleProvider](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/api/server/IBundleProvider.html).

When using a bundle provider however, the server will only request small sublists of resources as they are actually being returned. This allows servers to optimize by not loading all resources into memory until they are actually needed.

One implementation of a bundle provider is shown below. This provider example works by only keeping the resource IDs in memory, but there are other possible implementation strategies that would work as well.

Note that the IBundleProvider is intended to be simple and implementable and you are encouraged to provide your own implementations.

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/PagingPatientProvider.java|provider}}
```

## Using Named Pages

By default, the paging system uses parameters that are embedded into the page links for the start index and the page size. This is useful for servers that can retrieve arbitrary offsets within a search result. For example, if a given search can easily retrieve "items 5-10 from the given search", then the mechanism above works well.

Another option is to use "named pages", meaning that each page is simply assigned an ID by the server, and the next/previous	page is requested using this ID.

In order to support named pages, the IPagingProvider must implement the `retrieveResultList(RequestDetails theRequestDetails, String theSearchId, String thePageId)` method.

Then, individual search/history methods may return a [BundleProviderWithNamedPages](/hapi-fhir/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/BundleProviderWithNamedPages.html) or simply implement the `getPageId()` method on their own IBundleProvider implementation.

