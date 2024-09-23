-- citus setup

-- create database homebase;
-- using database homebase;

--drop database cdr;


--CREATE TABLE "MIGRATIONS" ("installed_rank" INTEGER NOT NULL,"version" VARCHAR(50),"description" VARCHAR(200) NOT NULL,"type" VARCHAR(20) NOT NULL,"script" VARCHAR(1000) NOT NULL,"checksum" INTEGER,"installed_by" VARCHAR(100) NOT NULL,"installed_on" DATE NOT NULL,"execution_time" INTEGER NOT NULL,"success" boolean NOT NULL);

--CREATE UNIQUE INDEX MIGRATIONS_PK_INDEX ON "MIGRATIONS" ("installed_rank");

-- load '/Users/michael.buckley/git/shared2/cdr/cdr-persistence-livebundle/target/classes/ca/cdr/docs/database_persistence/postgres.sql';

--truncate table hfj_resource cascade;

ALTER table IF EXISTS hfj_resource DROP CONSTRAINT hfj_resource_pkey cascade;
ALTER table hfj_resource add CONSTRAINT hfj_resource_pkey PRIMARY KEY (res_id, partition_id);

ALTER table hfj_resource DROP CONSTRAINT idx_res_type_fhir_id;
ALTER table hfj_resource add CONSTRAINT idx_res_type_fhir_id UNIQUE (res_type, fhir_id, partition_id);

--DROP INDEX idx_res_date;
--ALTER table hfj_resource DROP constraint idx_res_date;
--ALTER table hfj_resource add CONSTRAINT idx_res_date UNIQUE (res_updated, partition_id);

--DROP INDEX idx_res_fhir_id;
--ALTER TABLE hfj_resource DROP constraint idx_res_fhir_id;
--ALTER table hfj_resource add CONSTRAINT idx_res_fhir_id UNIQUE (fhir_id, partition_id);

ALTER TABLE hfj_forced_id DROP CONSTRAINT hfj_forced_id_pkey;
ALTER TABLE hfj_forced_id ADD CONSTRAINT hfj_forced_id_pkey PRIMARY KEY (pid, partition_id);

ALTER TABLE hfj_idx_cmb_tok_nu DROP CONSTRAINT hfj_idx_cmb_tok_nu_pkey;
ALTER TABLE hfj_idx_cmb_tok_nu add CONSTRAINT hfj_idx_cmb_tok_nu_pkey PRIMARY KEY (pid, partition_id);

drop index  idx_idxcmbtoknu_res;
CREATE INDEX IF NOT EXISTS idx_idxcmbtoknu_res ON hfj_idx_cmb_tok_nu(res_id, partition_id);
   
ALTER TABLE hfj_idx_cmp_string_uniq DROP CONSTRAINT if exists hfj_idx_cmp_string_uniq_pkey;
ALTER table hfj_idx_cmp_string_uniq DROP CONSTRAINT if exists fk_idxcmpstruniq_res_id;
ALTER TABLE hfj_idx_cmp_string_uniq DROP CONSTRAINT idx_idxcmpstruniq_string;
drop INDEX IF EXISTS idx_idxcmpstruniq_resource;


ALTER table hfj_idx_cmp_string_uniq add CONSTRAINT hfj_idx_cmp_string_uniq_pkey PRIMARY KEY (pid, partition_id);
ALTER TABLE hfj_idx_cmp_string_uniq add CONSTRAINT idx_idxcmpstruniq_string UNIQUE (idx_string, partition_id);
CREATE INDEX  idx_idxcmpstruniq_resource ON hfj_idx_cmp_string_uniq(res_id, partition_id);

ALTER TABLE hfj_res_link DROP CONSTRAINT hfj_res_link_pkey;
ALTER table hfj_res_link add CONSTRAINT hfj_res_link_pkey PRIMARY KEY (pid, partition_id);

drop INDEX IF  EXISTS idx_rl_src;
CREATE INDEX IF NOT EXISTS idx_rl_src ON hfj_res_link(src_resource_id, partition_id);

ALTER TABLE hfj_res_param_present DROP CONSTRAINT hfj_res_param_present_pkey;
ALTER TABLE hfj_res_param_present add CONSTRAINT hfj_res_param_present_pkey PRIMARY KEY (pid, partition_id);

drop index IF  EXISTS idx_resparmpresent_resid;
CREATE INDEX IF NOT EXISTS idx_resparmpresent_resid ON hfj_res_param_present(res_id, partition_id);

ALTER TABLE hfj_res_tag DROP CONSTRAINT hfj_res_tag_pkey;
ALTER TABLE hfj_res_tag add CONSTRAINT hfj_res_tag_pkey PRIMARY KEY (pid, partition_id);

ALTER TABLE hfj_res_tag DROP CONSTRAINT idx_restag_tagid;
ALTER TABLE hfj_res_tag add   CONSTRAINT idx_restag_tagid UNIQUE (res_id, tag_id, partition_id);


