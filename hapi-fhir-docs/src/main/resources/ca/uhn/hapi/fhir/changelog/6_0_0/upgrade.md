This release has some breaking changes that are worth knowing about.
* Hibernate Search mappings for Terminology entities have been upgraded, which causes a reindex of ValueSet resources to be required.

## Hibernate Search Mappings Upgrade

Some freetext mappings (definitions about how resources are freetext indexed) were changed for Terminology resources. 
This change requires a reindex of ValueSet resource for any Smile CDR installations which make use of the following features:

* Fulltext/Content search
* Terminology Expansion

Expansion can be requested as follows:

`POST /$reindex`

`Content-Type: application/fhir+json`

```json
{
   "resourceType": "Parameters",
   "parameter": [ {
      "name": "url",
      "valueString": "ValueSet?"
   }, {
      "name": "batchSize",
      "valueDecimal": 1000
   } ]
}
```
