# Upgrade Notes

## Major Database Change Breaking Zero-Downtime for some Sql Server users
This release introduces a database migration that breaks zero-downtime for a subset of users matching all the following
criteria:
* using SQL Server
* the first install of HAPI-FHIR was 7.4.0 or later (i.e. 7.4, 7.6, or any 8.x release in 2025).
* users who use or wish to use case-sensitive FHIR IDs (client-assigned resource IDs that differ only in case)

### The Issue
Previously, when using client-assigned FHIR resource IDs that differ only in case, for example:
```
PUT /Patient/PatientA
PUT /Patient/patientA
```
would either result in two versions of the same resource, or have the second request would fail.
However, by the FHIR spec, this should create 2 distinct resources.
This behaviour has now been corrected as part of [this HAPI-FHIR issue](https://github.com/hapifhir/hapi-fhir/issues/7430).

**Fixing this requires a database migration that is not compatible with zero-downtime upgrades.**

### Is your database impacted?
If you meet the criteria above, and want to determine if your database is affected, run the following diagnostic SQL
query to check:
```sql
SELECT CASE CHARINDEX('_CI_', COLLATION_NAME) WHEN 0 THEN 0 ELSE 1 END 
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = SCHEMA_NAME()
AND TABLE_NAME = 'HFJ_RESOURCE'
AND COLUMN_NAME = 'FHIR_ID'
```
Users who see:
* a `1` are affected by this case-insensitive collation
* a `0` are unaffected

### Action required to skip migration and enforce zero-downtime
If the case-sensitive FHIR ID behaviour is not required for your use case, and you wish to preserve zero-downtime upgrades, you may bypass this migration by using the following `hapi-fhir-cli` command:
```bin/hapi-fhir-cli migrate-database ...<args>... --skip-versions 8_8_0.20251208.10,8_8_0.20251208.20,8_8_0.20251208.30,8_8_0.20251208.40,8_8_0.20251208.50```
See the [migrate database docs](/hapi-fhir/docs/server_jpa/upgrading.html) for more information.