ALTER table hfj_spidx_coords DROP CONSTRAINT hfj_spidx_coords_pkey;
ALTER table hfj_spidx_coords add CONSTRAINT hfj_spidx_coords_pkey PRIMARY KEY (sp_id, partition_id);

drop INDEX IF  EXISTS idx_sp_coords_resid;
CREATE INDEX IF NOT EXISTS idx_sp_coords_resid ON hfj_spidx_coords(res_id, partition_id);

ALTER table hfj_spidx_date DROP CONSTRAINT hfj_spidx_date_pkey;
ALTER table hfj_spidx_date add CONSTRAINT hfj_spidx_date_pkey PRIMARY KEY (sp_id, partition_id);

ALTER table hfj_spidx_number DROP CONSTRAINT hfj_spidx_number_pkey;
ALTER table hfj_spidx_number add CONSTRAINT hfj_spidx_number_pkey PRIMARY KEY (sp_id, partition_id);

ALTER TABLE hfj_spidx_quantity DROP CONSTRAINT hfj_spidx_quantity_pkey;
ALTER TABLE hfj_spidx_quantity add CONSTRAINT hfj_spidx_quantity_pkey PRIMARY KEY (sp_id, partition_id);

ALTER TABLE hfj_spidx_quantity_nrml DROP CONSTRAINT hfj_spidx_quantity_nrml_pkey;
ALTER TABLE hfj_spidx_quantity_nrml add CONSTRAINT hfj_spidx_quantity_nrml_pkey PRIMARY KEY (sp_id, partition_id);

ALTER TABLE hfj_spidx_token DROP CONSTRAINT hfj_spidx_token_pkey;
ALTER TABLE hfj_spidx_token add CONSTRAINT hfj_spidx_token_pkey PRIMARY KEY (sp_id, partition_id);

ALTER table hfj_spidx_uri DROP CONSTRAINT hfj_spidx_uri_pkey;   
ALTER table hfj_spidx_uri add CONSTRAINT hfj_spidx_uri_pkey PRIMARY KEY (sp_id, partition_id);

drop INDEX IF  EXISTS idx_sp_uri_coords;
CREATE INDEX IF NOT EXISTS idx_sp_uri_coords ON hfj_spidx_uri(res_id, partition_id);

ALTER TABLE hfj_spidx_string DROP CONSTRAINT hfj_spidx_string_pkey;
ALTER TABLE hfj_spidx_string add CONSTRAINT hfj_spidx_string_pkey PRIMARY KEY (sp_id, partition_id);

ALTER TABLE hfj_res_ver DROP CONSTRAINT hfj_res_ver_pkey cascade;
ALTER TABLE hfj_res_ver add CONSTRAINT hfj_res_ver_pkey PRIMARY KEY (pid, partition_id);

ALTER TABLE hfj_res_ver_prov DROP CONSTRAINT hfj_res_ver_prov_pkey;
ALTER TABLE hfj_res_ver_prov add CONSTRAINT hfj_res_ver_prov_pkey PRIMARY KEY (res_ver_pid, partition_id);

ALTER TABLE hfj_history_tag DROP CONSTRAINT hfj_history_tag_pkey;
ALTER TABLE hfj_history_tag add CONSTRAINT hfj_history_tag_pkey PRIMARY KEY (pid, partition_id);

--ALTER TABLE hfj_history_tag DROP CONSTRAINT if exists fktderym7awj6q8iq5c51xv4ndw;   

ALTER TABLE hfj_history_tag DROP CONSTRAINT idx_reshisttag_tagid;   
ALTER TABLE hfj_history_tag add CONSTRAINT idx_reshisttag_tagid UNIQUE (res_ver_pid, tag_id, partition_id);


ALTER TABLE hfj_history_tag DROP CONSTRAINT if exists fk_historytag_history;


drop index idx_resverprov_res_pid;
CREATE INDEX IF NOT EXISTS idx_resverprov_res_pid ON hfj_res_ver_prov(res_pid);


ALTER TABLE hfj_res_ver DROP CONSTRAINT idx_resver_id_ver;
ALTER TABLE hfj_res_ver add CONSTRAINT idx_resver_id_ver UNIQUE (res_id, res_ver, partition_id);

ALTER table mpi_link DROP CONSTRAINT mpi_link_pkey;
ALTER table mpi_link add CONSTRAINT mpi_link_pkey PRIMARY KEY (pid, partition_id);

ALTER table mpi_link DROP CONSTRAINT idx_empi_person_tgt;
ALTER table mpi_link add CONSTRAINT idx_empi_person_tgt UNIQUE (person_pid, target_pid, partition_id);


CREATE INDEX IF NOT EXISTS fk_empi_link_target ON mpi_link(target_pid, partition_id);

ALTER TABLE mpi_link_aud drop CONSTRAINT mpi_link_aud_pkey;
ALTER TABLE mpi_link_aud ADD CONSTRAINT mpi_link_aud_pkey PRIMARY KEY (rev, pid, partition_id);

