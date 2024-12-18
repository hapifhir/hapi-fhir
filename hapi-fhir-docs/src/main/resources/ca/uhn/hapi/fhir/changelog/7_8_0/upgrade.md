## Resource Provenance

The JPA server stores values for the field `Resource.meta.source` in dedicated columns in its database so that they can be indexes and searched for as needed, using the `_source` Search Parameter.

Prior to HAPI FHIR 6.8.0 (and Smile CDR 2023.08.R01), these values were stored in a dedicated table called `HFJ_RES_VER_PROV`. Beginning in HAPI FHIR 6.8.0 (Smile CDR 2023.08.R01), two new columns were added to the `HFJ_RES_VER`
table which store the same data and make it available for searches.

As of HAPI FHIR 8.0.0, the legacy table is no longer searched by default. If you do not have Resource.meta.source data stored in HAPI FHIR that was last created/updated prior to version 6.8.0, this change will not affect you and no action needs to be taken.

If you do have such data, you should follow the following steps:

* Enable the JpaStorageSettings setting `setAccessMetaSourceInformationFromProvenanceTable(true)` to configure the server to continue using the legacy table.

* Perform a server resource reindex by invoking the [$reindex Operation (server)](https://smilecdr.com/docs/fhir_repository/search_parameter_reindexing.html#reindex-server) with the `optimizeStorage` parameter set to `ALL_VERSIONS`. 

* When this reindex operation has successfully completed, the setting above can be disabled. Disabling this setting avoids an extra database round-trip when loading data, so this change will have a positive performance impact on your server.

## Device membership in Patient Compartment

As of 8.0.0, versions of FHIR below R5 now consider the `Device` resource's `patient` Search Parameter to be in the Patient Compartment. The following features are affected:

- Patient Search with `_revInclude=*`
- Patient instance-level `$everything` operation
- Patient type-level `$everything` operation
- Automatic Search Narrowing
- Bulk Export

Previously, there were various shims in the code that permitted similar behaviour in these features. Those shims have been removed. The only remaining component is [Advanced Compartment Authorization](/hapi-fhir/docs/security/authorization_interceptor.html#advanced-compartment-authorization), which can still be used 
to add other Search Parameters into a given compartment.

## Fulltext Search with _lastUpdated Filter

Fulltext searches have been updated to support `_lastUpdated` search parameter. If you are using Advanced Hibernate Search indexing and wish to use the `_lastUpdated` search parameetr with this feature, a full reindex of your repository is required.
