This release contains a large breaking change for authors of interceptors. Internally, HAPI-FHIR has swapped from using `javax.*` to `jakarta.*` packages. Please see [the migration guide](/hapi-fhir/docs/interceptors/jakarta_upgrade.html) for more information.  Without manual intervention, the majority of interceptors will fail at runtime unless they are upgraded.

## Possible New Indexes on PostgresSQL

* This affects only clients running PostgreSQL who have a locale/collation that is NOT 'C'
* For those clients, the migration will detect this condition and add new indexes to:
    * hfj_spidx_string
    * hfj_spidx_uri
* This is meant to address performance issues for these clients on GET queries whose resulting SQL uses "LIKE" clauses

These are the new indexes that will be created:

```sql
CREATE INDEX idx_sp_string_hash_nrm_pattern_ops ON public.hfj_spidx_string USING btree (hash_norm_prefix, sp_value_normalized varchar_pattern_ops, res_id, partition_id);
```
```sql
CREATE UNIQUE INDEX idx_sp_uri_hash_identity_pattern_ops ON public.hfj_spidx_uri USING btree (hash_identity, sp_uri varchar_pattern_ops, res_id, partition_id);
```