-- status query
--select * from pg_dist_partition;
 
--    alter table if exists HFJ_HISTORY_TAG add constraint FKtderym7awj6q8iq5c51xv4ndw foreign key (TAG_ID) references HFJ_TAG_DEF;
--    alter table if exists HFJ_RES_TAG add constraint FKbfcjbaftmiwr3rxkwsy23vneo foreign key (TAG_ID) references HFJ_TAG_DEF;

alter table hfj_history_tag drop constraint if exists FKtderym7awj6q8iq5c51xv4ndw;
alter table HFJ_RES_TAG drop constraint if exists  FKbfcjbaftmiwr3rxkwsy23vneo;

-- ALTER TABLE hfj_res_tag ADD CONSTRAINT fk_restag_resource FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);

DO $$ BEGIN PERFORM create_reference_table('hfj_revinfo'); END; $$;
DO $$ BEGIN PERFORM create_reference_table('hfj_tag_def'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_resource', 'partition_id'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_res_ver', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_res_ver_prov', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_history_tag', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_res_tag', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_coords', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_idx_cmb_tok_nu', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_idx_cmp_string_uniq', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_string', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_number', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_quantity', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_quantity_nrml', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_token', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_date', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_res_link', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_spidx_uri', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('mpi_link', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('hfj_res_param_present', 'partition_id', colocate_with => 'hfj_resource'); END; $$;
DO $$ BEGIN PERFORM create_distributed_table('mpi_link_aud', 'partition_id', colocate_with => 'hfj_resource'); END; $$;

ALTER TABLE IF EXISTS hfj_res_ver ADD CONSTRAINT fk_resource_history_resource FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);

ALTER TABLE IF EXISTS hfj_res_ver_prov ADD CONSTRAINT fk_resverprov_res_pid FOREIGN KEY (res_pid, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_res_ver_prov ADD CONSTRAINT fk_resverprov_resver_pid FOREIGN KEY (res_ver_pid, partition_id) REFERENCES hfj_res_ver (pid, partition_id);

ALTER TABLE hfj_idx_cmb_tok_nu ADD CONSTRAINT fk_idxcmbtoknu_res_id FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER table hfj_idx_cmp_string_uniq ADD CONSTRAINT fk_idxcmpstruniq_res_id FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);

ALTER TABLE IF EXISTS hfj_res_link ADD CONSTRAINT fk_reslink_source FOREIGN KEY (src_resource_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
-- fixme new column here ALTER TABLE IF EXISTS hfj_res_link ADD CONSTRAINT fk_reslink_target FOREIGN KEY (target_resource_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
-- fixme citus doesn't suppoprt FK between shards ALTER TABLE IF EXISTS hfj_res_link ADD CONSTRAINT fk_reslink_target FOREIGN KEY (target_resource_id, target_res_partition_id) REFERENCES hfj_resource (res_id, partition_id);

ALTER TABLE IF EXISTS hfj_res_param_present ADD CONSTRAINT fk_resparmpres_resid FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_coords ADD CONSTRAINT fkc97mpk37okwu8qvtceg2nh9vn FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_date ADD CONSTRAINT fk_sp_date_res FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_number ADD CONSTRAINT fk_sp_number_res FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_quantity ADD CONSTRAINT fk_sp_quantity_res FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_quantity_nrml ADD CONSTRAINT fk_sp_quantitynm_res FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_string ADD CONSTRAINT fk_spidxstr_resource FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_token ADD CONSTRAINT fk_sp_token_res FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS hfj_spidx_uri ADD CONSTRAINT fkgxsreutymmfjuwdswv3y887do FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);

ALTER TABLE IF EXISTS hfj_res_tag ADD CONSTRAINT fk_restag_resource FOREIGN KEY (res_id, partition_id) REFERENCES hfj_resource (res_id, partition_id);
alter table HFJ_RES_TAG add constraint FKbfcjbaftmiwr3rxkwsy23vneo foreign key (TAG_ID) references HFJ_TAG_DEF;


ALTER TABLE hfj_history_tag ADD CONSTRAINT fk_historytag_history FOREIGN KEY (res_ver_pid, partition_id) REFERENCES hfj_res_ver (pid, partition_id);
alter table HFJ_HISTORY_TAG add constraint FKtderym7awj6q8iq5c51xv4ndw foreign key (TAG_ID) references HFJ_TAG_DEF;

ALTER TABLE IF EXISTS mpi_link ADD CONSTRAINT fk_empi_link_golden_resource FOREIGN KEY (golden_resource_pid, partition_id) REFERENCES hfj_resource (res_id, partition_id);
ALTER TABLE IF EXISTS mpi_link ADD CONSTRAINT fk_empi_link_person FOREIGN KEY (person_pid, partition_id) REFERENCES hfj_resource (res_id, partition_id);


ALTER TABLE IF EXISTS mpi_link ADD CONSTRAINT fk_empi_link_target FOREIGN KEY (target_pid, partition_id) REFERENCES hfj_resource (res_id, partition_id);
