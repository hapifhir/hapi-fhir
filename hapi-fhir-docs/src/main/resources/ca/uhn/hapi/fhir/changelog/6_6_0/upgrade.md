
This release has breaking changes.
* The Resource $validate operation no longer returns Precondition Failed 412 when a resource fails validation.  It now returns 200 irrespective of the validation outcome as required by the [FHIR Specification for the Resource $validate operation](https://www.hl7.org/fhir/R4/resource-operation-validate.html).
  
* This release changes database indexing for string and uri SearchParameters. The database migration may take several minutes.  These changes will be applied automatically on first startup. To avoid this delay on first startup, run the migration manually.

