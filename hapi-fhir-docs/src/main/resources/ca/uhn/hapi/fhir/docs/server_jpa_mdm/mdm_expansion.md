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


<div class="helpWarningCalloutBox">
One important caveat is that chaining is currently not supported when using this prefix.
</div>

## Enabling MDM Expansion

On top of needing to instantiate an MDM module, you must enable this feature in
the [DaoConfig](/hapi-fhir/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/DaoConfig.html) bean, using
the [Allow MDM Expansion](/hapi-fhir/apidocs/hapi-fhir-storage/ca/uhn/fhir/jpa/api/config/DaoConfig.html#setAllowMdmExpansion(boolean))
property.

<div class="helpWarningCalloutBox">
It is important to note that enabling this functionality can lead to incorrect data being returned by a request, if your MDM links are incorrect. Use with caution.
</div>

