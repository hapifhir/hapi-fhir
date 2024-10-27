
-- we can't use covering index until the autovacuum runs for those rows, which kills index performance
ALTER TABLE hfj_resource SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_forced_id SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_res_link SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_coords SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_date SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_number SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_quantity SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_quantity_nrml SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_string SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_token SET (autovacuum_vacuum_scale_factor = 0.01);
ALTER TABLE hfj_spidx_uri SET (autovacuum_vacuum_scale_factor = 0.01);

-- PG by default tracks the most common 100 values.  But our hashes cover 100s of SPs and need greater depth.
-- Set stats depth to the max for hash_value columns, and 1000 for hash_identity (one per SP).
alter table hfj_res_link alter column src_path set statistics 10000;
alter table hfj_res_link alter column target_resource_id set statistics 10000;
alter table hfj_res_link alter column src_resource_id set statistics 10000;
alter table hfj_spidx_coords alter column hash_identity set statistics 1000;
alter table hfj_spidx_date alter column hash_identity set statistics 1000;
alter table hfj_spidx_number alter column hash_identity set statistics 1000;
alter table hfj_spidx_quantity alter column hash_identity set statistics 1000;
alter table hfj_spidx_quantity alter column hash_identity_and_units set statistics 10000;
alter table hfj_spidx_quantity alter column hash_identity_sys_units set statistics 10000;
alter table hfj_spidx_quantity_nrml alter column hash_identity set statistics 1000;
alter table hfj_spidx_quantity_nrml alter column hash_identity_and_units set statistics 10000;
alter table hfj_spidx_quantity_nrml alter column hash_identity_sys_units set statistics 10000;
alter table hfj_spidx_string alter column hash_identity set statistics 1000;
alter table hfj_spidx_string alter column hash_exact set statistics 10000;
alter table hfj_spidx_string alter column hash_norm_prefix set statistics 10000;
alter table hfj_spidx_token alter column hash_identity set statistics 1000;
alter table hfj_spidx_token alter column hash_sys set statistics 10000;
alter table hfj_spidx_token alter column hash_sys_and_value set statistics 10000;
alter table hfj_spidx_token alter column hash_value set statistics 10000;
alter table hfj_spidx_uri alter column hash_identity set statistics 1000;
alter table hfj_spidx_uri alter column hash_uri set statistics 10000;
