Users of the `Resource.meta.source` field, as well as users of the `_source` parameter should perform a global $reindex after upgrading to this version of HAPI FHIR with the following parameters:

```url
[base]/$reindex?reindexSearchParameters=false&optimizeStorage=ALL_VERSIONS
```

The previous mechanism for storing and indexing these parameters is inefficient and will be replaced in a future release of HAPI FHIR. Performing this reindex operation ensures that existing data will continue to be searchable.
