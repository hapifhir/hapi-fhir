# MDM Expansion

Once you have MDM enabled, and you have many linked resources, it can be useful to search across all linked resources. Let's say you have the following MDM links in your database:
```bash
Patient/1 --> Patient/3
Patient/2 --> Patient/3
```
This indicates that both Patient/1 and Patient/2 are MDM-matched to the same golden resource (Patient/3).
What if you want to get all observations from Patient/1, but also include any observations from all of their linked resources. You could do this by first querying the [$mdm-query-links](/docs/server_jpa_mdm/mdm_operations.html) endpoint, and then making a subsequent call like the following
```http request
GET http://example.com:8000/Observation?subject=Patient/1,Patient/2,Patient/3
```

But HAPI-FHIR allows a shorthand for this, by means of a Search Parameter qualifier, as follows: 
```http request
GET http://example.com:8000/Observation?subject:mdm=Patient/1
```

This `:mdm` parameter qualifier instructs an interceptor in HAPI fhir to expand the set of resources included in the search by their MDM-matched resources. The two above HTTP requests will return the same result. 

This behaviour is also supported on the `$everything` operation, via a slightly different mechanism. If you call the operation with `_mdm=true`, then MDM expansion will occur on the base Patient instance. For example: 

```http request
GET http://example.com:8000/Patient/1/$everything?_mdm=true
```

This will first lookup all Patients linked to Patient/1, and then  perform an `$everything` including all resources for these patients.

<div class="helpWarningCalloutBox">
One important caveat is that chaining is currently not supported when using this prefix.
</div>

## Enabling MDM Expansion

On top of needing to instantiate an MDM module, you must enable this feature in
the [StorageSettings](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/entity/StorageSettings.html) bean, using
the [Allow MDM Expansion](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/entity/StorageSettings.html#setAllowMdmExpansion(boolean))
property.

<div class="helpWarningCalloutBox">
It is important to note that enabling this functionality can lead to incorrect data being returned by a request, if your MDM links are incorrect. Use with caution.
</div>

