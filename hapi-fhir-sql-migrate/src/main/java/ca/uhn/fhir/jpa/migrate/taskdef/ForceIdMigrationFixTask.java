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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

/**
 * Fix for bad version of {@link ForceIdMigrationCopyTask}
 * The earlier migration had used at cast to char instead of varchar, which is space-padded on Oracle.
 * This migration includes the copy action, but also adds a trim() call to fixup the bad server-assigned ids.
 */
public class ForceIdMigrationFixTask extends BaseTask {
	private static final Logger ourLog = LoggerFactory.getLogger(ForceIdMigrationFixTask.class);

	public ForceIdMigrationFixTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Override
	public void validate() {
		// no-op
	}

	@Override
	protected void doExecute() throws SQLException {
		logInfo(ourLog, "Starting: migrate fhir_id from hfj_forced_id to hfj_resource.fhir_id");

		JdbcTemplate jdbcTemplate = newJdbcTemplate();

		Pair<Long, Long> range = jdbcTemplate.queryForObject(
				"select min(RES_ID), max(RES_ID) from HFJ_RESOURCE",
				(rs, rowNum) -> Pair.of(rs.getLong(1), rs.getLong(2)));

		if (range == null || range.getLeft() == null) {
			logInfo(ourLog, "HFJ_RESOURCE is empty.  No work to do.");
			return;
		}

		// run update in batches.
		int rowsPerBlock = 50; // hfj_resource has roughly 50 rows per 8k block.
		int batchSize = rowsPerBlock * 2000; // a few thousand IOPS gives a batch size around a second.
		ourLog.info(
				"About to migrate ids from {} to {} in batches of size {}",
				range.getLeft(),
				range.getRight(),
				batchSize);
		for (long batchStart = range.getLeft(); batchStart <= range.getRight(); batchStart = batchStart + batchSize) {
			long batchEnd = batchStart + batchSize;
			ourLog.info("Migrating client-assigned ids for pids: {}-{}", batchStart, batchEnd);

			/*
			We have several cases.  Two require no action:
			1. client-assigned id, with correct value in fhir_id and row in hfj_forced_id
			2. server-assigned id, with correct value in fhir_id, no row in hfj_forced_id
			And three require action:
			3. client-assigned id, no value in fhir_id, but row in hfj_forced_id
			4. server-assigned id, no value in fhir_id, and row in hfj_forced_id
			5. bad migration - server-assigned id, with wrong space-padded value in fhir_id, no row in hfj_forced_id
			 */

			executeSql(
					"hfj_resource",
					"update hfj_resource " +
							// coalesce is varargs and chooses the first non-null value, like ||
							" set fhir_id = coalesce( "
							+
							// case 5.
							" trim(fhir_id), "
							+
							// case 3
							" (select f.forced_id from hfj_forced_id f where f.resource_pid = res_id), "
							+
							// case 4 - use pid as fhir_id
							"   cast(res_id as varchar(64)) "
							+ "  ) "
							+
							// avoid useless updates on engines that don't check
							// skip case 1, 2.  Only check 3,4,5
							getWhereClauseByDBType()
							+
							// chunk range.
							" and res_id >= ? and res_id < ?",
					batchStart,
					batchEnd);
		}
	}

	private String getWhereClauseByDBType() {
		switch (getDriverType()) {
			case MSSQL_2012:
				return " where (fhir_id is null or DATALENGTH(fhir_id) > LEN(fhir_id)) ";
			case H2_EMBEDDED:
			case DERBY_EMBEDDED:
			case MARIADB_10_1:
			case MYSQL_5_7:
			case POSTGRES_9_4:
			case ORACLE_12C:
			case COCKROACHDB_21_1:
			default:
				return " where (fhir_id is null or fhir_id <> trim(fhir_id)) ";
		}
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		// no-op - this is a singleton.
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		// no-op - this is a singleton.
	}
}
