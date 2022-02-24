This release has some breaking changes that are worth knowing about.
* Hibernate Search mappings for Terminology entities have been upgraded, which causes a reindex of ValueSet resources to be required.

## Hibernate Search Mappings Upgrade

Some freetext mappings (definitions about how resources are freetext indexed) were changed for Terminology resources. 
This change requires a full reindexing for any Smile CDR installations which make use of the following features:

* Fulltext/Content search
* Terminology Expansion

To reindex all resources call:

```http
POST /$reindex
Content-Type: application/fhir+json

{
  "resourceType": "Parameters",
  "parameter": [ {
    "name": "everything",
    "valueBoolean": "true"
  }, {
    "name": "batchSize",
    "valueDecimal": 1000
  } ]
}
```
