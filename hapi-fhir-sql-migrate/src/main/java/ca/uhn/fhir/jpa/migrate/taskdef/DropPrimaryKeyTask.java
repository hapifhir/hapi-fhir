/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.SQLException;

// LUKETODO:  make sure error handling is clear about what went wrong
public class DropPrimaryKeyTask extends BaseTableTask {
	private static final Logger ourLog = LoggerFactory.getLogger(DropPrimaryKeyTask.class);

	public DropPrimaryKeyTask(String theProductVersion, String theSchemaVersion, String theTableName) {
		super(theProductVersion, theSchemaVersion);
		setTableName(theTableName);
	}

	@Nonnull
	private String generateSql() {
		ourLog.info("6145: DropPrimaryKeyTask.generateSql()");
		final ResultSetExtractor<String> resultSetExtractor = rs -> {
			// LUKETODO:  error handling
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;
		};

		final String primaryKeyName = executeSqlWithResult(
				generatePrimaryKeyIndexNameSql(), resultSetExtractor, getTableNameWithDatabaseExpectedCase());
		ourLog.info("6145: primaryKeyName: [{}]", primaryKeyName);
		return generateDropPrimaryKeySql(primaryKeyName);
	}

	private String getTableNameWithDatabaseExpectedCase() {
		if (DriverTypeEnum.ORACLE_12C == getDriverType()) {
			return getTableName().toUpperCase();
		}

		return getTableName().toLowerCase();
	}

	@Override
	protected void doExecute() throws SQLException {
		logInfo(ourLog, "Going to DROP the PRIMARY KEY on table {}", getTableName());

		//		// LUKETODO:  error handling?
		executeSql(getTableName(), generateSql());
	}

	private String generateDropPrimaryKeySql(String thePrimaryKeyName) {
		switch (getDriverType()) {
			case MARIADB_10_1:
			case DERBY_EMBEDDED:
			case H2_EMBEDDED:
				@Language("SQL")
				final String sqlH2 = "ALTER TABLE %s DROP PRIMARY KEY";
				return String.format(sqlH2, getTableName());
			case POSTGRES_9_4:
			case ORACLE_12C:
			case MSSQL_2012:
			case MYSQL_5_7:
				@Language("SQL")
				final String sql = "ALTER TABLE %s DROP CONSTRAINT %s";
				return String.format(sql, getTableName(), thePrimaryKeyName);
			default:
				throw new IllegalStateException(Msg.code(59));
		}
	}

	@Language("SQL")
	private String generatePrimaryKeyIndexNameSql() {
		switch (getDriverType()) {
			case MYSQL_5_7:
			case MARIADB_10_1:
			case DERBY_EMBEDDED:
			case COCKROACHDB_21_1:
			case H2_EMBEDDED:
				return "SELECT index_name " + "FROM information_schema.indexes "
						+ "WHERE table_schema = 'PUBLIC' "
						+ "AND index_type_name = 'PRIMARY KEY' "
						+ "AND table_name = ?";
			case POSTGRES_9_4:
				return "SELECT constraint_name " + "FROM information_schema.table_constraints "
						+ "WHERE table_schema = 'public' "
						+ "AND constraint_type = 'PRIMARY KEY' "
						+ "AND table_name = ?";
			case ORACLE_12C:
				return "SELECT constraint_name " + "FROM user_constraints "
						+ "WHERE constraint_type = 'P' "
						+ "AND table_name = ?";
			case MSSQL_2012:
				return "SELECT tc.constraint_name " + "FROM information_schema.table_constraints tc "
						+ "JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name "
						+ "WHERE tc.constraint_type = 'PRIMARY KEY' "
						+ "AND  tc.table_name = ?";
			default:
				throw new IllegalStateException(Msg.code(59));
		}
	}

	// LUKETODO:  in order to drop a primary key, we need to introspect

	// LUKETODO:  try CASCADE for all DB products
	/*
		postgres:
		ALTER TABLE table_name
	DROP CONSTRAINT constraint_name;

	mssql:
	ALTER TABLE Production.TransactionHistoryArchive
	DROP CONSTRAINT PK_TransactionHistoryArchive_TransactionID;

	oracle:
	ALTER TABLE test DROP CONSTRAINT xpk_test CASCADE;

	mysql:

	ALTER TABLE user_customer_permission DROP PRIMARY KEY;
		 */

