/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
		for (long batchStart = range.getLeft(); batchStart <= range.getRight(); batchStart = batchStart + batchSize) {
			long batchEnd = batchStart + batchSize;
			ourLog.info("Migrating client-assigned ids for pids: {}-{}", batchStart, batchEnd);

			executeSql(
					"hfj_resource",
					"update hfj_resource set fhir_id = trim(fhir_id) where res_id >= ? and res_id < ?",
					batchStart,
					batchEnd);
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
