## Breaking Changes

* Support for Java 11 has been dropped. A minimum of Java 17 is now required for HAPI FHIR. Java 21 is also supported.

## The `SP_UPDATED` column in `HFJ_SPIDX_*` tables
The `SP_UPDATED` column is no longer used in the `HFJ_SPIDX_*` tables.
Existing data in `SP_UPDATED` column can be safely removed manually after upgrading to version 8.2 to free up database storage space.

