# Performance

This page contains information for performance optimization. If you are planning a production deployment, you should consider the options discussed here as they may have significant impacts on your ability to scale.

# History Counting

The FHIR history operation allows clients to see a change history for a resource, across all resources of a given type, or even across all resources on a server. This operation includes a total count (in `Bundle.total`) that can be very expensive to calculate on large databases with many resources.

As a result, a setting on the `DaoConfig` object has been added called **History Count Mode**. This setting has 3 possible options:

* COUNT_CACHED. This is the new default: A loading cache will be used for history counts without any dates specified, meaning that counts are stored in RAM for up to one minute, and the loading cache blocks all but one client thread per JVM from actually performing the count. This effectively throttles access to the database. History operation invocations that include a `_since` or `_to` parameter will never have a count included in the results.
  
* COUNT_ACCURATE: This option always uses a fresh count statement for all history invocations. This means that the count total in the History bundle is guaranteed to be accurate every time. Note that this means that users may trigger a large amount of potentially expensive database operations by performing a large number of history operations. Do not use this option in situations where you have untrusted users accessing your server.
  
* COUNT_DISABLED: This setting avoids the count query entirely, saving time and avoiding any risk of expensive count queries at the expense of not including any total in the response.


# Bulk Loading

On servers where a large amount of data will be ingested, the following considerations may be helpful:

* Optimize your database thread pool count and HTTP client thread count: Every environment will have a different optimal setting for the number of concurrent writes that are permitted, and the maximum number of database connections allowed. 

* Disable deletes: If the JPA server is configured to have the FHIR delete operation disabled, it is able to skip some resource reference deletion checks during resource creation, which can have a measurable improvement to performance over large datasets. 

# Disabling :text Indexing

On servers storing large numbers of Codings and CodeableConcepts (as well as any other token SearchParameter target where the `:text` modifier is supported), the indexes required to support the `:text` modifier can consume a large amount of index space, and cause a measurable impact on write times.

This modifier can be disabled globally by using the ModelConfig#setSuppressStringIndexingInTokens setting.

It can also be disabled at a more granular level (or selectively re-enabled if it disabled globally) by using an extension on individual SearchParameter resources. For example, the following SearchParameter disables text indexing on the Observation:code parameter:

```json
{
  "resourceType": "SearchParameter",
  "id": "observation-code",
  "extension": [ {
    "url": "http://hapifhir.io/fhir/StructureDefinition/searchparameter-token-suppress-text-index",
    "valueBoolean": true
  } ],
  "status": "active",
  "code": "code",
  "base": [ "Observation" ],
  "type": "token",
  "expression": "Observation.code"
}
```

# Disable Upsert Existence Check

If you are using an *Update with Client Assigned ID* (aka an Upsert), the server will perform a SQL Select in order to determine whether the ID already exists, and then proceed to create a new record if no data matches the existing row.

If you are sure that the row does not already exist, you can add the following header to your request in order to avoid this check.

```http
X-Upsert-Extistence-Check: disabled
```

This should improve write performance, so this header can be useful when large amounts of data will be created using client assigned IDs in a controlled fashion.

If this setting is used and a resource already exists with a given client-assigned ID, a database constraint error will prevent any duplicate records from being created, and the operation will fail.
