
## Contained Resources

When parsing or serializing resources, contained resources will no longer be given an ID starting with the `#` character, although this character is still used in references to that resource. For example, if a _Patient_ has a contained _Practitioner_ resource, the practitioner will have an ID such as `123`, and the `Patient.generalPractitioner` reference will be `#123`. In previous versions of HAPI FHIR, both of these values would be set to `#123` which was confusing and no longer validates correctly.

See [Contained Resources](/hapi-fhir/docs/model/references.html#contained) for a more detailed example.

## The `SP_UPDATED` column in `HFJ_SPIDX_*` tables

The `SP_UPDATED` column is no longer used in the `HFJ_SPIDX_*` tables.
Existing data in `SP_UPDATED` column can be safely removed manually after upgrading to version 8.2 to free up database storage space.
