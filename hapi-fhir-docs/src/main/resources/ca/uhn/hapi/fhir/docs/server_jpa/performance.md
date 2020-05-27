# Performance

This page contains information for performance optimization.

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
