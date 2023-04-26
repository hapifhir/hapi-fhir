
This release has breaking changes.
* The Resource $validate operation no longer returns Precondition Failed 412 when a resource fails validation.  It now returns 200 irrespective of the validation outcome as required by the [FHIR Specification for the Resource $validate operation](https://www.hl7.org/fhir/R4/resource-operation-validate.html).
  
* This release changes database indexing for string, uri, and reference SearchParameters. The database migration may take several minutes.  These changes will be applied automatically on first startup. To avoid this delay on first startup, run the migration manually.

Bulk export behaviour is changing in this release such that Binary resources created as part of the response will now be created in the partition that the bulk export was requested rather than in the DEFAULT partition as was being done previously.

Bulk import behaviour is changing in this release such that data imported as part of the request will now create resources in the partition that the bulk import was requested rather than in the DEFAULT partition as was being done previously.

The default statistics depth for many tables has changed for Postgres.
This improves the performance of many queries.
Users of Postgres may wish to ANALYZE the HFJ_SPIDX_* indexing tables to see these improvements immediately.
```
analyze hfj_spidx_coords;
analyze hfj_spidx_date;
analyze hfj_spidx_number;
analyze hfj_spidx_quantity;
analyze hfj_spidx_quantity_nrml;
analyze hfj_spidx_string;
analyze hfj_spidx_token;
analyze hfj_spidx_uri;
analyze hfj_res_link;
```