	// table defs:
	/*
		postgres:

		create table if not exists public.hfj_res_search_url
	(
		res_search_url varchar(768) not null
			primary key,
		created_time   timestamp(6) not null,
		res_id         bigint       not null
			constraint fk_res_search_url_resource
				references public.hfj_resource
	);

	alter table public.hfj_res_search_url
		owner to cdr;

	create index if not exists idx_ressearchurl_res
		on public.hfj_res_search_url (res_id);

	create index if not exists idx_ressearchurl_time
		on public.hfj_res_search_url (created_time);


		create table if not exists public.cdr_metric_timer
	(
		intervl        integer          not null,
		start_time     timestamp(6)     not null,
		timer_type     integer          not null,
		is_collapsed   boolean          not null,
		count_val      bigint,
		rate_15_min    double precision not null,
		first_since_rs boolean          not null,
		rate_5_min     double precision not null,
		latency_max    integer          not null,
		latency_mean   integer          not null,
		latency_min    integer          not null,
		modproc_pid    bigint           not null
			constraint fk_mettimer_modp
				references public.cdr_module_process,
		rate_1_min     double precision not null,
		primary key (intervl, modproc_pid, start_time, timer_type)
	);

	alter table public.cdr_metric_timer
		owner to cdr;

	create index if not exists idx_timer_modcollintstart
		on public.cdr_metric_timer (modproc_pid, timer_type, intervl, start_time);

	create index if not exists idx_timer_modcollstart_v2
		on public.cdr_metric_timer (start_time, modproc_pid, is_collapsed);



	h2:

	create table PUBLIC.HFJ_RES_SEARCH_URL
	(
		RES_SEARCH_URL CHARACTER VARYING(768) not null
			primary key,
		CREATED_TIME   TIMESTAMP              not null,
		RES_ID         BIGINT                 not null,
		PARTITION_ID   INTEGER                not null,
		PARTITION_DATE DATE,
		constraint FK_RES_SEARCH_URL_RESOURCE
			foreign key (RES_ID) references PUBLIC.HFJ_RESOURCE
	);

	create index PUBLIC.IDX_RESSEARCHURL_RES
		on PUBLIC.HFJ_RES_SEARCH_URL (RES_ID);

	create index PUBLIC.IDX_RESSEARCHURL_TIME
		on PUBLIC.HFJ_RES_SEARCH_URL (CREATED_TIME);

	create unique index PUBLIC.IDX_SEARCH_URL_PARTITION_ID
		on PUBLIC.HFJ_RES_SEARCH_URL (RES_SEARCH_URL, PARTITION_ID);


	create table PUBLIC.CDR_METRIC_TIMER
	(
		INTERVL        INTEGER          not null,
		START_TIME     TIMESTAMP        not null,
		TIMER_TYPE     INTEGER          not null,
		IS_COLLAPSED   BOOLEAN          not null,
		COUNT_VAL      BIGINT,
		RATE_15_MIN    DOUBLE PRECISION not null,
		FIRST_SINCE_RS BOOLEAN          not null,
		RATE_5_MIN     DOUBLE PRECISION not null,
		LATENCY_MAX    INTEGER          not null,
		LATENCY_MEAN   INTEGER          not null,
		LATENCY_MIN    INTEGER          not null,
		MODPROC_PID    BIGINT           not null,
		RATE_1_MIN     DOUBLE PRECISION not null,
		primary key (INTERVL, MODPROC_PID, START_TIME, TIMER_TYPE),
		constraint FK_METTIMER_MODP
			foreign key (MODPROC_PID) references PUBLIC.CDR_MODULE_PROCESS
	);

	create index PUBLIC.IDX_TIMER_MODCOLLINTSTART
		on PUBLIC.CDR_METRIC_TIMER (MODPROC_PID, TIMER_TYPE, INTERVL, START_TIME);

	create index PUBLIC.IDX_TIMER_MODCOLLSTART_V2
		on PUBLIC.CDR_METRIC_TIMER (START_TIME, MODPROC_PID, IS_COLLAPSED);



		 */

	// find PK index name:
	/*
		h2:
		select index_name
	from information_schema.indexes
	where table_schema = 'PUBLIC' and
		table_name = 'HFJ_RES_SEARCH_URL' and
		index_type_name = 'PRIMARY KEY';

		postgres:

	select constraint_name
	from information_schema.table_constraints
	where table_schema = 'public'
	and table_name = 'hfj_res_search_url'
	and constraint_type = 'PRIMARY KEY';

	mmsql:

	SELECT name
	FROM sys.key_constraints
	WHERE type = 'PK' AND OBJECT_NAME(parent_object_id) = N'TransactionHistoryArchive';
	GO

	oracle:

	SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE
	FROM USER_CONSTRAINTS
	WHERE TABLE_NAME = 'your_table_name';


	mysql:

	ALTER TABLE user_customer_permission DROP PRIMARY KEY;

		 */
}
