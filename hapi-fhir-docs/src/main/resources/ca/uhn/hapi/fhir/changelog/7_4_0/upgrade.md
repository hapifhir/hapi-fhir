## Possible migration errors on SQL Server (MSSQL)

* This affects only clients running SQL Server (MSSQL) who have custom indexes on `HFJ_SPIDX` tables, which
  include `sp_name` or `res_type` columns.
* For those clients, migration of `sp_name` and `res_type` columns to nullable on `HFJ_SPIDX` tables may be completed with errors, as changing a column to nullable when a column is a
  part of an index can lead to errors on SQL Server (MSSQL).
* If client wants to use existing indexes and settings, these errors can be ignored. However, if client wants to enable both [Index Storage Optimized](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/entity/StorageSettings.html#setIndexStorageOptimized(boolean))
   and [Index Missing Fields](/hapi-fhir/apidocs/hapi-fhir-jpaserver-model/ca/uhn/fhir/jpa/model/entity/StorageSettings.html#getIndexMissingFields()) settings, manual steps are required to change `sp_name` and `res_type` nullability.

To update columns to nullable in such a scenario, execute steps below:

1. Indexes that include `sp_name` or `res_type` columns should be dropped:

```sql
DROP INDEX IDX_SP_TOKEN_REST_TYPE_SP_NAME ON HFJ_SPIDX_TOKEN;
```

2.  The nullability of `sp_name` and `res_type` columns should be updated:

```sql
ALTER TABLE HFJ_SPIDX_TOKEN ALTER COLUMN RES_TYPE varchar(100) NULL;
ALTER TABLE HFJ_SPIDX_TOKEN ALTER COLUMN SP_NAME varchar(100) NULL;
```
3. Additionally, the following index may need to be added to improve the search performance:
```sql
CREATE INDEX IDX_SP_TOKEN_MISSING_OPTIMIZED ON HFJ_SPIDX_TOKEN (HASH_IDENTITY, SP_MISSING, RES_ID, PARTITION_ID);
```
